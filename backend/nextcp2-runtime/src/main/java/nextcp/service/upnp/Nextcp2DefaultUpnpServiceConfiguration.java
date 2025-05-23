/**
 * Copyright (C) 2014 4th Line GmbH, Switzerland and others
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License Version 1 or later
 * ("CDDL") (collectively, the "License"). You may not use this file
 * except in compliance with the License. See LICENSE.txt for more
 * information.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package nextcp.service.upnp;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.jupnp.UpnpServiceConfiguration;
import org.jupnp.binding.xml.DeviceDescriptorBinder;
import org.jupnp.binding.xml.RecoveringUDA10DeviceDescriptorBinderImpl;
import org.jupnp.binding.xml.RecoveringUDA10ServiceDescriptorBinderImpl;
import org.jupnp.binding.xml.ServiceDescriptorBinder;
import org.jupnp.model.Namespace;
import org.jupnp.model.message.UpnpHeaders;
import org.jupnp.model.message.header.UpnpHeader;
import org.jupnp.model.meta.RemoteDeviceIdentity;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.types.ServiceType;
import org.jupnp.transport.impl.DatagramIOConfigurationImpl;
import org.jupnp.transport.impl.DatagramIOImpl;
import org.jupnp.transport.impl.DatagramProcessorImpl;
import org.jupnp.transport.impl.GENAEventProcessorImpl;
import org.jupnp.transport.impl.MulticastReceiverConfigurationImpl;
import org.jupnp.transport.impl.MulticastReceiverImpl;
import org.jupnp.transport.impl.NetworkAddressFactoryImpl;
import org.jupnp.transport.impl.SOAPActionProcessorImpl;
import org.jupnp.transport.impl.jetty.StreamClientConfigurationImpl;
import org.jupnp.transport.spi.DatagramIO;
import org.jupnp.transport.spi.DatagramProcessor;
import org.jupnp.transport.spi.GENAEventProcessor;
import org.jupnp.transport.spi.MulticastReceiver;
import org.jupnp.transport.spi.NetworkAddressFactory;
import org.jupnp.transport.spi.SOAPActionProcessor;
import org.jupnp.transport.spi.StreamClient;
import org.jupnp.transport.spi.StreamServer;
import org.jupnp.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import nextcp.dto.Config;

/**
 * Default configuration data of a typical UPnP stack.
 * <p>
 * This configuration utilizes the default network transport implementation found in
 * {@link org.jupnp.transport.impl}.
 * </p>
 * <p>
 * This configuration utilizes the DOM default descriptor binders found in
 * {@link org.jupnp.binding.xml}.
 * </p>
 * <p>
 * The thread <code>Executor</code> is an <code>Executors.newCachedThreadPool()</code> with
 * a custom {@link JUPnPThreadFactory} (it only sets a thread name).
 * </p>
 * <p>
 * Note that this pool is effectively unlimited, so the number of threads will
 * grow (and shrink) as needed - or restricted by your JVM.
 * </p>
 * <p>
 * The default {@link org.jupnp.model.Namespace} is configured without any
 * base path or prefix.
 * </p>
 *
 * @author Christian Bauer
 * @author Kai Kreuzer - introduced bounded thread pool
 * @author Jochen Hiller - increased thread pool size to 200
 * @author Victor Toni - consolidated transport abstraction into one interface
 */
