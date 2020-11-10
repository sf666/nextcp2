package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class ContainerItemDto
{

    public String parentFolderTitle;
    public ContainerDto currentContainer;
    public List<ContainerDto> containerDto;
    public List<ContainerDto> albumDto;
    public List<MusicItemDto> musicItemDto;

    public ContainerItemDto()
    {
    }

    public ContainerItemDto(String parentFolderTitle, ContainerDto currentContainer, List<ContainerDto> containerDto, List<ContainerDto> albumDto, List<MusicItemDto> musicItemDto)
    {
        this.parentFolderTitle = parentFolderTitle;
        this.currentContainer = currentContainer;
        this.containerDto = containerDto;
        this.albumDto = albumDto;
        this.musicItemDto = musicItemDto;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("ContainerItemDto [");
        sb.append("parentFolderTitle=").append(this.parentFolderTitle).append(", ");
        sb.append("currentContainer=").append(this.currentContainer).append(", ");
        sb.append("containerDto=").append(this.containerDto).append(", ");
        sb.append("albumDto=").append(this.albumDto).append(", ");
        sb.append("musicItemDto=").append(this.musicItemDto).append(", ");
        sb.append("]");
        return sb.toString();
    }

}