package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class TrackInfoDto
{

    public String mediaRendererUdn;
    public Long detailsCount;
    public Long metatextCount;
    public String metadata;
    public Long trackCount;
    public String uri;
    public String codecName;
    public String metatext;
    public MusicItemDto currentTrack;
    public String duration;
    public Long sampleRate;
    public Boolean lossless;
    public Long bitDepth;
    public Long bitrate;

    public TrackInfoDto()
    {
    }

    public TrackInfoDto(String mediaRendererUdn, Long detailsCount, Long metatextCount, String metadata, Long trackCount, String uri, String codecName, String metatext, MusicItemDto currentTrack, String duration, Long sampleRate, Boolean lossless, Long bitDepth, Long bitrate)
    {
        this.mediaRendererUdn = mediaRendererUdn;
        this.detailsCount = detailsCount;
        this.metatextCount = metatextCount;
        this.metadata = metadata;
        this.trackCount = trackCount;
        this.uri = uri;
        this.codecName = codecName;
        this.metatext = metatext;
        this.currentTrack = currentTrack;
        this.duration = duration;
        this.sampleRate = sampleRate;
        this.lossless = lossless;
        this.bitDepth = bitDepth;
        this.bitrate = bitrate;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("TrackInfoDto [");
        sb.append("mediaRendererUdn=").append(this.mediaRendererUdn).append(", ");
        sb.append("detailsCount=").append(this.detailsCount).append(", ");
        sb.append("metatextCount=").append(this.metatextCount).append(", ");
        sb.append("metadata=").append(this.metadata).append(", ");
        sb.append("trackCount=").append(this.trackCount).append(", ");
        sb.append("uri=").append(this.uri).append(", ");
        sb.append("codecName=").append(this.codecName).append(", ");
        sb.append("metatext=").append(this.metatext).append(", ");
        sb.append("currentTrack=").append(this.currentTrack).append(", ");
        sb.append("duration=").append(this.duration).append(", ");
        sb.append("sampleRate=").append(this.sampleRate).append(", ");
        sb.append("lossless=").append(this.lossless).append(", ");
        sb.append("bitDepth=").append(this.bitDepth).append(", ");
        sb.append("bitrate=").append(this.bitrate).append(", ");
        sb.append("]");
        return sb.toString();
    }

}