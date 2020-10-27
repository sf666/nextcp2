package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class PlayRadioDto
{

    public MediaRendererDto mediaRendererDto;
    public RadioStation radioStation;

    public PlayRadioDto()
    {
    }

    public PlayRadioDto(MediaRendererDto mediaRendererDto, RadioStation radioStation)
    {
        this.mediaRendererDto = mediaRendererDto;
        this.radioStation = radioStation;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("PlayRadioDto [");
        sb.append("mediaRendererDto=").append(this.mediaRendererDto).append(", ");
        sb.append("radioStation=").append(this.radioStation).append(", ");
        sb.append("]");
        return sb.toString();
    }

}