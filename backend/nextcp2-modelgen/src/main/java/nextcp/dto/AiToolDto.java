package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class AiToolDto
{

    public String id;
    @nextcp.handcoded.Nullable
    public String name;

    public AiToolDto()
    {
    }

    public AiToolDto(String id, String name)
    {
        this.id = id;
        this.name = name;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AiToolDto [");
        sb.append("id=").append(this.id).append(", ");
        sb.append("name=").append(this.name).append(", ");
        sb.append("]");
        return sb.toString();
    }

}