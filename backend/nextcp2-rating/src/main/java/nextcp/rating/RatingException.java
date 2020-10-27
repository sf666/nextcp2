package nextcp.rating;

public class RatingException extends RuntimeException
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final int GENERIC_ERROR = 0;
    public static final int ILLEGAL_RATING_VALUE = 1000;
    public static final int EMPTY_ACOUSTIC_ID = 1001;

    public static final int DATABASE_ACCESS_ERROR = 2000;

    public int errorCode = 0;
    public String description = "";

    public RatingException(int errorCode, String description)
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
