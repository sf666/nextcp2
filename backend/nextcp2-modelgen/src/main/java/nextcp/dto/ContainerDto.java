package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class ContainerDto
{

    public String id;
    public String parentID;
    public String title;
    public String objectClass;
    public Integer childCount;
    public String createClass;
    public String searchClass;
    public Boolean searchable;
    public String mediaServerUDN;
    public String albumartUri;
    public String artist;
    public Integer rating;
    public String creator;

    public ContainerDto()
    {
    }

    public ContainerDto(String id, String parentID, String title, String objectClass, Integer childCount, String createClass, String searchClass, Boolean searchable, String mediaServerUDN, String albumartUri, String artist, Integer rating, String creator)
    {
        this.id = id;
        this.parentID = parentID;
        this.title = title;
        this.objectClass = objectClass;
        this.childCount = childCount;
        this.createClass = createClass;
        this.searchClass = searchClass;
        this.searchable = searchable;
        this.mediaServerUDN = mediaServerUDN;
        this.albumartUri = albumartUri;
        this.artist = artist;
        this.rating = rating;
        this.creator = creator;
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
        sb.append("childCount=").append(this.childCount).append(", ");
        sb.append("createClass=").append(this.createClass).append(", ");
        sb.append("searchClass=").append(this.searchClass).append(", ");
        sb.append("searchable=").append(this.searchable).append(", ");
        sb.append("mediaServerUDN=").append(this.mediaServerUDN).append(", ");
        sb.append("albumartUri=").append(this.albumartUri).append(", ");
        sb.append("artist=").append(this.artist).append(", ");
        sb.append("rating=").append(this.rating).append(", ");
        sb.append("creator=").append(this.creator).append(", ");
        sb.append("]");
        return sb.toString();
    }

}