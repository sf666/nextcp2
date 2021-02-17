package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class SearchResultDto
{

    public List<MusicItemDto> musicItems;
    public List<ContainerDto> albumItems;
    public List<ContainerDto> artistItems;
    public List<ContainerDto> playlistItems;

    public SearchResultDto()
    {
    }

    public SearchResultDto(List<MusicItemDto> musicItems, List<ContainerDto> albumItems, List<ContainerDto> artistItems, List<ContainerDto> playlistItems)
    {
        this.musicItems = musicItems;
        this.albumItems = albumItems;
        this.artistItems = artistItems;
        this.playlistItems = playlistItems;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SearchResultDto [");
        sb.append("musicItems=").append(this.musicItems).append(", ");
        sb.append("albumItems=").append(this.albumItems).append(", ");
        sb.append("artistItems=").append(this.artistItems).append(", ");
        sb.append("playlistItems=").append(this.playlistItems).append(", ");
        sb.append("]");
        return sb.toString();
    }

}