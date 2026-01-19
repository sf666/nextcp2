/*
 * This file is part of Universal Media Server, based on PS3 Media Server.
 *
 * This program is a free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; version 2 of the License only.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package nextcp.service.upnp;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import org.eclipse.jetty.ee11.servlet.ServletContextHandler;
import org.eclipse.jetty.ee11.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.util.thread.VirtualThreadPool;
import org.jupnp.transport.spi.ServletContainerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import jakarta.servlet.Servlet;

/**
 * A singleton wrapper of a EE10 <code>org.eclipse.jetty.server.Server</code>.
 * <p>
 * This {@link ServletContainerAdapter} starts a Jetty 12.x instance on its own
 * and stops it. Only one single context and servlet is registered, to handle
 * UPnP requests.
 * </p>
 * <p>
 * This implementation might work on Android (not tested within JUPnP),
 * dependencies is <code>jetty-ee10-servlet</code> Maven module.
 * </p>
 *
 * @author Christian Bauer - initial contribution
 * @author Victor Toni - refactoring for JUPnP
 * @author Surf@ceS - adapt to v12 ee10 servlet
 */
@Component
public class JettyServletContainer
	implements JakartaServletContainerAdapter {

	private static final Logger log = LoggerFactory.getLogger(JettyServletContainer.class.getName());

    protected Server server;

	public JettyServletContainer() {
		resetServer();
	}

	@Override
	public synchronized void setExecutorService(ExecutorService executorService) {
		// the Jetty server has its own QueuedThreadPool
	}

	@Override
	public synchronized int addConnector(String host, int port) throws IOException {
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(host);
        connector.setPort(port);
        server.addConnector(connector);
        return port;
	}

	@Override
	public synchronized void registerServlet(String contextPath, Servlet servlet) {		
		if (server.getHandler() == null) {
			ContextHandlerCollection contextHandlers = new ContextHandlerCollection();
			server.setHandler(contextHandlers);
		}
		if (server.getHandler() instanceof ContextHandlerCollection contextHandlers) {
			log.info("Registering UPnP servlet under context path: {}", contextPath);
			ServletContextHandler servletHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
			if (contextPath != null && !contextPath.isEmpty()) {
				servletHandler.setContextPath(contextPath);
			}

			final ServletHolder s = new ServletHolder("UPNP HTTP SERVER", servlet);
			servletHandler.addServlet(s, "/dev/*");
			contextHandlers.addHandler(servletHandler);
		} else {
			log.trace("Server handler is not a ContextHandlerCollection");
		}
	}

	@Override
	public synchronized void startIfNotRunning() {
		if (!server.isStarted() && !server.isStarting()) {
			log.info("Starting Jetty server {}", Server.getVersion());
			try {
				server.start();
			} catch (Exception e) {
				log.error("Couldn't start Jetty server", e);
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public synchronized void stopIfRunning() {
		if (!server.isStopped() && !server.isStopping()) {
			log.info("Stopping Jetty server...");
			try {
				server.stop();
			} catch (Exception e) {
				log.error("Couldn't stop Jetty server", e);
				throw new RuntimeException(e);
			} finally {
				resetServer();
			}
		}
	}

	private void resetServer() {
		VirtualThreadPool threadPool = new VirtualThreadPool();
		threadPool.setName("jupnp-stream-server");
		server = new Server(threadPool);
	}

}
