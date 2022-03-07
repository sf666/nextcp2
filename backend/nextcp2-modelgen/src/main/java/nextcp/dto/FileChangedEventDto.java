package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class FileChangedEventDto
{

    public String type;
    public String path;

    public FileChangedEventDto()
    {
    }

    public FileChangedEventDto(String type, String path)
    {
        this.type = type;
        this.path = path;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("FileChangedEventDto [");
        sb.append("type=").append(this.type).append(", ");
        sb.append("path=").append(this.path).append(", ");
        sb.append("]");
        return sb.toString();
    }

}