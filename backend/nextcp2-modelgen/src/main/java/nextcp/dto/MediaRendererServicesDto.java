package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class MediaRendererServicesDto
{

    public String namespace;
    public String serviceName;
    public String version;

    public MediaRendererServicesDto()
    {
    }

    public MediaRendererServicesDto(String namespace, String serviceName, String version)
    {
        this.namespace = namespace;
        this.serviceName = serviceName;
        this.version = version;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaRendererServicesDto [");
        sb.append("namespace=").append(this.namespace).append(", ");
        sb.append("serviceName=").append(this.serviceName).append(", ");
        sb.append("version=").append(this.version).append(", ");
        sb.append("]");
        return sb.toString();
    }

}