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
    private volatile boolean terminateReadThread = false;
    private IDataReceivedCallback receivedCallback = null;
    private volatile Thread readThread = null;

    private long lastSend = System.currentTimeMillis();
    private long lastReceive = lastSend;

    private SocketAddress address;

    public TcpDeviceConnection(SocketAddress address, IDataReceivedCallback receivedCallback)
    {
        if (address == null)
        {
            throw new RuntimeException("Missing address parameter ...");
        }
        this.address = address;
        this.receivedCallback = receivedCallback;
    }
    
    public void reconnect()
    {
        log.info("reconnecting ... ");
        lastSend = System.currentTimeMillis();
        lastReceive = lastSend;
        open();
    }

    public SocketAddress getSocketAddress()
    {
        return address;
    }

    public void open()
    {
        closeIfOpen();
        try
        {
            socketToDevice = SocketChannel.open(address);
            if (!socketToDevice.finishConnect())
            {
                log.error("cannot finish connect to remote address " + address);
            }
            createReadTread();
        }
        catch (IOException e)
        {
            log.error("cannot open socket", e);
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
            while (!readThread.isInterrupted() && !terminateReadThread && socketToDevice.isConnected())
            {
                try
                {
                    log.info("waiting for receiving data ... ");
                    int size = socketToDevice.read(buffer);
                    if (size <= 0)
                    {
                        log.info("received 0 or less data. Reconnecting.");
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
            log.warn("terminated read thread. Reason  :");
            log.info("!readThread.isInterrupted()     : " + !readThread.isInterrupted());
            log.info("!terminateReadThread            : " + !terminateReadThread);
            log.info("socketToDevice.isConnected()    : " + socketToDevice.isConnected());
            readThread = null;
        };

        readThread = new Thread(readRunnable);
        readThread.setName("tcp read thread for device " + address + " : " + System.currentTimeMillis());
        readThread.setDaemon(false);
        readThread.start();
        log.info("Started tcp read thread : " + readThread.getName());
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
            log.error("Not connected to device. Reconnecting to address " + this.address);
            reconnect();
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
                log.warn("Not connected to tcpDevice ... " + socketToDevice);
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
            log.info("interrupting tcp read thread " + readThread.getName());
            readThread.interrupt();
            while (readThread != null)
            {
                try
                {
                    Thread.currentThread().wait(1000);
                }
                catch (InterruptedException e)
                {
                    log.warn("wait 1 sec ", e);
                }
            }
        }
        if (socketToDevice != null)
        {
            try
            {
                log.info("closing connection to device : " + socketToDevice.getRemoteAddress().toString());
            }
            catch (IOException e1)
            {
                log.warn("logging data", e1);
            }
            try
            {
                socketToDevice.close();
            }
            catch (Exception e)
            {
                log.warn("closing socket", e);
            }
        }
    }
}
