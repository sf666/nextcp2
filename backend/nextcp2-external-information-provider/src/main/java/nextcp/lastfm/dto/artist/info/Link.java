
package nextcp.lastfm.dto.artist.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "#text", "rel", "href" })

public class Link
{

    @JsonProperty("#text")
    private String text;
    @JsonProperty("rel")
    private String rel;
    @JsonProperty("href")
    private String href;

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

    @JsonProperty("rel")
    public String getRel()
    {
        return rel;
    }

    @JsonProperty("rel")
    public void setRel(String rel)
    {
        this.rel = rel;
    }

    @JsonProperty("href")
    public String getHref()
    {
        return href;
    }

    @JsonProperty("href")
    public void setHref(String href)
    {
        this.href = href;
    }

    @Override
    public String toString()
    {
        return "Link [text=" + text + ", rel=" + rel + ", href=" + href + "]";
    }

}
