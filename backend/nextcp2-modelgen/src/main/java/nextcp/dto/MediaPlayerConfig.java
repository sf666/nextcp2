package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class MediaPlayerConfig
{

    public String workdir;
    public String script;
    public String playType;

    public MediaPlayerConfig()
    {
    }

    public MediaPlayerConfig(String workdir, String script, String playType)
    {
        this.workdir = workdir;
        this.script = script;
        this.playType = playType;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("MediaPlayerConfig [");
        sb.append("workdir=").append(this.workdir).append(", ");
        sb.append("script=").append(this.script).append(", ");
        sb.append("playType=").append(this.playType).append(", ");
        sb.append("]");
        return sb.toString();
    }

}