package devicedriver.McIntosh;

import java.net.SocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import devicedriver.McIntosh.control.Commands;
import devicedriver.McIntosh.tcp.IMcIntoshDeviceChanged;
import devicedriver.McIntosh.tcp.McIntoshDeviceConnection;
import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.devicedriver.IDeviceDriverService;
import nextcp.dto.DeviceDriverState;
import nextcp.dto.InputSourceDto;

public class Ma9000Binding implements IMcIntoshDeviceChanged, IDeviceDriverService {

	private static final Logger log = LoggerFactory.getLogger(Ma9000Binding.class);

	private volatile McIntoshDeviceConnection device = null;
	private final DeviceDriverState state = new DeviceDriverState();
	private final IDeviceDriverCallback callback;
	private final InputManager inputManager = new InputManager();
	private final Object stateLock = new Object();

	public Ma9000Binding(SocketAddress hostAddress, IDeviceDriverCallback callback, String rendererUdn) {
		if (hostAddress == null) {
			throw new IllegalArgumentException("hostAddress must not be null");
		}
		if (callback == null) {
			throw new IllegalArgumentException("callback must not be null");
		}
		if (rendererUdn == null) {
			throw new IllegalArgumentException("rendererUdn must not be null");
		}

		log.info("Initializing MA9000 driver, connecting to: {}", hostAddress);
		this.callback = callback;

		state.rendererUDN = rendererUdn;
		state.volume = 0;
		state.standby = true;
		state.hasDeviceDriver = true;
		state.input = new InputSourceDto();
		state.balance = 0;

		start(hostAddress);
	}

	@Override
	public void start() {
		if (device != null) {
			start(device.getSocketAddress());
		} else {
			log.error("Cannot restart: no previous device connection available");
		}
	}

	private void start(SocketAddress hostAddress) {
		log.info("Starting physical device connection to {} ...", hostAddress);

		McIntoshDeviceConnection oldDevice = device;
		if (oldDevice != null) {
			oldDevice.shutdown();
		}

		McIntoshDeviceConnection newDevice = new McIntoshDeviceConnection(this, hostAddress);
		device = newDevice;

		Thread t = new Thread(() -> {
			if (newDevice.open()) {
				log.info("Connection established to {}", hostAddress);
				checkPowerState();
			} else {
				log.error("Failed to open connection to {}", hostAddress);
			}
		}, "MA9000-connect-thread");
		t.setDaemon(true);
		t.start();
	}

	@Override
	public void stop() {
		McIntoshDeviceConnection currentDevice = device;
		if (currentDevice != null) {
			log.info("Stopping physical device ...");
			currentDevice.shutdown();
			device = null;
		}
	}

	private void checkPowerState() {
		device.send(Commands.POWER_STATUS);
	}

	@Override
	public void standbyStateChanged(boolean standbyState) {
		log.debug("Standby state changed to: {}", standbyState);

		if (!standbyState) {
			readDeviceInfoAfterPowerChange();
		}

		synchronized (stateLock) {
			state.standby = standbyState;
		}
		callback.standbyChanged(standbyState);
	}

	/**
	 * read device information after power on.
	 * power state will NOT be read to avoid an endless loop.
	 */
	private void readDeviceInfoAfterPowerChange() {
		device.send(Commands.INPUT_STATUS);
		device.send(Commands.VOLUME_STATUS);
	}

	public int getVolume() {
		synchronized (stateLock) {
			return state.volume != null ? state.volume : 0;
		}
	}

	@Override
	public void setStandby(boolean gotoStandbyMode) {
		log.info("MA9000 at {} : set standby to {}", device.getSocketAddress(), gotoStandbyMode);

		if (!gotoStandbyMode) {
			device.send(Commands.POWER_ON);
		} else {
			device.send(Commands.POWER_OFF);
		}
	}

	@Override
	public void setVolume(int volInPercent) {
		device.send(Commands.VOLUME_SET_PERCENT, volInPercent);
	}

	@Override
	public void volumeStatusChanged(int volume) {
		log.debug("Volume changed to: {}", volume);
		synchronized (stateLock) {
			state.volume = volume;
		}
		callback.volumeChanged(volume);
	}

	@Override
	public DeviceDriverState getCurrentState() {
		synchronized (stateLock) {
			return state;
		}
	}

	@Override
	public boolean getStandby() {
		synchronized (stateLock) {
			return state.standby != null ? state.standby : false;
		}
	}

	@Override
	public void setInput(String id) {
		device.send(Commands.INPUT_SELECT, id);
	}

	@Override
	public InputSourceDto getInput() {
		synchronized (stateLock) {
			return state.input;
		}
	}

	@Override
	public void inputChanged(String input) {
		log.debug("McIntosh input changed to: {}", input);
		try {
			int id = Integer.parseInt(input.trim());
			InputSourceDto inputSource = inputManager.getInputSource(id);
			synchronized (stateLock) {
				state.input = inputSource;
			}
			callback.inputChanged(inputSource);
		} catch (NumberFormatException e) {
			// Fix: Operator-Precedence Bug behoben
			log.warn("Cannot parse input: '{}'", input != null ? input : "NULL", e);
		}
	}

	@Override
	public void trimBalanceChanged(int balance) {
		log.debug("Trim balance changed to: {}", balance);
		synchronized (stateLock) {
			state.balance = balance;
		}
		callback.trimBalanaceChanged(balance);
	}

	@Override
	public void trimInputChanged(float trimInp) {
		log.debug("Trim input changed to: {}", trimInp);
	}

	@Override
	public void trimEqChanged(boolean eq) {
		log.debug("Trim EQ changed to: {}", eq);
	}

	@Override
	public void setTrimBalance(Integer balance) {
		device.send(Commands.TRIM_BALANCE, balance);
	}
}