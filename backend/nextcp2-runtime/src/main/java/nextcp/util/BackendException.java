package nextcp.util;

public class BackendException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 843798251944041866L;

    public static final int GENERIC_ERROR = 0;

    public static final int DIDL_PARSE_ERROR = 2000;

    public static final int SERVICE_UNAVAILABLE_AVTRANSPORT = 3000;

    public static final int DEVICE_DRIVER_UNAVAILABLE = 4000;

    public static final int DEVICE_DRIVER_CONNECTION_STRING_ERROR = 4001;

    public static final int DEVICE_DRIVER_CONNECTION_PORT_RANGE = 4002;

    public static final int DEVICE_DRIVER_CONNECTION_STRING_EMPTY = 4003;

    public static final int PLAYLIST_ALREADY_ADDED = 5000;

    public int errorCode = 0;
    public String description = "";

    public BackendException(int errorCode, String description)
    {
        super();
        this.errorCode = errorCode;
        this.description = description;
    }

    public BackendException(int errorCode, String description, Exception e)
    {
        super(e);
        this.errorCode = errorCode;
        this.description = description;
    }
}
