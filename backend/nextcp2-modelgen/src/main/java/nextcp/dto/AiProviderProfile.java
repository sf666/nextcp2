package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class AiProviderProfile
{

    public String aiProvider;
    @nextcp.handcoded.Nullable
    public String aiApiKey;
    @nextcp.handcoded.Nullable
    public String aiBaseUrl;
    @nextcp.handcoded.Nullable
    public String aiModel;
    @nextcp.handcoded.Nullable
    public String aiToolIds;
    @nextcp.handcoded.Nullable
    public Boolean aiSendTools;

    public AiProviderProfile()
    {
    }

    public AiProviderProfile(String aiProvider, String aiApiKey, String aiBaseUrl, String aiModel, String aiToolIds, Boolean aiSendTools)
    {
        this.aiProvider = aiProvider;
        this.aiApiKey = aiApiKey;
        this.aiBaseUrl = aiBaseUrl;
        this.aiModel = aiModel;
        this.aiToolIds = aiToolIds;
        this.aiSendTools = aiSendTools;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AiProviderProfile [");
        sb.append("aiProvider=").append(this.aiProvider).append(", ");
        sb.append("aiApiKey=").append(this.aiApiKey).append(", ");
        sb.append("aiBaseUrl=").append(this.aiBaseUrl).append(", ");
        sb.append("aiModel=").append(this.aiModel).append(", ");
        sb.append("aiToolIds=").append(this.aiToolIds).append(", ");
        sb.append("aiSendTools=").append(this.aiSendTools).append(", ");
        sb.append("]");
        return sb.toString();
    }

}