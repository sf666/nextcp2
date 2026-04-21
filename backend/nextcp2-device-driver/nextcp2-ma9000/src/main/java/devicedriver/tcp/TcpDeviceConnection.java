package devicedriver.tcp;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpDeviceConnection {

	private static final Logger log = LoggerFactory.getLogger(TcpDeviceConnection.class);

	// Constants
	private static final int BUFFER_SIZE = 4096;
	private static final long RECEIVE_TIMEOUT_MS = 1000;
	private static final long THREAD_SHUTDOWN_TIMEOUT_MS = 5000;

	private volatile SocketChannel socketToDevice = null;

	private final IDataReceivedCallback receivedCallback;

	private final SocketAddress address;
	private final Object socketLock = new Object();

	private final ExecutorService readExecutor;
	private final ExecutorService reconnectExecutor;
	private volatile Future<?> readFuture = null;

	private final AtomicLong lastSend = new AtomicLong(System.currentTimeMillis());
	private final AtomicLong lastReceive = new AtomicLong(System.currentTimeMillis());

	private volatile boolean isReconnecting = false;

	public TcpDeviceConnection(SocketAddress address) {
		this(address, null);
	}
	
	public TcpDeviceConnection(SocketAddress address, IDataReceivedCallback receivedCallback) {
		if (address == null) {
			throw new IllegalArgumentException("address must not be null");
		}
		this.address = address;
		this.receivedCallback = receivedCallback;

		// Single-Thread Executors mit aussagekräftigen Thread-Namen
		this.readExecutor = Executors.newSingleThreadExecutor(r -> {
			Thread t = new Thread(r);
			t.setName("tcp-read-thread-" + address);
			t.setDaemon(true);
			return t;
		});

		this.reconnectExecutor = Executors.newSingleThreadExecutor(r -> {
			Thread t = new Thread(r);
			t.setName("tcp-reconnect-thread-" + address);
			t.setDaemon(true);
			return t;
		});
	}

	public SocketAddress getSocketAddress() {
		return address;
	}

	public boolean open() {
		close();
		try {
			synchronized (socketLock) {
				SocketChannel channel = SocketChannel.open(address);
				if (!channel.finishConnect()) {
					log.error("Cannot finish connect to remote address {}", address);
					channel.close();
					return false;
				}
				socketToDevice = channel;
				log.info("Successfully connected to {}", address);
			}
			startReadTask();
			return true;
		} catch (IOException e) {
			log.error("Cannot open socket to {}", address, e);
			socketToDevice = null;
			return false;
		}
	}

	public void reconnect() {
		if (isReconnecting) {
			log.debug("Reconnect already in progress, skipping ...");
			return;
		}

		reconnectExecutor.submit(() -> {
			isReconnecting = true;
			try {
				log.info("Reconnecting to {} ...", address);
				lastSend.set(System.currentTimeMillis());
				lastReceive.set(System.currentTimeMillis());
				open();
			} finally {
				isReconnecting = false;
			}
		});
	}

	public void sendData(ByteBuffer data) {
		if (data == null || !data.hasRemaining()) {
			log.warn("sendData called with null or empty buffer");
			return;
		}

		if ((System.currentTimeMillis() - lastReceive.get()) > RECEIVE_TIMEOUT_MS) {
			log.warn("Did not receive any data in the last {}ms, reconnecting ...", RECEIVE_TIMEOUT_MS);
			reconnect();
			return;
		}

		synchronized (socketLock) {
			if (socketToDevice == null) {
				log.error("Not connected to device at address {}", address);
				return;
			}

			try {
				if (!socketToDevice.isConnected()) {
					log.warn("Socket not connected: {}", socketToDevice);
					return;
				}

				socketToDevice.write(data);
				lastSend.set(System.currentTimeMillis());
				log.debug("Data sent successfully to {}", address);
			} catch (IOException e) {
				log.error("Send error to {}", address, e);
				closeSocketInternal();
			}
		}
	}

	public void close() {
		closeSocketInternal();
		stopReadTask();
	}

	public void shutdown() {
		close();
		shutdownExecutor(readExecutor, "read");
		shutdownExecutor(reconnectExecutor, "reconnect");
	}

	protected void dataReceived(ByteBuffer receivedBuffer) {
		if (receivedCallback != null) {
			receivedCallback.dataReceived(receivedBuffer);
		} else {
			log.warn("dataReceived() called but no callback set and method not overridden");
		}
	}

	private void startReadTask() {
		stopReadTask();

		readFuture = readExecutor.submit(() -> {
			log.info("Read task started for {}", address);
			ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

			while (!Thread.currentThread().isInterrupted()) {
				SocketChannel localSocket;
				synchronized (socketLock) {
					localSocket = socketToDevice;
				}

				if (localSocket == null || !localSocket.isOpen()) {
					log.debug("Socket closed, exiting read task");
					break;
				}

				try {
					log.debug("Waiting for data from {} ...", address);
					int bytesRead = localSocket.read(buffer);

					if (bytesRead < 0) {
						log.info("Connection closed by remote host {}, reconnecting ...", address);
						reconnect();
						break;
					} else if (bytesRead == 0) {
						log.debug("No data available, sleeping briefly ...");
						Thread.sleep(10);
						continue;
					} else {
						log.debug("Received {} bytes from {}", bytesRead, address);
						lastReceive.set(System.currentTimeMillis());
						buffer.flip();
						dataReceived(buffer);
						buffer.clear();
					}
				} catch (IOException e) {
					if (!Thread.currentThread().isInterrupted()) {
						log.error("IO error in read task for {}", address, e);
						closeSocketInternal();
					}
					break;
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.debug("Read task interrupted for {}", address);
					break;
				}
			}

			log.info("Read task terminated for {}", address);
		});
	}

	private void stopReadTask() {
		Future<?> currentFuture = readFuture;
		if (currentFuture != null && !currentFuture.isDone()) {
			log.debug("Stopping read task for {}", address);
			currentFuture.cancel(true);
			try {
				currentFuture.get(THREAD_SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
			} catch (java.util.concurrent.CancellationException e) {
				log.debug("Read task cancelled for {}", address);
			} catch (java.util.concurrent.TimeoutException e) {
				log.warn("Read task did not terminate within {}ms for {}", THREAD_SHUTDOWN_TIMEOUT_MS, address);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.warn("Interrupted while waiting for read task to stop for {}", address);
			} catch (java.util.concurrent.ExecutionException e) {
				log.warn("Read task terminated with exception for {}", address, e.getCause());
			} finally {
				readFuture = null;
			}
		}
	}

	private void closeSocketInternal() {
		synchronized (socketLock) {
			if (socketToDevice != null) {
				try {
					log.info("Closing socket - isOpen: {}, isBlocking: {}, isConnected: {}", socketToDevice.isOpen(),
						socketToDevice.isBlocking(), socketToDevice.isConnected());
					socketToDevice.close();
					log.debug("Socket closed successfully");
				} catch (IOException e) {
					log.warn("Error while closing socket for {}", address, e);
				} finally {
					socketToDevice = null;
				}
			}
		}
	}

	private void shutdownExecutor(ExecutorService executor, String name) {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(THREAD_SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
				log.warn("{} executor did not terminate within {}ms, forcing shutdown ...", name, THREAD_SHUTDOWN_TIMEOUT_MS);
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.warn("Interrupted while shutting down {} executor", name);
			executor.shutdownNow();
		}
	}
}