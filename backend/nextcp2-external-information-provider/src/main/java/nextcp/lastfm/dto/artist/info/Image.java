
package nextcp.lastfm.dto.artist.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "#text", "size" })

public class Image
{

    @JsonProperty("#text")
    private String text;
    @JsonProperty("size")
    private String size;

    @JsonProperty("#text")
    public String getText()
    {
        return text;
    }

    @JsonProperty("#text")
    public void setText(String text)
    {
        this.text = text;
    }

    @JsonProperty("size")
    public String getSize()
    {
        return size;
    }

    @JsonProperty("size")
    public void setSize(String size)
    {
        this.size = size;
    }

    @Override
    public String toString()
    {
        return "Image [text=" + text + ", size=" + size + "]";
    }

}
