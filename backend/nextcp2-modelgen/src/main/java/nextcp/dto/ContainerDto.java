package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ContainerDto
{

    public String id;
    public String parentID;
    public String title;
    public String objectClass;
    public String conductor;
    public String composer;
    public Integer childCount;
    public String createClass;
    public String searchClass;
    public Boolean searchable;
    public String mediaServerUDN;
    public String albumartUri;
    public String artist;
    public Integer rating;
    public String creator;
    public String media_date;
    public String genre;

    public ContainerDto()
    {
    }

    public ContainerDto(String id, String parentID, String title, String objectClass, String conductor, String composer, Integer childCount, String createClass, String searchClass, Boolean searchable, String mediaServerUDN, String albumartUri, String artist, Integer rating, String creator, String media_date, String genre)
    {
        this.id = id;
        this.parentID = parentID;
        this.title = title;
        this.objectClass = objectClass;
        this.conductor = conductor;
        this.composer = composer;
        this.childCount = childCount;
        this.createClass = createClass;
        this.searchClass = searchClass;
        this.searchable = searchable;
        this.mediaServerUDN = mediaServerUDN;
        this.albumartUri = albumartUri;
        this.artist = artist;
        this.rating = rating;
        this.creator = creator;
        this.media_date = media_date;
        this.genre = genre;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ContainerDto [");
        sb.append("id=").append(this.id).append(", ");
        sb.append("parentID=").append(this.parentID).append(", ");
        sb.append("title=").append(this.title).append(", ");
        sb.append("objectClass=").append(this.objectClass).append(", ");
        sb.append("conductor=").append(this.conductor).append(", ");
        sb.append("composer=").append(this.composer).append(", ");
        sb.append("childCount=").append(this.childCount).append(", ");
        sb.append("createClass=").append(this.createClass).append(", ");
        sb.append("searchClass=").append(this.searchClass).append(", ");
        sb.append("searchable=").append(this.searchable).append(", ");
        sb.append("mediaServerUDN=").append(this.mediaServerUDN).append(", ");
        sb.append("albumartUri=").append(this.albumartUri).append(", ");
        sb.append("artist=").append(this.artist).append(", ");
        sb.append("rating=").append(this.rating).append(", ");
        sb.append("creator=").append(this.creator).append(", ");
        sb.append("media_date=").append(this.media_date).append(", ");
        sb.append("genre=").append(this.genre).append(", ");
        sb.append("]");
        return sb.toString();
    }

}