package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class PlayOpenHomeRadioDto
{

    public MediaRendererDto mediaRendererDto;
    public MusicItemDto radioStation;

    public PlayOpenHomeRadioDto()
    {
    }

    public PlayOpenHomeRadioDto(MediaRendererDto mediaRendererDto, MusicItemDto radioStation)
    {
        this.mediaRendererDto = mediaRendererDto;
        this.radioStation = radioStation;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("PlayOpenHomeRadioDto [");
        sb.append("mediaRendererDto=").append(this.mediaRendererDto).append(", ");
        sb.append("radioStation=").append(this.radioStation).append(", ");
        sb.append("]");
        return sb.toString();
    }

}