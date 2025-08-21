package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class MediaRendererDto
{

    public String img;
    public String udn;
    public String friendlyName;
    public List<MediaRendererServicesDto> services;
    public InputSourceDto currentSource;
    public List<InputSourceDto> allSources;

    public MediaRendererDto()
    {
    }

    public MediaRendererDto(String img, String udn, String friendlyName, List<MediaRendererServicesDto> services, InputSourceDto currentSource, List<InputSourceDto> allSources)
    {
        this.img = img;
        this.udn = udn;
        this.friendlyName = friendlyName;
        this.services = services;
        this.currentSource = currentSource;
        this.allSources = allSources;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaRendererDto [");
        sb.append("img=").append(this.img).append(", ");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("friendlyName=").append(this.friendlyName).append(", ");
        sb.append("services=").append(this.services).append(", ");
        sb.append("currentSource=").append(this.currentSource).append(", ");
        sb.append("allSources=").append(this.allSources).append(", ");
        sb.append("]");
        return sb.toString();
    }

}