package nextcp.lastfm;

import org.springframework.stereotype.Service;

/**
 * LastFM configuration
 */
@Service
public class LastFmConfig implements ILastFmConfig
{

    public LastFmConfig()
    {
    }

    
    
    @Override
    public String getSharedSecret()
    {
        return "8c85da4a87193c501aa6ebd016667715";
    }

    @Override
    public String getApiKey()
    {
        return "a9292ddac1cef440892f454e95c78300";
    }

}
