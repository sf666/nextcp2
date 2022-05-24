package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class SpotifyConfigDto
{

    public boolean accountConnected;
    public String clientId;
    public String refreshToken;
    public String redirectUrl;

    public SpotifyConfigDto()
    {
    }

    public SpotifyConfigDto(boolean accountConnected, String clientId, String refreshToken, String redirectUrl)
    {
        this.accountConnected = accountConnected;
        this.clientId = clientId;
        this.refreshToken = refreshToken;
        this.redirectUrl = redirectUrl;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SpotifyConfigDto [");
        sb.append("accountConnected=").append(this.accountConnected).append(", ");
        sb.append("clientId=").append(this.clientId).append(", ");
        sb.append("refreshToken=").append(this.refreshToken).append(", ");
        sb.append("redirectUrl=").append(this.redirectUrl).append(", ");
        sb.append("]");
        return sb.toString();
    }

}