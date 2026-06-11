package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class AiModelsDto
{

    @nextcp.handcoded.Nullable
    public String provider;
    public List<String> models;

    public AiModelsDto()
    {
    }

    public AiModelsDto(String provider, List<String> models)
    {
        this.provider = provider;
        this.models = models;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AiModelsDto [");
        sb.append("provider=").append(this.provider).append(", ");
        sb.append("models=").append(this.models).append(", ");
        sb.append("]");
        return sb.toString();
    }

}