public class Nextcp2DefaultUpnpServiceConfiguration implements UpnpServiceConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Nextcp2DefaultUpnpServiceConfiguration.class);
	private static final List<String> LOG_LEVEL_MEDIASERVER = List.of("org.jupnp");
	private static final List<String> LOG_LEVEL_BASIC = List.of("net.pms.network.mediaserver.jupnp.transport.impl.UmsDatagramProcessor", "org.jupnp.protocol");
	private static final List<String> LOG_LEVEL_FULL = List.of("org.jupnp.binding", "org.jupnp.model", "org.jupnp.registry", "org.jupnp.transport");

	private static final int CORE_THREAD_POOL_SIZE = 16;
	private static final int THREAD_POOL_SIZE = 200;
	private static final int THREAD_QUEUE_SIZE = 1000;
	private static final boolean THREAD_POOL_CORE_TIMEOUT = true;
	private static final String STREAM_CLIENT_THREAD_NAME = "jupnp-stream-client";
	private static final String STREAM_SERVER_THREAD_NAME = "jupnp-stream-server";
	private static final String REGISTRY_MAINTAINER_THREAD_NAME = "jupnp-registry-maintainer";
	private static final String MULTICAST_RECEIVER_THREAD_NAME = "jupnp-multicast-receiver";
	private static final String DATAGRAM_IO_THREAD_NAME = "jupnp-datagram-io";
	private static final String SYNC_PROTOCOL_THREAD_NAME = "jupnp-sync-protocol";
	private static final String ASYNC_PROTOCOL_THREAD_NAME = "jupnp-async-protocol";
	private static final String REMOTE_LISTENER_THREAD_NAME = "jupnp-remote-listener";
	private static final String REGISTRY_LISTENER_THREAD_NAME = "jupnp-registry-listener";

	private final DatagramProcessor datagramProcessor;
	private final SOAPActionProcessor soapActionProcessor;
	private final GENAEventProcessor genaEventProcessor;
	private final DeviceDescriptorBinder deviceDescriptorBinderUDA10;
	private final ServiceDescriptorBinder serviceDescriptorBinderUDA10;
	
	private final Namespace namespace;
	private final UpnpHeaders headers = new UpnpHeaders();
	private final String upnpBindInterface;

	private boolean useThreadPool = true;
	private boolean multicastReceiverThreadPool = true;
	private boolean datagramIOThreadPool = true;
	private boolean streamClientThreadPool = true;
	private boolean streamServerThreadPool = true;
	private boolean syncProtocolThreadPool = true;
	private boolean asyncProtocolThreadPool = true;
	private boolean remoteListenerThreadPool = true;
	private boolean registryListenerThreadPool = true;
	private boolean registryMaintainerThreadPool = true;
	
	private ExecutorService multicastReceiverExecutorService;
	private ExecutorService datagramIOExecutorService;
	private ExecutorService streamClientExecutorService;
	private ExecutorService streamServerExecutorService;
	private ExecutorService syncProtocolExecutorService;
	private ExecutorService asyncProtocolExecutorService;
	private ExecutorService remoteListenerExecutorService;
	private ExecutorService registryListenerExecutorService;
	private ExecutorService registryMaintainerExecutorService;

	@Autowired
	private JettyTransportConfiguration tc;
	
	@Autowired
	private Config config = null;
	
	public Nextcp2DefaultUpnpServiceConfiguration(Nextcp2UpnpUserConfig userConf) {
		this.upnpBindInterface = userConf.getUpnpBindAddress();
		datagramProcessor = new DatagramProcessorImpl();
		soapActionProcessor = new SOAPActionProcessorImpl();
		genaEventProcessor = new GENAEventProcessorImpl();
		deviceDescriptorBinderUDA10 = new RecoveringUDA10DeviceDescriptorBinderImpl();
		serviceDescriptorBinderUDA10 = new RecoveringUDA10ServiceDescriptorBinderImpl();
		namespace = new Namespace();
		headers.add(UpnpHeader.Type.USER_AGENT.getHttpName(), "nextcp2/GENERIC UPnP/1.0 DLNADOC/1.50 (" + System.getProperty("os.name").replace(" ", "_") + ")");		
		createExecutorServices();		
	}

	

	private void createExecutorServices() {
		if (useThreadPool) {
			if (multicastReceiverThreadPool) {
				LOGGER.trace("Creating multicast receiver executor service");
				multicastReceiverExecutorService = createDefaultExecutorService(MULTICAST_RECEIVER_THREAD_NAME);
			} else {
				LOGGER.trace("Skipping multicast receiver executor service creation.");
			}
			if (datagramIOThreadPool) {
				LOGGER.debug("Creating datagram IO executor service");
				datagramIOExecutorService = createDefaultExecutorService(DATAGRAM_IO_THREAD_NAME);
			} else {
				LOGGER.trace("Skipping datagram IO executor service creation.");
			}
			if (streamClientThreadPool) {
				LOGGER.debug("Creating stream client executor service");
				streamClientExecutorService = createDefaultExecutorService(STREAM_CLIENT_THREAD_NAME);
			} else {
				LOGGER.trace("Skipping stream client executor service creation.");
			}
			if (streamServerThreadPool) {
				LOGGER.debug("Creating stream server executor service");
				streamServerExecutorService = createDefaultExecutorService(STREAM_SERVER_THREAD_NAME);
			} else {
				LOGGER.trace("Skipping stream server executor service creation.");
			}
			if (syncProtocolThreadPool) {
				LOGGER.debug("Creating sync protocol executor service");
				syncProtocolExecutorService = createDefaultExecutorService(SYNC_PROTOCOL_THREAD_NAME);
			} else {
				LOGGER.trace("Skipping sync protocol executor service creation.");
			}
			if (asyncProtocolThreadPool) {
				LOGGER.debug("Creating async protocol executor service");
				asyncProtocolExecutorService = createDefaultExecutorService(ASYNC_PROTOCOL_THREAD_NAME);
			} else {
				LOGGER.debug("Skipping async protocol executor service creation.");
			}
			if (remoteListenerThreadPool) {
				LOGGER.debug("Creating remote listener executor service");
				remoteListenerExecutorService = createDefaultExecutorService(REMOTE_LISTENER_THREAD_NAME);
			} else {
				LOGGER.debug("Skipping remote listener executor service creation.");
			}
			if (registryListenerThreadPool) {
				LOGGER.debug("Creating registry listener executor service");
				registryListenerExecutorService = createDefaultExecutorService(REGISTRY_LISTENER_THREAD_NAME);
			} else {
				LOGGER.debug("Skipping registry listener executor service creation.");
			}
			if (registryMaintainerThreadPool) {
				LOGGER.debug("Creating registry maintainer executor service");
				registryMaintainerExecutorService = createDefaultExecutorService(REGISTRY_MAINTAINER_THREAD_NAME);
			} else {
				LOGGER.debug("Skipping registry maintainer executor service creation.");
			}
		} else {
			LOGGER.debug("Skipping thread pooled executor services creation.");
		}
	}

	protected void shutdownExecutorServices() {
		if (multicastReceiverExecutorService != null) {
			LOGGER.trace("Shutting down multicast receiver executor service");
			multicastReceiverExecutorService.shutdownNow();
		}
		if (datagramIOExecutorService != null) {
			LOGGER.trace("Shutting down datagram IO executor service");
			datagramIOExecutorService.shutdownNow();
		}
		if (streamServerExecutorService != null) {
			LOGGER.trace("Shutting down stream server executor service");
			streamServerExecutorService.shutdownNow();
		}
		if (syncProtocolExecutorService != null) {
			LOGGER.trace("Shutting down sync protocol executor service");
			syncProtocolExecutorService.shutdownNow();
		}
		if (registryListenerExecutorService != null) {
			LOGGER.trace("Shutting down registry listener executor service");
			registryListenerExecutorService.shutdownNow();
		}
		if (registryMaintainerExecutorService != null) {
			LOGGER.trace("Shutting down registry maintainer executor service");
			registryMaintainerExecutorService.shutdownNow();
		}
		if (streamClientExecutorService != null) {
			LOGGER.trace("Shutting down stream client executor service");
			streamClientExecutorService.shutdownNow();
		}
		if (asyncProtocolExecutorService != null) {
			LOGGER.trace("Shutting down async protocol executor service");
			asyncProtocolExecutorService.shutdownNow();
		}
		if (remoteListenerExecutorService != null) {
			LOGGER.trace("Shutting down remote listener executor service");
			remoteListenerExecutorService.shutdownNow();
		}
	}

	private ExecutorService createDefaultExecutorService(String name) {
		return new JUPnPExecutor(name);
	}


	@Override
	public int getRegistryMaintenanceIntervalMillis() {
		return 15000;
	}


	@Override
	public StreamClient createStreamClient() {
		ExecutorService executorService = getStreamClientExecutorService();
		return tc.createStreamClient(executorService, new StreamClientConfigurationImpl(executorService));
	}

	@Override
	public StreamServer createStreamServer(NetworkAddressFactory networkAddressFactory) {
		return tc.createStreamServer(networkAddressFactory.getStreamListenPort());
	}


	@Override
	public ExecutorService getMulticastReceiverExecutor() {
		if (useThreadPool && multicastReceiverThreadPool) {
			return multicastReceiverExecutorService;
		} else {
			return Executors.newCachedThreadPool(new SimpleThreadFactory(MULTICAST_RECEIVER_THREAD_NAME, true));
		}
	}

	@Override
	public ExecutorService getDatagramIOExecutor() {
		if (useThreadPool && datagramIOThreadPool) {
			return datagramIOExecutorService;
		} else {
			return Executors.newCachedThreadPool(new SimpleThreadFactory(DATAGRAM_IO_THREAD_NAME, true));
		}
	}

	@Override
	public ExecutorService getAsyncProtocolExecutor() {
		if (useThreadPool && asyncProtocolThreadPool) {
			return asyncProtocolExecutorService;
		} else {
			return Executors.newCachedThreadPool(new SimpleThreadFactory(ASYNC_PROTOCOL_THREAD_NAME, true));
		}
	}

	@Override
	public ExecutorService getSyncProtocolExecutorService() {
		if (useThreadPool && syncProtocolThreadPool) {
			return syncProtocolExecutorService;
		} else {
			return Executors.newCachedThreadPool(new SimpleThreadFactory(SYNC_PROTOCOL_THREAD_NAME, true));
		}
	}

	public ExecutorService getStreamClientExecutorService() {
		if (useThreadPool && streamClientThreadPool) {
			return streamClientExecutorService;
		} else {
			return Executors.newCachedThreadPool(new SimpleThreadFactory(STREAM_CLIENT_THREAD_NAME, true));
		}
	}

	@Override
	public ExecutorService getStreamServerExecutorService() {
		if (useThreadPool && streamServerThreadPool) {
			return streamServerExecutorService;
		} else {
			return Executors.newCachedThreadPool(new SimpleThreadFactory(STREAM_SERVER_THREAD_NAME, true));
		}
	}

	@Override
	public Executor getRegistryMaintainerExecutor() {
		if (useThreadPool && registryMaintainerThreadPool) {
			return registryMaintainerExecutorService;
		} else {
			return Executors.newCachedThreadPool(new SimpleThreadFactory(REGISTRY_MAINTAINER_THREAD_NAME, true));
		}
	}

	@Override
	public Executor getRegistryListenerExecutor() {
		if (useThreadPool && registryListenerThreadPool) {
			return registryListenerExecutorService;
		} else {
			return Executors.newCachedThreadPool(new SimpleThreadFactory(REGISTRY_LISTENER_THREAD_NAME, true));
		}
	}

	@Override
	public Executor getRemoteListenerExecutor() {
		if (useThreadPool && remoteListenerThreadPool) {
			return remoteListenerExecutorService;
		} else {
			return Executors.newCachedThreadPool(new SimpleThreadFactory(REMOTE_LISTENER_THREAD_NAME, true));
		}
	}

	@Override
	public void shutdown() {
		LOGGER.trace("Shutting down executor services");
		shutdownExecutorServices();
		// create the executor again ready for reuse in case the runtime is started up again.
		createExecutorServices();
	}

	@Override
	public DatagramProcessor getDatagramProcessor() {
		return datagramProcessor;
	}

	@Override
	public SOAPActionProcessor getSoapActionProcessor() {
		return soapActionProcessor;
	}

	@Override
	public GENAEventProcessor getGenaEventProcessor() {
		return genaEventProcessor;
	}

	@Override
	public DeviceDescriptorBinder getDeviceDescriptorBinderUDA10() {
		return deviceDescriptorBinderUDA10;
	}

	@Override
	public ServiceDescriptorBinder getServiceDescriptorBinderUDA10() {
		return serviceDescriptorBinderUDA10;
	}

	@Override
	public ServiceType[] getExclusiveServiceTypes() {
		return new ServiceType[0];
	}

	/**
	 * @return Defaults to <code>false</code>.
	 */
	@Override
	public boolean isReceivedSubscriptionTimeoutIgnored() {
		return false;
	}

	@Override
	public Integer getRemoteDeviceMaxAgeSeconds() {
		return null;
	}

	@Override
	public UpnpHeaders getEventSubscriptionHeaders(RemoteService service) {
		return null;
	}

	@Override
	public Namespace getNamespace() {
		return namespace;
	}

	public static class JUPnPExecutor extends ThreadPoolExecutor {

		public JUPnPExecutor(String name) {
			this(new SimpleThreadFactory(name),
					new ThreadPoolExecutor.DiscardPolicy() {
				// The pool is bounded and rejections will happen during shutdown
				@Override
				public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
					// Log and discard
					LOGGER.warn("Thread pool rejected execution of " + runnable.getClass());
					super.rejectedExecution(runnable, threadPoolExecutor);
				}
			}
			);
		}

		public JUPnPExecutor(ThreadFactory threadFactory, RejectedExecutionHandler rejectedHandler) {
			// This is the same as Executors.newCachedThreadPool
			super(CORE_THREAD_POOL_SIZE,
					THREAD_POOL_SIZE,
					10L,
					TimeUnit.SECONDS,
					new ArrayBlockingQueue<>(THREAD_QUEUE_SIZE),
					threadFactory,
					rejectedHandler
			);
			allowCoreThreadTimeOut();
		}

		private void allowCoreThreadTimeOut() {
			allowCoreThreadTimeOut(THREAD_POOL_CORE_TIMEOUT);
		}

		@Override
		protected void afterExecute(Runnable runnable, Throwable throwable) {
			super.afterExecute(runnable, throwable);
			if (throwable != null) {
				Throwable cause = Exceptions.unwrap(throwable);
				if (cause instanceof InterruptedException) {
					// Ignore this, might happen when we shutdownNow() the executor. We can't
					// log at this point as the logging system might be stopped already (e.g.
					// if it's a CDI component).
					return;
				}
				// Log only
				LOGGER.warn("Thread terminated " + runnable + " abruptly with exception: " + throwable);
				LOGGER.warn("Root cause: " + cause);
			}
		}
	}

	@Override
	public NetworkAddressFactory createNetworkAddressFactory() {
		return new Nextcp2NetworkAddressFactory(
			// TODO make port configurable ... 
			5003,
			NetworkAddressFactoryImpl.DEFAULT_MULTICAST_RESPONSE_LISTEN_PORT,
			upnpBindInterface);
	}

	@Override
	public MulticastReceiver createMulticastReceiver(NetworkAddressFactory networkAddressFactory) {
		return new MulticastReceiverImpl(
			new MulticastReceiverConfigurationImpl(
					networkAddressFactory.getMulticastGroup(),
					networkAddressFactory.getMulticastPort()
			)
	);
	}

	@Override
	public DatagramIO createDatagramIO(NetworkAddressFactory networkAddressFactory) {
		return new DatagramIOImpl(new DatagramIOConfigurationImpl());
	}

	@Override
	public int getAliveIntervalMillis() {
		return 5000;
	}

	@Override
	public UpnpHeaders getDescriptorRetrievalHeaders(RemoteDeviceIdentity identity) {
		return headers;
	}


}
