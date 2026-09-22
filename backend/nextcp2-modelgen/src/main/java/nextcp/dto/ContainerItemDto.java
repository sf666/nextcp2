package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class ContainerItemDto
{

    public String parentFolderTitle;
    public ContainerDto currentContainer;
    public List<ContainerDto> containerDto;
    public List<ContainerDto> albumDto;
    public List<ItemDto> items;
    public List<ContainerDto> minimServerSupportTags;
    public MusicAlbumIds allTracksSameAlbumIds;
    public Long totalMatches;
    public String resourceId;

    public ContainerItemDto()
    {
    }

    public ContainerItemDto(String parentFolderTitle, ContainerDto currentContainer, List<ContainerDto> containerDto, List<ContainerDto> albumDto, List<ItemDto> items, List<ContainerDto> minimServerSupportTags, MusicAlbumIds allTracksSameAlbumIds, Long totalMatches, String resourceId)
    {
        this.parentFolderTitle = parentFolderTitle;
        this.currentContainer = currentContainer;
        this.containerDto = containerDto;
        this.albumDto = albumDto;
        this.items = items;
        this.minimServerSupportTags = minimServerSupportTags;
        this.allTracksSameAlbumIds = allTracksSameAlbumIds;
        this.totalMatches = totalMatches;
        this.resourceId = resourceId;
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
        sb.append("items=").append(this.items).append(", ");
        sb.append("minimServerSupportTags=").append(this.minimServerSupportTags).append(", ");
        sb.append("allTracksSameAlbumIds=").append(this.allTracksSameAlbumIds).append(", ");
        sb.append("totalMatches=").append(this.totalMatches).append(", ");
        sb.append("resourceId=").append(this.resourceId).append(", ");
        sb.append("]");
        return sb.toString();
    }

}