package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class AiConfig
{

    public Boolean aiEnabled;
    public String aiProvider;
    public String aiApiKey;
    public String aiModel;

    public AiConfig()
    {
    }

    public AiConfig(Boolean aiEnabled, String aiProvider, String aiApiKey, String aiModel)
    {
        this.aiEnabled = aiEnabled;
        this.aiProvider = aiProvider;
        this.aiApiKey = aiApiKey;
        this.aiModel = aiModel;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AiConfig [");
        sb.append("aiEnabled=").append(this.aiEnabled).append(", ");
        sb.append("aiProvider=").append(this.aiProvider).append(", ");
        sb.append("aiApiKey=").append(this.aiApiKey).append(", ");
        sb.append("aiModel=").append(this.aiModel).append(", ");
        sb.append("]");
        return sb.toString();
    }

}