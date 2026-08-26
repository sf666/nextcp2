package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class RadioBrowserStationDto
{

    public String uuid;
    public String name;
    public String url;
    public String favicon;
    public String countryCode;
    public String language;
    public String tags;
    public String codec;
    public Integer bitrate;
    public Integer votes;

    public RadioBrowserStationDto()
    {
    }

    public RadioBrowserStationDto(String uuid, String name, String url, String favicon, String countryCode, String language, String tags, String codec, Integer bitrate, Integer votes)
    {
        this.uuid = uuid;
        this.name = name;
        this.url = url;
        this.favicon = favicon;
        this.countryCode = countryCode;
        this.language = language;
        this.tags = tags;
        this.codec = codec;
        this.bitrate = bitrate;
        this.votes = votes;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RadioBrowserStationDto [");
        sb.append("uuid=").append(this.uuid).append(", ");
        sb.append("name=").append(this.name).append(", ");
        sb.append("url=").append(this.url).append(", ");
        sb.append("favicon=").append(this.favicon).append(", ");
        sb.append("countryCode=").append(this.countryCode).append(", ");
        sb.append("language=").append(this.language).append(", ");
        sb.append("tags=").append(this.tags).append(", ");
        sb.append("codec=").append(this.codec).append(", ");
        sb.append("bitrate=").append(this.bitrate).append(", ");
        sb.append("votes=").append(this.votes).append(", ");
        sb.append("]");
        return sb.toString();
    }

}