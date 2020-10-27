package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class PlayRequestDto
{

    public MediaRendererDto mediaRendererDto;
    public String streamUrl;
    public String streamMetadata;

    public PlayRequestDto()
    {
    }

    public PlayRequestDto(MediaRendererDto mediaRendererDto, String streamUrl, String streamMetadata)
    {
        this.mediaRendererDto = mediaRendererDto;
        this.streamUrl = streamUrl;
        this.streamMetadata = streamMetadata;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("PlayRequestDto [");
        sb.append("mediaRendererDto=").append(this.mediaRendererDto).append(", ");
        sb.append("streamUrl=").append(this.streamUrl).append(", ");
        sb.append("streamMetadata=").append(this.streamMetadata).append(", ");
        sb.append("]");
        return sb.toString();
    }

}