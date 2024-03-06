package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ServerDeleteObjectRequest
{

    public String serverUdn;
    public String objectId;

    public ServerDeleteObjectRequest()
    {
    }

    public ServerDeleteObjectRequest(String serverUdn, String objectId)
    {
        this.serverUdn = serverUdn;
        this.objectId = objectId;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ServerDeleteObjectRequest [");
        sb.append("serverUdn=").append(this.serverUdn).append(", ");
        sb.append("objectId=").append(this.objectId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}