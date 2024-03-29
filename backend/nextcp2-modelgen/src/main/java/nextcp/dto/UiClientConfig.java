package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class UiClientConfig
{

    public String uuid;
    public String clientName;
    public MediaServerDto defaultMediaServer;
    public MediaRendererDto defaultMediaRenderer;

    public UiClientConfig()
    {
    }

    public UiClientConfig(String uuid, String clientName, MediaServerDto defaultMediaServer, MediaRendererDto defaultMediaRenderer)
    {
        this.uuid = uuid;
        this.clientName = clientName;
        this.defaultMediaServer = defaultMediaServer;
        this.defaultMediaRenderer = defaultMediaRenderer;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("UiClientConfig [");
        sb.append("uuid=").append(this.uuid).append(", ");
        sb.append("clientName=").append(this.clientName).append(", ");
        sb.append("defaultMediaServer=").append(this.defaultMediaServer).append(", ");
        sb.append("defaultMediaRenderer=").append(this.defaultMediaRenderer).append(", ");
        sb.append("]");
        return sb.toString();
    }

}