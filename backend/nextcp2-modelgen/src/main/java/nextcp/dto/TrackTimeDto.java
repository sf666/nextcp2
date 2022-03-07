package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class TrackTimeDto
{

    public String mediaRendererUdn;
    public Long duration;
    public String durationDisp;
    public Long seconds;
    public String secondsDisp;
    public Long trackCount;
    public Integer percent;
    public Boolean streaming;

    public TrackTimeDto()
    {
    }

    public TrackTimeDto(String mediaRendererUdn, Long duration, String durationDisp, Long seconds, String secondsDisp, Long trackCount, Integer percent, Boolean streaming)
    {
        this.mediaRendererUdn = mediaRendererUdn;
        this.duration = duration;
        this.durationDisp = durationDisp;
        this.seconds = seconds;
        this.secondsDisp = secondsDisp;
        this.trackCount = trackCount;
        this.percent = percent;
        this.streaming = streaming;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("TrackTimeDto [");
        sb.append("mediaRendererUdn=").append(this.mediaRendererUdn).append(", ");
        sb.append("duration=").append(this.duration).append(", ");
        sb.append("durationDisp=").append(this.durationDisp).append(", ");
        sb.append("seconds=").append(this.seconds).append(", ");
        sb.append("secondsDisp=").append(this.secondsDisp).append(", ");
        sb.append("trackCount=").append(this.trackCount).append(", ");
        sb.append("percent=").append(this.percent).append(", ");
        sb.append("streaming=").append(this.streaming).append(", ");
        sb.append("]");
        return sb.toString();
    }

}