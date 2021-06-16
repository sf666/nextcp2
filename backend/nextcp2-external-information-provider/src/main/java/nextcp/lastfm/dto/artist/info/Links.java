
package nextcp.lastfm.dto.artist.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "link" })

public class Links
{

    @JsonProperty("link")
    private Link link;

    @JsonProperty("link")
    public Link getLink()
    {
        return link;
    }

    @JsonProperty("link")
    public void setLink(Link link)
    {
        this.link = link;
    }

    @Override
    public String toString()
    {
        return "Links [link=" + link + "]";
    }

}
