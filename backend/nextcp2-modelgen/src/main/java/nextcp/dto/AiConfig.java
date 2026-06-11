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
    public Boolean aiSendTools;
    public Boolean aiConversationMemory;
    public String aiProvider;
    public String aiApiKey;
    public String aiModel;
    @nextcp.handcoded.Nullable
    public String aiBaseUrl;
    @nextcp.handcoded.Nullable
    public String selectedRendererUdn;
    @nextcp.handcoded.Nullable
    public String selectedServerUdn;

    public AiConfig()
    {
    }

    public AiConfig(Boolean aiEnabled, Boolean aiSendTools, Boolean aiConversationMemory, String aiProvider, String aiApiKey, String aiModel, String aiBaseUrl, String selectedRendererUdn, String selectedServerUdn)
    {
        this.aiEnabled = aiEnabled;
        this.aiSendTools = aiSendTools;
        this.aiConversationMemory = aiConversationMemory;
        this.aiProvider = aiProvider;
        this.aiApiKey = aiApiKey;
        this.aiModel = aiModel;
        this.aiBaseUrl = aiBaseUrl;
        this.selectedRendererUdn = selectedRendererUdn;
        this.selectedServerUdn = selectedServerUdn;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AiConfig [");
        sb.append("aiEnabled=").append(this.aiEnabled).append(", ");
        sb.append("aiSendTools=").append(this.aiSendTools).append(", ");
        sb.append("aiConversationMemory=").append(this.aiConversationMemory).append(", ");
        sb.append("aiProvider=").append(this.aiProvider).append(", ");
        sb.append("aiApiKey=").append(this.aiApiKey).append(", ");
        sb.append("aiModel=").append(this.aiModel).append(", ");
        sb.append("aiBaseUrl=").append(this.aiBaseUrl).append(", ");
        sb.append("selectedRendererUdn=").append(this.selectedRendererUdn).append(", ");
        sb.append("selectedServerUdn=").append(this.selectedServerUdn).append(", ");
        sb.append("]");
        return sb.toString();
    }

}