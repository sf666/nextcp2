package nextcp.spotify;

public interface ISpotifyConfig
{
    public String getClientId();

    public String getSpotifyRefreshToken();

    public void setSpotifyRefreshToken(String currentToken);

    /**
     * 
     * @param authNeeded
     *            true if user authorization is needed
     */
    public void setUserAuthorizationNeeded(boolean authNeeded);
}
