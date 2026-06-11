package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class AiToolsDto
{

    public List<AiToolDto> tools;

    public AiToolsDto()
    {
    }

    public AiToolsDto(List<AiToolDto> tools)
    {
        this.tools = tools;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AiToolsDto [");
        sb.append("tools=").append(this.tools).append(", ");
        sb.append("]");
        return sb.toString();
    }

}