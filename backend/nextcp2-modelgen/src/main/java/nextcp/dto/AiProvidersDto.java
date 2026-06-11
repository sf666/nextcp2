package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class AiProvidersDto
{

    public List<String> providers;

    public AiProvidersDto()
    {
    }

    public AiProvidersDto(List<String> providers)
    {
        this.providers = providers;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AiProvidersDto [");
        sb.append("providers=").append(this.providers).append(", ");
        sb.append("]");
        return sb.toString();
    }

}