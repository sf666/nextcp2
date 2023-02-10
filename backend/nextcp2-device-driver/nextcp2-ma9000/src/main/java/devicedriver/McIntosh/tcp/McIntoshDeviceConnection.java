package devicedriver.McIntosh.tcp;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import devicedriver.McIntosh.control.BaseCommandStructure;
import devicedriver.tcp.TcpDeviceConnection;
import devicedriver.util.CharBufferUtil;

public class McIntoshDeviceConnection extends TcpDeviceConnection
{
    private static final Logger log = LoggerFactory.getLogger(McIntoshDeviceConnection.class.getName());

    private ByteBuffer sendBuffer = ByteBuffer.allocateDirect(256);

    private CharBuffer reply = null;

    private IMcIntoshDeviceChanged subscriptionCallback = null;

    private final Pattern pattern = Pattern.compile("\\((.+?)\\)");

    private ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 10, 5, TimeUnit.SECONDS, queue);

    public McIntoshDeviceConnection()
    {
    }

    /**
     * MA9000 Brodcast Meldungen kommen hier an ...
     * 
     * @param subscriptionCallback
     */
    public McIntoshDeviceConnection(IMcIntoshDeviceChanged subscriptionCallback)
    {
        this.subscriptionCallback = subscriptionCallback;
    }

    @Override
    protected void dataReceived(ByteBuffer receivedBuffer)
    {
        CharsetDecoder cd = StandardCharsets.UTF_8.newDecoder();
        try
        {
            CharBuffer lastResponse = cd.decode(receivedBuffer);
            log.info("Data received : " + lastResponse.toString());
            receivedBuffer.clear();
            String[] lines = CharBufferUtil.split(lastResponse, "\r\n");
            for (String line : lines)
            {
                final Matcher matcher = pattern.matcher(line);
                while (matcher.find())
                {
                    asyncStateChange(matcher.group(1));
                }
            }

        }
        catch (CharacterCodingException e)
        {
            log.error("Should not happen ...", e);
        }
    }

    /**
     * <pre>
     * Device INIT looks like this:
     * 
     * (MA9000)(Serial Number: AFK1456)(FW Version: 1.04)(PWR 1)(VOL 56)(MUT 0)(OP1 1)(OP2 1)(INP 15)(STA 1)(TBA -2)(TIN 0)(TEQ 0)(TPR 3)(TPC 1)(TMO 0)(TML 1)(TDB 3)(THH 1)(HPS 2)
     * 
     * </pre>
     * 
     * @param aCommand
     */
    private void asyncStateChange(String aCommand)
    {
        threadPool.execute(new Runnable()
        {
            public void run()
            {
                log.info(String.format("status received from device : %s", aCommand));
                if (aCommand.startsWith("PWR"))
                {
                    if (aCommand.endsWith("0"))
                    {
                        subscriptionCallback.standbyStateChanged(true);
                    }
                    else
                    {
                        subscriptionCallback.standbyStateChanged(false);
                    }
                }
                else if (aCommand.startsWith("VOL"))
                {
                    int vol = Integer.parseInt(aCommand.split(" ")[1]);
                    subscriptionCallback.volumeStatusChanged(vol);
                }
                else if (aCommand.startsWith("INP"))
                {
                    String inp = aCommand.split(" ")[1];
                    subscriptionCallback.inputChanged(inp);
                }
                else if (aCommand.startsWith("FW "))
                {
                    // subscriptionCallback.firmwareVersionChanged(aCommand.substring(11));
                }
            }
        });
    }

    synchronized public void send(BaseCommandStructure request, Object... param)
    {
        sendBuffer.clear();
        if (reply != null)
        {
            reply.clear();
        }
        byte[] deviceRequest = request.getCommandAsByteArray(param);
        sendBuffer.put(deviceRequest);
        sendBuffer.flip();
        log.info("sending message to device : " + new String(deviceRequest));
        sendData(sendBuffer);
    }
}
