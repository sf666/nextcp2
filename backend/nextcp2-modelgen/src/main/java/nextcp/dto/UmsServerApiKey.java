package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class UmsServerApiKey
{

    public String serverUuid;
    public String serverApiKey;

    public UmsServerApiKey()
    {
    }

    public UmsServerApiKey(String serverUuid, String serverApiKey)
    {
        this.serverUuid = serverUuid;
        this.serverApiKey = serverApiKey;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("UmsServerApiKey [");
        sb.append("serverUuid=").append(this.serverUuid).append(", ");
        sb.append("serverApiKey=").append(this.serverApiKey).append(", ");
        sb.append("]");
        return sb.toString();
    }

}