package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class RadioBrowserSearchRequest
{

    public String serverUdn;
    public String name;
    public String countryCode;
    public String language;
    public String tag;
    public Integer offset;
    public Integer limit;

    public RadioBrowserSearchRequest()
    {
    }

    public RadioBrowserSearchRequest(String serverUdn, String name, String countryCode, String language, String tag, Integer offset, Integer limit)
    {
        this.serverUdn = serverUdn;
        this.name = name;
        this.countryCode = countryCode;
        this.language = language;
        this.tag = tag;
        this.offset = offset;
        this.limit = limit;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RadioBrowserSearchRequest [");
        sb.append("serverUdn=").append(this.serverUdn).append(", ");
        sb.append("name=").append(this.name).append(", ");
        sb.append("countryCode=").append(this.countryCode).append(", ");
        sb.append("language=").append(this.language).append(", ");
        sb.append("tag=").append(this.tag).append(", ");
        sb.append("offset=").append(this.offset).append(", ");
        sb.append("limit=").append(this.limit).append(", ");
        sb.append("]");
        return sb.toString();
    }

}