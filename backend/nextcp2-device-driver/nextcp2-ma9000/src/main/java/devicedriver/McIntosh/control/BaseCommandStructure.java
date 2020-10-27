package devicedriver.McIntosh.control;

import java.io.UnsupportedEncodingException;
import java.nio.CharBuffer;

import devicedriver.util.CharBufferUtil;

public abstract class BaseCommandStructure
{

    private String request = null;
    private int paramCount = 0;

    public BaseCommandStructure(String request)
    {
        this.request = request;
    }

    public BaseCommandStructure(String request, int paramCount)
    {
        this(request);
        this.paramCount = paramCount;
    }

    public String getCommand(Object... param)
    {
        if (param.length != paramCount)
        {
            throw new RuntimeException("Parameter expected : " + paramCount + ". Got : " + param.length);
        }
        if (paramCount == 0)
        {
            String command = String.format("(%s)", request);
            return command;
        }
        else
        {
            StringBuilder sb = new StringBuilder(20);
            for (int i = 0; i < param.length; i++)
            {
                sb.append(param[0].toString());
                if (i < (param.length - 1))
                {
                    sb.append(' ');
                }
            }
            String command = String.format("(%s %s)", request, sb.toString());
            return command;
        }
    }

    public byte[] getCommandAsByteArray(Object... param)
    {
        try
        {
            return getCommand(param).getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException(e);
        }
    }

    abstract public boolean hasReturnValue();

    public String getReturnValue(CharBuffer reply)
    {
        if (CharBufferUtil.startsWith(request, reply))
        {
            return getParameterValue(reply).toString();
        }
        throw new RuntimeException("Wrong reply to request " + request + ". Reply is : '" + reply.toString() + "'");
    }

    /**
     * resets position !
     * 
     * @param completeReply
     * @return
     */
    private CharBuffer getParameterValue(CharBuffer completeReply)
    {
        int start = CharBufferUtil.getFirstIndexOf('(', completeReply);
        int end = CharBufferUtil.getFirstIndexOf(')', completeReply);
        completeReply.position(0);
        return completeReply.subSequence(start + 1, end);
    }

    protected String getRequest()
    {
        return request;
    }

}
