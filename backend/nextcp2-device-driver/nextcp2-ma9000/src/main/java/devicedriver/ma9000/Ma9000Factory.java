package devicedriver.ma9000;

import java.net.InetSocketAddress;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import devicedriver.McIntosh.Ma9000Binding;
import nextcp.devicedriver.IDeviceCapabilities;
import nextcp.devicedriver.IDeviceDriverCallback;
import nextcp.devicedriver.IDeviceDriverFactory;
import nextcp.devicedriver.IDeviceDriverService;
import nextcp.util.BackendException;

public class Ma9000Factory implements IDeviceDriverFactory
{
    private static final Logger log = LoggerFactory.getLogger(Ma9000Factory.class.getName());

    private IDeviceCapabilities capabilities = new Ma9000Capabilities();

    @Override
    public IDeviceCapabilities getDriverCapabilities()
    {
        return capabilities;
    }

    @Override
    public IDeviceDriverService getDriverFor(String connectionString, IDeviceDriverCallback callback, String rendererUdn)
    {
        try
        {
            if (StringUtils.isBlank(connectionString))
            {
                throw new BackendException(BackendException.DEVICE_DRIVER_CONNECTION_STRING_EMPTY, "connection string shall not be null or empty.");
            }
            if (connectionString == null || !connectionString.contains(":"))
            {
                throw new RuntimeException(String.format("invalid connection string : %s", connectionString));
            }
            String[] part = connectionString.split(":");
            checkParts(part);
            return new Ma9000Binding(new InetSocketAddress(part[0], Integer.parseInt(part[1])), callback, rendererUdn);
        }
        catch (Exception e)
        {
            log.error(String.format("failed aquiring driver for connectin string >%s<", connectionString), e);
            return null;
        }
    }

    private void checkParts(String[] part)
    {
        if (StringUtils.isBlank(part[0]) || StringUtils.isBlank(part[1]))
        {
            throw new BackendException(BackendException.DEVICE_DRIVER_CONNECTION_STRING_ERROR, "IP and port number shall not be null.");
        }
        int i = Integer.parseInt(part[1]);
        if (i < 0 || i > 65565)
        {
            throw new BackendException(BackendException.DEVICE_DRIVER_CONNECTION_PORT_RANGE, "Port out of Range");
        }
    }

    @Override
    public String getTargetUsageDescription()
    {
        return "getDriverFor(\"IP:PORT\")";
    }
}
