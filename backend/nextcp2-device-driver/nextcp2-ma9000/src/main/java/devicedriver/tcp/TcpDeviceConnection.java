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
    private Thread readThread = null;

    private long lastSend = System.currentTimeMillis();
    private long lastReceive = lastSend;
    
    private SocketAddress address;

    public void reconnect()
    {
        log.info("reconnecting ... ");
        lastSend = System.currentTimeMillis();
        lastReceive = lastSend;        
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
            while (!readThread.isInterrupted() && !terminateReadThread)
            {
                try
                {
                    int size = socketToDevice.read(buffer);
                    if (size <= 0)
                    {
                        closeIfOpen();
                        reconnect();
                    }
                    else
                    {
                        buffer.flip();
                        dataReceived(buffer);
                        lastReceive = System.currentTimeMillis();
                    }
                }
                catch (IOException e)
                {
                    closeIfOpen();
                    e.printStackTrace();
                }
            }
        };

        readThread = new Thread(readRunnable);
        readThread.setName("incoming read thread for device " + address + " : " + System.currentTimeMillis());
        readThread.setDaemon(true);
        readThread.start();
    }

    public void sendData(ByteBuffer data)
    {
        if ((lastSend - lastReceive) > 1000)
        {
            log.warn("did not receive any data in the last second ... reconnecting");
            reconnect();
        }
        
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
                lastSend = System.currentTimeMillis();
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
        if (readThread != null)
        {
            readThread.interrupt();
        }
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
