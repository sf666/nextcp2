package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class RadioBrowserFilterRequest
{

    public String serverUdn;
    public String kind;
    public String search;

    public RadioBrowserFilterRequest()
    {
    }

    public RadioBrowserFilterRequest(String serverUdn, String kind, String search)
    {
        this.serverUdn = serverUdn;
        this.kind = kind;
        this.search = search;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RadioBrowserFilterRequest [");
        sb.append("serverUdn=").append(this.serverUdn).append(", ");
        sb.append("kind=").append(this.kind).append(", ");
        sb.append("search=").append(this.search).append(", ");
        sb.append("]");
        return sb.toString();
    }

}