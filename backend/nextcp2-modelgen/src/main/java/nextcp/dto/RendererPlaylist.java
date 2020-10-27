package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class RendererPlaylist
{

    public String udn;
    public List<MusicItemDto> musicItemDto;

    public RendererPlaylist()
    {
    }

    public RendererPlaylist(String udn, List<MusicItemDto> musicItemDto)
    {
        this.udn = udn;
        this.musicItemDto = musicItemDto;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RendererPlaylist [");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("musicItemDto=").append(this.musicItemDto).append(", ");
        sb.append("]");
        return sb.toString();
    }

}