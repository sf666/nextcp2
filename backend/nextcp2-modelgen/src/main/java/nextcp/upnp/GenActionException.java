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

    /**
     * The full sentence, including the context nextcp wrapped around the failure: which device
     * refused which action, and the UPnP error code. Meant for the log.
     */
    public String description = "";

    /**
     * What the device itself said, without that wrapping - "entry already in Playlist." rather than
     * "device MusicServer rejected action CreateReference : 501 : entry already in Playlist.".
     *
     * This is the only part of a failure that tells a user something they did not already know, so
     * it is what reaches a toast. Empty when the device sent no readable description; then there is
     * nothing more precise than {@link #description} and that is shown instead.
     */
    public String deviceReason = "";

    public GenActionException(int errorCode, String description, Exception e)
    {
        super(description, e);
        this.errorCode = errorCode;
        this.description = description;
    }

    public GenActionException(int errorCode, String description)
    {
        super(description);
        this.errorCode = errorCode;
        this.description = description;
    }

    /**
     * @param description what to log, with device and action named
     * @param deviceReason the device's own words, or empty when it sent none
     */
    public GenActionException(int errorCode, String description, String deviceReason)
    {
        super(description);
        this.errorCode = errorCode;
        this.description = description;
        this.deviceReason = deviceReason == null ? "" : deviceReason;
    }
}
