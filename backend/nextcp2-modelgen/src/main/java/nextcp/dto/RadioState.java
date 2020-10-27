package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class RadioState
{

    public Long ChannelsMax;
    public Long Id;
    public String Metadata;
    public String ProtocolInfo;
    public String TransportState;
    public String Uri;

    public RadioState()
    {
    }

    public RadioState(Long ChannelsMax, Long Id, String Metadata, String ProtocolInfo, String TransportState, String Uri)
    {
        this.ChannelsMax = ChannelsMax;
        this.Id = Id;
        this.Metadata = Metadata;
        this.ProtocolInfo = ProtocolInfo;
        this.TransportState = TransportState;
        this.Uri = Uri;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RadioState [");
        sb.append("ChannelsMax=").append(this.ChannelsMax).append(", ");
        sb.append("Id=").append(this.Id).append(", ");
        sb.append("Metadata=").append(this.Metadata).append(", ");
        sb.append("ProtocolInfo=").append(this.ProtocolInfo).append(", ");
        sb.append("TransportState=").append(this.TransportState).append(", ");
        sb.append("Uri=").append(this.Uri).append(", ");
        sb.append("]");
        return sb.toString();
    }

}