package nextcp.indexer;

public class IndexerException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int GENERIC_ERROR = 0;
    public static final int ILLEGAL_RATING_VALUE = 1000;
    public static final int EMPTY_ACOUSTIC_ID = 1001;

    public static final int DATABASE_ACCESS_ERROR = 2000;

    public static final int INVALID_PLAYLIST_NAME = 4000;

    public static final int PLAYLIST_FILE_EXISTS = 4001;
    public static final int PLAYLIST_PARAM_NOT_INITIALIZED = 4002;
    public static final int PLAYLIST_FILE_NOT_IN_PLAYLIST = 4003;
    public static final int PLAYLIST_NOT_EXISTS = 4004;
    public static final int PLAYLIST_DIRECTORY_NOT_EXISTS = 4005;

    public int errorCode = 0;
    public String description = "";

    public IndexerException(int errorCode, String description)
    {
        super();
        this.errorCode = errorCode;
        this.description = description;
    }

    @Override
    public String toString()
    {
        return String.format("Error code : %d. %s", errorCode, description);
    }
}
