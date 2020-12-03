package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class MusicbrainzSupport
{

    public Boolean isActive;
    public String username;
    public String password;

    public MusicbrainzSupport()
    {
    }

    public MusicbrainzSupport(Boolean isActive, String username, String password)
    {
        this.isActive = isActive;
        this.username = username;
        this.password = password;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MusicbrainzSupport [");
        sb.append("isActive=").append(this.isActive).append(", ");
        sb.append("username=").append(this.username).append(", ");
        sb.append("password=").append(this.password).append(", ");
        sb.append("]");
        return sb.toString();
    }

}