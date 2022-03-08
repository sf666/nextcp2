package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class GenericResult
{

    public Boolean success;
    public String message;
    public String headerMessage;

    public GenericResult()
    {
    }

    public GenericResult(Boolean success, String message, String headerMessage)
    {
        this.success = success;
        this.message = message;
        this.headerMessage = headerMessage;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("GenericResult [");
        sb.append("success=").append(this.success).append(", ");
        sb.append("message=").append(this.message).append(", ");
        sb.append("headerMessage=").append(this.headerMessage).append(", ");
        sb.append("]");
        return sb.toString();
    }

}