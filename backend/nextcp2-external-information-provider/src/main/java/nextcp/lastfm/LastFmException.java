package nextcp.lastfm;

/**
 * <pre>
   
1 : This error does not exist
2 : Invalid service -This service does not exist
3 : Invalid Method - No method with that name in this package
4 : Authentication Failed - You do not have permissions to access the service
5 : Invalid format - This service doesn't exist in that format
6 : Invalid parameters - Your request is missing a required parameter
7 : Invalid resource specified
8 : Operation failed - Most likely the backend service failed. Please try again.
9 : Invalid session key - Please re-authenticate
10 : Invalid API key - You must be granted a valid key by last.fm
11 : Service Offline - This service is temporarily offline. Try again later.
12 : Subscribers Only - This station is only available to paid last.fm subscribers
13 : Invalid method signature supplied
14 : Unauthorized Token - This token has not been authorized
15 : This item is not available for streaming.
16 : The service is temporarily unavailable, please try again.
17 : Login: User requires to be logged in
18 : Trial Expired - This user has no free radio plays left. Subscription required.
19 : This error does not exist
20 : Not Enough Content - There is not enough content to play this station
21 : Not Enough Members - This group does not have enough members for radio
22 : Not Enough Fans - This artist does not have enough fans for radio
23 : Not Enough Neighbours - There are not enough neighbours for radio
24 : No Peak Radio - This user is not allowed to listen to radio during peak usage
25 : Radio Not Found - Radio station not found
26 : API Key Suspended - This application is not allowed to make requests to the web services
27 : Deprecated - This type of request is no longer supported
29 : Rate Limit Exceded - Your IP has made too many requests in a short period, exceeding our API guidelines
 * 
 * </pre>
 * 
 * Error code of 9999 is an internal nextCP/2 exception.
 */
public class LastFmException extends RuntimeException
{
    public int errorCode;

    /**
     * 
     */
    private static final long serialVersionUID = 8471222056296196283L;

    public LastFmException(int errorCode, String errorMessage)
    {
        super(errorMessage);
        this.errorCode = errorCode;
    }

}
