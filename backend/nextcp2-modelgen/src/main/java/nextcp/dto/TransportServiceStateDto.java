package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class TransportServiceStateDto
{

    public String udn;
    public String transportState;
    public boolean canSkipNext;
    public boolean canSkipPrevious;
    public boolean canRepeat;
    public boolean canShuffle;
    public boolean canSeek;
    public boolean canPause;
    public boolean repeat;
    public boolean shuffle;

    public TransportServiceStateDto()
    {
    }

    public TransportServiceStateDto(String udn, String transportState, boolean canSkipNext, boolean canSkipPrevious, boolean canRepeat, boolean canShuffle, boolean canSeek, boolean canPause, boolean repeat, boolean shuffle)
    {
        this.udn = udn;
        this.transportState = transportState;
        this.canSkipNext = canSkipNext;
        this.canSkipPrevious = canSkipPrevious;
        this.canRepeat = canRepeat;
        this.canShuffle = canShuffle;
        this.canSeek = canSeek;
        this.canPause = canPause;
        this.repeat = repeat;
        this.shuffle = shuffle;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("TransportServiceStateDto [");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("transportState=").append(this.transportState).append(", ");
        sb.append("canSkipNext=").append(this.canSkipNext).append(", ");
        sb.append("canSkipPrevious=").append(this.canSkipPrevious).append(", ");
        sb.append("canRepeat=").append(this.canRepeat).append(", ");
        sb.append("canShuffle=").append(this.canShuffle).append(", ");
        sb.append("canSeek=").append(this.canSeek).append(", ");
        sb.append("canPause=").append(this.canPause).append(", ");
        sb.append("repeat=").append(this.repeat).append(", ");
        sb.append("shuffle=").append(this.shuffle).append(", ");
        sb.append("]");
        return sb.toString();
    }

}