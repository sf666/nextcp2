package nextcp.upnp;

public class GenActionException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 8350087322266229754L;

    public static final int GENERIC_ERROR = 0;

    public static final int ACTION_ERROR = 5000;
    public static final int ACTION_BAD_CONTROL_URL = 5001;
    public static final int ACTION_FAILED = 5002;

    public int errorCode = 0;
    public String description = "";

    public GenActionException(int errorCode, String description)
    {
        super();
        this.errorCode = errorCode;
        this.description = description;
    }
}
