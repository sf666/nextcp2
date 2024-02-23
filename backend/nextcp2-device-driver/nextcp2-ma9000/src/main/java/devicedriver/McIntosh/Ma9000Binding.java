package devicedriver.McIntosh;

import java.io.IOException;
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

	private static final Logger log = LoggerFactory.getLogger(Ma9000Binding.class.getName());

	private McIntoshDeviceConnection device = null;

	private DeviceDriverState state = new DeviceDriverState();

	private IDeviceDriverCallback callback = null;

	private InputManager inputManager = new InputManager();

	public Ma9000Binding(SocketAddress hostAddress, IDeviceDriverCallback callback, String rendererUdn) throws IOException {
		if (hostAddress == null) {
			throw new RuntimeException("hostAddress shall not be null");
		}

		log.info("Initializing MA 9000 driver. connecting to : " + hostAddress.toString());
		this.callback = callback;

		state.rendererUDN = rendererUdn;
		state.volume = 0;
		state.standby = true;
		state.hasDeviceDriver = true;
		state.input = new InputSourceDto();
		state.balance = 0;

		device = new McIntoshDeviceConnection(this, hostAddress, null);
		device.open();

		checkPowerState();
	}

	private void checkPowerState() {
		device.send(Commands.POWER_STATUS);
	}

	@Override
	public void standbyStateChanged(boolean standbyState) {
		if (deviceSwitchedOn(standbyState)) {
			readDeviceInfoAfterPowerChange();
		}
		state.standby = standbyState;
		callback.standbyChanged(standbyState); // convert from power logic to
												 // standby logic
	}

	private boolean deviceSwitchedOn(boolean standbyState) {
		return standbyState == false;
	}

	/**
	 * device information can only be read, if the device is powered on.
	 */
	private void readDeviceInfoAfterPowerChange() {
		device.send(Commands.INPUT_STATUS);
		device.send(Commands.VOLUME_STATUS);
		// Do not read power status, since this method is called on power change
		// ... this would end in a loop
	}

	public int getVolume() {
		if (state.volume != null) {
			return state.volume;
		}

		return 0;
	}

	@Override
	public void setStandby(boolean gotoStandybyMode) {
		if (!gotoStandybyMode) {
			device.send(Commands.POWER_ON);
		} else {
			device.send(Commands.POWER_OFF);
		}
		log.info(
			String.format("MA9000 at %s : set standby to %s", device.getSocketAddress().toString(), Boolean.toString(gotoStandybyMode)));
	}

	@Override
	public void setVolume(int volInPercent) {
		device.send(Commands.VOLUME_SET_PERCENT, volInPercent);
	}

	/**
	 * Volume is delivered in percent
	 */
	@Override
	public void volumeStatusChanged(int volume) {
		state.volume = volume;
		callback.volumeChanged(volume);
	}

	@Override
	public DeviceDriverState getCurrentState() {
		return state;
	}

	@Override
	public boolean getStandby() {
		if (state.standby != null) {
			return state.standby;
		}

		return false;
	}

	@Override
	public void setInput(String id) {
		device.send(Commands.INPUT_SELECT, id);
	}

	@Override
	public InputSourceDto getInput() {
		return state.input;
	}

	@Override
	public void inputChanged(String input) {
		try {
			log.debug("McIntosh input changed to : " + input);
			int id = Integer.parseInt(input.trim());
			state.input = inputManager.getInputSource(id);
			callback.inputChanged(state.input);
		} catch (Exception e) {
			log.warn("cannot parse input : " + input != null ? input : "NULL", e);
		}
	}

	@Override
	public void trimBalanceChanged(int balance) {
		try {
			log.debug("trim balance changed to : {}", balance);
			state.balance = balance;
			callback.trimBalanaceChanged(state.balance);
		} catch (Exception e) {
			log.error("trimBalanceChanged", e);
		}
	}

	@Override
	public void trimInputChanged(float trimInp) {
		log.debug("trim input changed to : {}", trimInp);
	}

	@Override
	public void trimEqChanged(boolean eq) {
		log.debug("trim EQ changed to : {}", eq);
	}

	@Override
	public void setTrimBalance(Integer balance) {
		device.send(Commands.TRIM_BALANCE, balance);
	}
}
