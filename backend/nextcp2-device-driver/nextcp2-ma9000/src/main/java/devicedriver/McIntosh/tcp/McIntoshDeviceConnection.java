package devicedriver.McIntosh.tcp;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.OptionalInt;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import devicedriver.McIntosh.Ma9000Binding;
import devicedriver.McIntosh.control.BaseCommandStructure;
import devicedriver.tcp.TcpDeviceConnection;
import devicedriver.util.CharBufferUtil;

public class McIntoshDeviceConnection extends TcpDeviceConnection {

	private static final Logger log = LoggerFactory.getLogger(McIntoshDeviceConnection.class);

	private static final Pattern RESPONSE_PATTERN = Pattern.compile("\\((.+?)\\)");

	private final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(50);
	private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 10, 5, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.CallerRunsPolicy());

	private final Ma9000Binding ma9000Binding;
	private final Map<String, CommandHandler> commandHandlers;

	@FunctionalInterface
	private interface CommandHandler {

		void handle(String value);
	}

	public McIntoshDeviceConnection(Ma9000Binding ma9000Binding, SocketAddress address) {
		super(address);
		if (ma9000Binding == null) {
			throw new IllegalArgumentException("ma9000Binding must not be null");
		}
		this.ma9000Binding = ma9000Binding;
		this.commandHandlers = buildCommandHandlers();
	}

	private Map<String, CommandHandler> buildCommandHandlers() {
		return Map.of("PWR", value -> ma9000Binding.standbyStateChanged("0".equals(value)), "VOL",
			value -> parseValue(value, "VOL").ifPresent(ma9000Binding::volumeStatusChanged), "TBA",
			value -> parseValue(value, "TBA").ifPresent(ma9000Binding::trimBalanceChanged), "INP",
			value -> ma9000Binding.inputChanged(value));
	}

	/**
	 * <pre>
	 * Device INIT looks like this:
	 *
	 * (MA9000)(Serial Number: AFK1456)(FW Version: 1.04)(PWR 1)(VOL 56)(MUT 0)
	 * (OP1 1)(OP2 1)(INP 15)(STA 1)(TBA -2)(TIN 0)(TEQ 0)(TPR 3)(TPC 1)
	 * (TMO 0)(TML 1)(TDB 3)(THH 1)(HPS 2)
	 *
	 * </pre>
	 */
	@Override
	protected void dataReceived(ByteBuffer receivedBuffer) {
		// Lokale Instanz → CharsetDecoder ist nicht thread-safe
		try {
			CharBuffer lastResponse = StandardCharsets.UTF_8.newDecoder().decode(receivedBuffer.asReadOnlyBuffer());

			log.info("Data received: {}", lastResponse);

			String[] lines = CharBufferUtil.split(lastResponse, "\r\n");
			for (String line : lines) {
				Matcher matcher = RESPONSE_PATTERN.matcher(line);
				while (matcher.find()) {
					asyncStateChange(matcher.group(1));
				}
			}
		} catch (CharacterCodingException e) {
			log.error("Failed to decode received data", e);
		}
	}

	private void asyncStateChange(String command) {
		if (command == null) {
			log.warn("Received null command, ignoring ...");
			return;
		}

		threadPool.execute(() -> {
			log.info("Status received from device: {}", command);

			if (command.length() < 3) {
				log.warn("Command too short to parse: '{}'", command);
				return;
			}

			String prefix = command.substring(0, 3);
			String value = command.length() > 4 ? command.substring(4) : "";

			CommandHandler handler = commandHandlers.get(prefix);
			if (handler != null) {
				handler.handle(value);
			} else {
				log.info("Received unhandled command: {}", command);
			}
		});
	}

	public void send(BaseCommandStructure request, Object... params) {
		byte[] deviceRequest = request.getCommandAsByteArray(params);
		ByteBuffer buffer = ByteBuffer.allocateDirect(deviceRequest.length);
		buffer.put(deviceRequest);
		buffer.flip();
		log.info("Sending message to device: {}", new String(deviceRequest, StandardCharsets.UTF_8));
		sendData(buffer);
	}

	@Override
	public void shutdown() {
		super.shutdown();
		shutdownThreadPool();
	}

	private OptionalInt parseValue(String value, String commandName) {
		try {
			return OptionalInt.of(Integer.parseInt(value.trim()));
		} catch (NumberFormatException e) {
			log.error("Cannot parse value '{}' for command '{}'", value, commandName);
			return OptionalInt.empty();
		}
	}

	private void shutdownThreadPool() {
		threadPool.shutdown();
		try {
			if (!threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
				log.warn("ThreadPool did not terminate in time, forcing shutdown ...");
				threadPool.shutdownNow();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.warn("Interrupted while shutting down ThreadPool");
			threadPool.shutdownNow();
		}
	}
}