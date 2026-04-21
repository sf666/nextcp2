package devicedriver.tcp;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpDeviceConnection {

	private static final Logger log = LoggerFactory.getLogger(TcpDeviceConnection.class);

	// -------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------

	private static final int BUFFER_SIZE = 4096;
	private static final long RECEIVE_TIMEOUT_MS = 1000;
	private static final long THREAD_SHUTDOWN_TIMEOUT_MS = 5000;

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	/**
	 * The active socket channel to the remote device. Volatile to ensure
	 * visibility across threads.
	 */
	private volatile SocketChannel socketToDevice = null;

	/**
	 * Optional callback for received data. May be null if dataReceived() is
	 * overridden in a subclass.
	 */
	private final IDataReceivedCallback receivedCallback;

	/** Remote address of the device. */
	private final SocketAddress address;

	/** Lock object for all socket operations. */
	private final Object socketLock = new Object();

	/** Executor for the single read task. */
	private final ExecutorService readExecutor;

	/** Executor for async reconnect operations. */
	private final ExecutorService reconnectExecutor;

	/** Future representing the currently running read task. */
	private volatile Future<?> readFuture = null;

	/** Timestamp of the last successful send operation. */
	private final AtomicLong lastSend = new AtomicLong(System.currentTimeMillis());

	/** Timestamp of the last successful receive operation. */
	private final AtomicLong lastReceive = new AtomicLong(System.currentTimeMillis());

	/**
	 * Flag indicating whether any data has ever been received on this
	 * connection. Used to avoid triggering a reconnect before the first
	 * response is received.
	 */
	private final AtomicBoolean dataEverReceived = new AtomicBoolean(false);

	/**
	 * Flag to prevent multiple simultaneous reconnect attempts.
	 */
	private volatile boolean isReconnecting = false;

	/**
	 * Creates a new TcpDeviceConnection.
	 *
	 * @param address Remote address of the device. Must not be null.
	 * @param receivedCallback Optional callback invoked when data is received.
	 *            May be null if {@link #dataReceived(ByteBuffer)} is
	 *            overridden.
	 * @throws IllegalArgumentException if address is null.
	 */
	public TcpDeviceConnection(SocketAddress address) {
		this(address, null);		
	}
	
	public TcpDeviceConnection(SocketAddress address, IDataReceivedCallback receivedCallback) {
		if (address == null) {
			throw new IllegalArgumentException("address must not be null");
		}

		this.address = address;
		this.receivedCallback = receivedCallback;

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

	/**
	 * Returns the remote socket address of this connection.
	 *
	 * @return the remote {@link SocketAddress}
	 */
	public SocketAddress getSocketAddress() {
		return address;
	}

	/**
	 * Opens the TCP connection to the remote device and starts the read task.
	 * Any previously open connection is closed before attempting to reconnect.
	 *
	 * @return true if the connection was established successfully, false
	 *         otherwise.
	 */
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

	/**
	 * Triggers an asynchronous reconnect. If a reconnect is already in
	 * progress, the call is silently ignored. The reconnect runs on a dedicated
	 * executor to avoid blocking or self-joining the calling thread (e.g. the
	 * read task thread).
	 */
	public void reconnect() {
		if (isReconnecting) {
			log.debug("Reconnect already in progress, skipping ...");
			return;
		}

		reconnectExecutor.submit(() -> {
			isReconnecting = true;
			try {
				log.info("Reconnecting to {} ...", address);

				// Reset timestamps and received flag so the timeout check
				// does not fire immediately after the new connection is
				// established.
				lastSend.set(System.currentTimeMillis());
				lastReceive.set(System.currentTimeMillis());
				dataEverReceived.set(false);

				open();
			} finally {
				isReconnecting = false;
			}
		});
	}

	/**
	 * Sends data to the remote device.
	 * <p>
	 * If no data has been received within {@value #RECEIVE_TIMEOUT_MS}ms after
	 * the first successful receive, a reconnect is triggered and the send is
	 * aborted. The caller is responsible for retrying after the connection is
	 * re-established.
	 * </p>
	 *
	 * @param data the {@link ByteBuffer} containing the data to send. Must not
	 *            be null or empty.
	 */
	public void sendData(ByteBuffer data) {
		if (data == null || !data.hasRemaining()) {
			log.warn("sendData called with null or empty buffer");
			return;
		}

		// Only check the receive timeout after at least one response has been
		// received.
		// This prevents an immediate reconnect on the very first send after
		// connect.
		if (dataEverReceived.get() && (System.currentTimeMillis() - lastReceive.get()) > RECEIVE_TIMEOUT_MS) {
			log.warn("Did not receive any data in the last {}ms, reconnecting ...", RECEIVE_TIMEOUT_MS);
			reconnect();
			// Data is not re-sent after reconnect since the connection state is
			// unclear.
			// The caller should retry once the connection is re-established.
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

	/**
	 * Closes the socket and stops the read task. The executor services remain
	 * available for future reconnect attempts. Call {@link #shutdown()} to
	 * fully release all resources.
	 */
	public void close() {
		closeSocketInternal();
		stopReadTask();
	}

	/**
	 * Fully shuts down this connection, including all executor services. Must
	 * be called when this instance is no longer needed to prevent resource
	 * leaks. Can be overridden in subclasses to release additional resources.
	 */
	public void shutdown() {
		close();
		shutdownExecutor(readExecutor, "read");
		shutdownExecutor(reconnectExecutor, "reconnect");
	}

	// -------------------------------------------------------------------------
	// Protected extension point
	// -------------------------------------------------------------------------

	/**
	 * Called when data has been received from the remote device.
	 * <p>
	 * The default implementation delegates to the {@link IDataReceivedCallback}
	 * if one was provided at construction time. Subclasses may override this
	 * method to handle received data directly.
	 * </p>
	 * <p>
	 * The provided buffer is managed by the read task. Implementations must not
	 * call {@code clear()} or modify the buffer position/limit. Use
	 * {@link ByteBuffer#asReadOnlyBuffer()} if a read-only view is needed.
	 * </p>
	 *
	 * @param receivedBuffer the buffer containing the received data, ready for
	 *            reading (flipped).
	 */
	protected void dataReceived(ByteBuffer receivedBuffer) {
		if (receivedCallback != null) {
			receivedCallback.dataReceived(receivedBuffer);
		} else {
			log.warn("dataReceived() called but no callback set and method not overridden");
		}
	}


	/**
	 * Starts the read task on the read executor. Any previously running read
	 * task is cancelled before starting a new one.
	 */
	private void startReadTask() {
		stopReadTask();

		readFuture = readExecutor.submit(() -> {
			log.info("Read task started for {}", address);
			ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

			while (!Thread.currentThread().isInterrupted()) {
				// Capture a local reference under lock to avoid a race
				// condition
				// between the null-check and the actual read call.
				SocketChannel localSocket;
				synchronized (socketLock) {
					localSocket = socketToDevice;
				}

				if (localSocket == null || !localSocket.isOpen()) {
					log.debug("Socket closed, exiting read task for {}", address);
					break;
				}

				try {
					log.debug("Waiting for data from {} ...", address);
					int bytesRead = localSocket.read(buffer);

					if (bytesRead < 0) {
						// EOF: remote host closed the connection
						log.info("Connection closed by remote host {}, reconnecting ...", address);
						reconnect();
						break;
					} else if (bytesRead == 0) {
						// No data available in non-blocking mode: sleep briefly
						// to avoid CPU spin
						log.debug("No data available, sleeping briefly ...");
						Thread.sleep(10);
						continue;
					} else {
						log.debug("Received {} bytes from {}", bytesRead, address);
						lastReceive.set(System.currentTimeMillis());
						dataEverReceived.set(true);
						buffer.flip();
						dataReceived(buffer);
						buffer.clear();
					}
				} catch (ClosedChannelException e) {
					// Expected when the socket is closed externally while
					// read() is blocking.
					// This is not an error - it happens during intentional
					// close/reconnect.
					log.debug("Socket closed during read for {}, terminating read task", address);
					break;
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

	/**
	 * Cancels the currently running read task and waits for it to terminate.
	 * Waits at most {@value #THREAD_SHUTDOWN_TIMEOUT_MS}ms before giving up.
	 */
	private void stopReadTask() {
		Future<?> currentFuture = readFuture;
		if (currentFuture != null && !currentFuture.isDone()) {
			log.debug("Stopping read task for {}", address);
			currentFuture.cancel(true);
			try {
				currentFuture.get(THREAD_SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS);
			} catch (java.util.concurrent.CancellationException e) {
				// Expected after cancel()
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

	/**
	 * Closes the underlying socket channel without stopping the read task. Used
	 * internally to avoid deadlocks when called from within the read task.
	 * Always sets {@link #socketToDevice} to null, even if close() throws.
	 */
	private void closeSocketInternal() {
		synchronized (socketLock) {
			if (socketToDevice != null) {
				try {
					log.info("Closing socket to {} - isOpen: {}, isBlocking: {}, isConnected: {}", address, socketToDevice.isOpen(),
						socketToDevice.isBlocking(), socketToDevice.isConnected());
					socketToDevice.close();
					log.debug("Socket closed successfully for {}", address);
				} catch (IOException e) {
					log.warn("Error while closing socket for {}", address, e);
				} finally {
					// Always nullify, even if close() threw an exception
					socketToDevice = null;
				}
			}
		}
	}

	/**
	 * Gracefully shuts down the given executor service. Waits up to
	 * {@value #THREAD_SHUTDOWN_TIMEOUT_MS}ms for running tasks to complete.
	 * Forces shutdown if the timeout is exceeded.
	 *
	 * @param executor the executor to shut down
	 * @param name a descriptive name used for log messages
	 */
	private void shutdownExecutor(ExecutorService executor, String name) {
		executor.shutdown();
		try {
			if (!executor.awaitTermination(THREAD_SHUTDOWN_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
				log.warn("{} executor did not terminate within {}ms, forcing shutdown ...", name, THREAD_SHUTDOWN_TIMEOUT_MS);
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.warn("Interrupted while shutting down {} executor for {}", name, address);
			executor.shutdownNow();
		}
	}
}