package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ContainerIdDto
{

    public String id;
    public String title;

    public ContainerIdDto()
    {
    }

    public ContainerIdDto(String id, String title)
    {
        this.id = id;
        this.title = title;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ContainerIdDto [");
        sb.append("id=").append(this.id).append(", ");
        sb.append("title=").append(this.title).append(", ");
        sb.append("]");
        return sb.toString();
    }

}