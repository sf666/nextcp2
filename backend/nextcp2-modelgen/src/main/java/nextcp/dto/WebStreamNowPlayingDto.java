package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class WebStreamNowPlayingDto
{

    public String objectID;
    public String streamTitle;
    public String artist;
    public String title;
    public String artUrl;

    public WebStreamNowPlayingDto()
    {
    }

    public WebStreamNowPlayingDto(String objectID, String streamTitle, String artist, String title, String artUrl)
    {
        this.objectID = objectID;
        this.streamTitle = streamTitle;
        this.artist = artist;
        this.title = title;
        this.artUrl = artUrl;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("WebStreamNowPlayingDto [");
        sb.append("objectID=").append(this.objectID).append(", ");
        sb.append("streamTitle=").append(this.streamTitle).append(", ");
        sb.append("artist=").append(this.artist).append(", ");
        sb.append("title=").append(this.title).append(", ");
        sb.append("artUrl=").append(this.artUrl).append(", ");
        sb.append("]");
        return sb.toString();
    }

}