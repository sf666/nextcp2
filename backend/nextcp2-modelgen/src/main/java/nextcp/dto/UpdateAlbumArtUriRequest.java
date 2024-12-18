package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class UpdateAlbumArtUriRequest
{

    public String previousAlbumArtUri;
    public String newAlbumArtUri;
    public String mediaServerDevice;
    public MusicItemIdDto musicItemIdDto;

    public UpdateAlbumArtUriRequest()
    {
    }

    public UpdateAlbumArtUriRequest(String previousAlbumArtUri, String newAlbumArtUri, String mediaServerDevice, MusicItemIdDto musicItemIdDto)
    {
        this.previousAlbumArtUri = previousAlbumArtUri;
        this.newAlbumArtUri = newAlbumArtUri;
        this.mediaServerDevice = mediaServerDevice;
        this.musicItemIdDto = musicItemIdDto;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("UpdateAlbumArtUriRequest [");
        sb.append("previousAlbumArtUri=").append(this.previousAlbumArtUri).append(", ");
        sb.append("newAlbumArtUri=").append(this.newAlbumArtUri).append(", ");
        sb.append("mediaServerDevice=").append(this.mediaServerDevice).append(", ");
        sb.append("musicItemIdDto=").append(this.musicItemIdDto).append(", ");
        sb.append("]");
        return sb.toString();
    }

}