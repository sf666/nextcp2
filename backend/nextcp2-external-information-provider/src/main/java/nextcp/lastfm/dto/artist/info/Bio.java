
package nextcp.lastfm.dto.artist.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "links", "published", "summary", "content" })

public class Bio
{

    @JsonProperty("links")
    private Links links;
    @JsonProperty("published")
    private String published;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("content")
    private String content;

    @Override
    public String toString()
    {
        return "Bio [links=" + links + ", published=" + published + ", summary=" + summary + ", content=" + content + "]";
    }

    @JsonProperty("links")
    public Links getLinks()
    {
        return links;
    }

    @JsonProperty("links")
    public void setLinks(Links links)
    {
        this.links = links;
    }

    @JsonProperty("published")
    public String getPublished()
    {
        return published;
    }

    @JsonProperty("published")
    public void setPublished(String published)
    {
        this.published = published;
    }

    @JsonProperty("summary")
    public String getSummary()
    {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary)
    {
        this.summary = summary;
    }

    @JsonProperty("content")
    public String getContent()
    {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content)
    {
        this.content = content;
    }

}
