package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class PlaylistState
{

    public String udn;
    public Long TracksMax;
    public Boolean Shuffle;
    public Boolean Repeat;
    public String TransportState;
    public String ProtocolInfo;
    public Long Id;

    public PlaylistState()
    {
    }

    public PlaylistState(String udn, Long TracksMax, Boolean Shuffle, Boolean Repeat, String TransportState, String ProtocolInfo, Long Id)
    {
        this.udn = udn;
        this.TracksMax = TracksMax;
        this.Shuffle = Shuffle;
        this.Repeat = Repeat;
        this.TransportState = TransportState;
        this.ProtocolInfo = ProtocolInfo;
        this.Id = Id;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("PlaylistState [");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("TracksMax=").append(this.TracksMax).append(", ");
        sb.append("Shuffle=").append(this.Shuffle).append(", ");
        sb.append("Repeat=").append(this.Repeat).append(", ");
        sb.append("TransportState=").append(this.TransportState).append(", ");
        sb.append("ProtocolInfo=").append(this.ProtocolInfo).append(", ");
        sb.append("Id=").append(this.Id).append(", ");
        sb.append("]");
        return sb.toString();
    }

}