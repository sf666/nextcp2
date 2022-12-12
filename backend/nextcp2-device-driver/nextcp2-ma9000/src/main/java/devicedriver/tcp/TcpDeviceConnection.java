package devicedriver.tcp;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpDeviceConnection
{
    private static final Logger log = LoggerFactory.getLogger(TcpDeviceConnection.class.getName());

    private SocketChannel socketToDevice = null;
    private boolean terminateReadThread = false;
    private IDataReceivedCallback receivedCallback = null;

    private SocketAddress address;

    public void reconnect()
    {
        open(address, receivedCallback);
    }

    public void open(SocketAddress address)
    {
        open(address, null);
    }

    public SocketAddress getSocketAddress()
    {
        return address;
    }

    public void open(SocketAddress address, IDataReceivedCallback receivedCallback)
    {
        if (address == null)
        {
            throw new RuntimeException("Missing address parameter ...");
        }

        this.address = address;
        closeIfOpen();
        try
        {
            socketToDevice = SocketChannel.open(address);
            this.receivedCallback = receivedCallback;
            createReadTread();
        }
        catch (IOException e)
        {
            log.error("Cannot open Socket", e);
        }
    }

    /**
     * Falls die Benachrichtigung über Composition erfolgt ...
     * 
     * @param receivedBuffer
     */
    protected void dataReceived(ByteBuffer receivedBuffer)
    {
        if (receivedCallback != null)
        {
            receivedCallback.dataReceived(receivedBuffer);
        }
    }

    private void createReadTread()
    {
        Runnable readRunnable = () -> {
            ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
            terminateReadThread = false;
            while (!terminateReadThread)
            {
                try
                {
                    int size = socketToDevice.read(buffer);
                    if (size == -1)
                    {
                        closeIfOpen();
                    }
                    else
                    {
                        buffer.flip();
                        dataReceived(buffer);
                    }
                }
                catch (IOException e)
                {
                    closeIfOpen();
                    e.printStackTrace();
                }
            }
        };

        Thread readThread = new Thread(readRunnable);
        readThread.setName("incoming thread for device " + address);
        readThread.setDaemon(true);
        readThread.start();
    }

    public void sendData(ByteBuffer data)
    {
        if (socketToDevice == null)
        {
            log.error("Not connected to device ... ");
            if (address != null)
            {
                open(address);
            }
        }

        try
        {
            if (socketToDevice.isConnected())
            {
                socketToDevice.write(data);
            }
            else
            {
                log.warn("Not connected to tcpDevice ... ");
            }
        }
        catch (IOException e)
        {
            log.error("send error", e);
            closeIfOpen();
        }
    }

    private void closeIfOpen()
    {
        terminateReadThread = true;
        if (socketToDevice != null && socketToDevice.isConnected())
        {
            try
            {
                log.info("closing connection to device : " + socketToDevice.getRemoteAddress().toString());
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
            try
            {
                socketToDevice.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
