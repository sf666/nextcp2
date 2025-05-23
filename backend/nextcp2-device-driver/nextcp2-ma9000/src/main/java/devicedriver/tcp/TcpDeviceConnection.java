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
    private IDataReceivedCallback receivedCallback = null;
    private volatile Thread readThread = null;

    private volatile long lastSend = System.currentTimeMillis();
    private volatile long lastReceive = lastSend;

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
        close();
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
            while (!Thread.currentThread().isInterrupted())
            {
                try
                {
                    log.info("blocked wait for reading data ... ");
                    int size = socketToDevice.read(buffer);
                    if (size <= 0)
                    {
                        log.info("received 0 or less data. Reconnecting ...");
                        reconnect();
                    }
                    else
                    {
                        log.debug("data received. Processing ... ");
                        lastReceive = System.currentTimeMillis();
                        buffer.flip();
                        dataReceived(buffer);
                    }
                }
                catch (IOException e)
                {
                    close();
                    e.printStackTrace();
                }
            }
            log.info("read tread terminated : " + Thread.currentThread().getName());            
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
            if (!socketToDevice.isConnected())
            {
                log.warn("Not connected to tcpDevice ... " + socketToDevice);
                reconnect();
            }
            if (!socketToDevice.isConnected())
            {
                log.error("reconnection failed. returning without sending data ... ");
                return;
            }
            socketToDevice.write(data);
            lastSend = System.currentTimeMillis();
        }
        catch (IOException e)
        {
            log.error("send error", e);
            reconnect();
        }
    }

    public void close()
    {
        if (socketToDevice != null)
        {
            try
            {
                log.info(String.format("socket state while closing : isOpen [%b], isBlocking [%b], isConnected [%b]", socketToDevice.isOpen(), socketToDevice.isBlocking(),
                        socketToDevice.isConnected()));
                socketToDevice.close();
                log.debug("socker close() called");
            }
            catch (Exception e)
            {
                log.warn("closing socket", e);
            }
        }
        if (readThread != null)
        {
            log.info("interrupting tcp read thread " + readThread.getName());
            readThread.interrupt();
        }
    }
}
