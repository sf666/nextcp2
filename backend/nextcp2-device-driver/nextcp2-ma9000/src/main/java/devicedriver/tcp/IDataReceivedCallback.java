package devicedriver.tcp;

import java.nio.ByteBuffer;

public interface IDataReceivedCallback
{
    public void dataReceived(ByteBuffer buffer);
}
