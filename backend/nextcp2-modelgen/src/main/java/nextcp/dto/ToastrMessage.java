package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ToastrMessage
{

    public String clientID;
    public String type;
    public String header;
    public String body;

    public ToastrMessage()
    {
    }

    public ToastrMessage(String clientID, String type, String header, String body)
    {
        this.clientID = clientID;
        this.type = type;
        this.header = header;
        this.body = body;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ToastrMessage [");
        sb.append("clientID=").append(this.clientID).append(", ");
        sb.append("type=").append(this.type).append(", ");
        sb.append("header=").append(this.header).append(", ");
        sb.append("body=").append(this.body).append(", ");
        sb.append("]");
        return sb.toString();
    }

}