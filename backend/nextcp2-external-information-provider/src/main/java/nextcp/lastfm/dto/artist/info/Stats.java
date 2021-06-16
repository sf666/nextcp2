
package nextcp.lastfm.dto.artist.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "listeners", "playcount" })

public class Stats
{

    @JsonProperty("listeners")
    private String listeners;
    @JsonProperty("playcount")
    private String playcount;

    @JsonProperty("listeners")
    public String getListeners()
    {
        return listeners;
    }

    @JsonProperty("listeners")
    public void setListeners(String listeners)
    {
        this.listeners = listeners;
    }

    @JsonProperty("playcount")
    public String getPlaycount()
    {
        return playcount;
    }

    @JsonProperty("playcount")
    public void setPlaycount(String playcount)
    {
        this.playcount = playcount;
    }

    @Override
    public String toString()
    {
        return "Stats [listeners=" + listeners + ", playcount=" + playcount + "]";
    }

}
