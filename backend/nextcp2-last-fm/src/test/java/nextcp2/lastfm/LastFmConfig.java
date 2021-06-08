package nextcp2.lastfm;

/**
 * LastFM configuration
 */
public class LastFmConfig implements ILastFmConfig
{

    public LastFmConfig()
    {
    }

    
    
    @Override
    public String getApiKey()
    {
        return "a9292ddac1cef440892f454e95c78300";
    }

    @Override
    public String getSharedSecret()
    {
        return "8c85da4a87193c501aa6ebd016667715";
    }

}
