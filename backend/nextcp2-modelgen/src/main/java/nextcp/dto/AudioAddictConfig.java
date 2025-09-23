package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class AudioAddictConfig
{

    public Boolean preferEuropeanServer;
    public String user;
    public String pass;

    public AudioAddictConfig()
    {
    }

    public AudioAddictConfig(Boolean preferEuropeanServer, String user, String pass)
    {
        this.preferEuropeanServer = preferEuropeanServer;
        this.user = user;
        this.pass = pass;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("AudioAddictConfig [");
        sb.append("preferEuropeanServer=").append(this.preferEuropeanServer).append(", ");
        sb.append("user=").append(this.user).append(", ");
        sb.append("pass=").append(this.pass).append(", ");
        sb.append("]");
        return sb.toString();
    }

}