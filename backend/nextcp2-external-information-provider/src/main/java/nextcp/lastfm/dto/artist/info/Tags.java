
package nextcp.lastfm.dto.artist.info;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "tag" })

public class Tags
{

    @JsonProperty("tag")
    private List<Tag> tag = null;

    @JsonProperty("tag")
    public List<Tag> getTag()
    {
        return tag;
    }

    @JsonProperty("tag")
    public void setTag(List<Tag> tag)
    {
        this.tag = tag;
    }

    @Override
    public String toString()
    {
        return "Tags [tag=" + tag + "]";
    }

}
