package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class AudioFormat
{

    public Long nrAudioChannels;
    public Long sampleFrequency;
    public Long bitsPerSample;
    public Long bitrate;
    public String filetype;
    public String durationDisp;
    public Long durationInSeconds;
    public String contentFormat;
    public Long size;
    public Boolean isStreaming;

    public AudioFormat()
    {
    }

    public AudioFormat(Long nrAudioChannels, Long sampleFrequency, Long bitsPerSample, Long bitrate, String filetype, String durationDisp, Long durationInSeconds, String contentFormat, Long size, Boolean isStreaming)
    {
        this.nrAudioChannels = nrAudioChannels;
        this.sampleFrequency = sampleFrequency;
        this.bitsPerSample = bitsPerSample;
        this.bitrate = bitrate;
        this.filetype = filetype;
        this.durationDisp = durationDisp;
        this.durationInSeconds = durationInSeconds;
        this.contentFormat = contentFormat;
        this.size = size;
        this.isStreaming = isStreaming;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AudioFormat [");
        sb.append("nrAudioChannels=").append(this.nrAudioChannels).append(", ");
        sb.append("sampleFrequency=").append(this.sampleFrequency).append(", ");
        sb.append("bitsPerSample=").append(this.bitsPerSample).append(", ");
        sb.append("bitrate=").append(this.bitrate).append(", ");
        sb.append("filetype=").append(this.filetype).append(", ");
        sb.append("durationDisp=").append(this.durationDisp).append(", ");
        sb.append("durationInSeconds=").append(this.durationInSeconds).append(", ");
        sb.append("contentFormat=").append(this.contentFormat).append(", ");
        sb.append("size=").append(this.size).append(", ");
        sb.append("isStreaming=").append(this.isStreaming).append(", ");
        sb.append("]");
        return sb.toString();
    }

}