
package nextcp.lastfm.dto.artist.info;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "name", "mbid", "url", "image", "streamable", "ontour", "stats", "similar", "tags", "bio" })

public class Artist
{

    @JsonProperty("name")
    private String name;
    @JsonProperty("mbid")
    private String mbid;
    @JsonProperty("url")
    private String url;
    @JsonProperty("image")
    private List<Image> image = null;
    @JsonProperty("streamable")
    private String streamable;
    @JsonProperty("ontour")
    private String ontour;
    @JsonProperty("stats")
    private Stats stats;
    @JsonProperty("similar")
    private Similar similar;
    @JsonProperty("tags")
    private Tags tags;
    @JsonProperty("bio")
    private Bio bio;

    @Override
    public String toString()
    {
        return "Artist [name=" + name + ", mbid=" + mbid + ", url=" + url + ", image=" + image + ", streamable=" + streamable + ", ontour=" + ontour + ", stats=" + stats
                + ", similar=" + similar + ", tags=" + tags + ", bio=" + bio + "]";
    }

    @JsonProperty("name")
    public String getName()
    {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name)
    {
        this.name = name;
    }

    @JsonProperty("mbid")
    public String getMbid()
    {
        return mbid;
    }

    @JsonProperty("mbid")
    public void setMbid(String mbid)
    {
        this.mbid = mbid;
    }

    @JsonProperty("url")
    public String getUrl()
    {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url)
    {
        this.url = url;
    }

    @JsonProperty("image")
    public List<Image> getImage()
    {
        return image;
    }

    @JsonProperty("image")
    public void setImage(List<Image> image)
    {
        this.image = image;
    }

    @JsonProperty("streamable")
    public String getStreamable()
    {
        return streamable;
    }

    @JsonProperty("streamable")
    public void setStreamable(String streamable)
    {
        this.streamable = streamable;
    }

    @JsonProperty("ontour")
    public String getOntour()
    {
        return ontour;
    }

    @JsonProperty("ontour")
    public void setOntour(String ontour)
    {
        this.ontour = ontour;
    }

    @JsonProperty("stats")
    public Stats getStats()
    {
        return stats;
    }

    @JsonProperty("stats")
    public void setStats(Stats stats)
    {
        this.stats = stats;
    }

    @JsonProperty("similar")
    public Similar getSimilar()
    {
        return similar;
    }

    @JsonProperty("similar")
    public void setSimilar(Similar similar)
    {
        this.similar = similar;
    }

    @JsonProperty("tags")
    public Tags getTags()
    {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(Tags tags)
    {
        this.tags = tags;
    }

    @JsonProperty("bio")
    public Bio getBio()
    {
        return bio;
    }

    @JsonProperty("bio")
    public void setBio(Bio bio)
    {
        this.bio = bio;
    }

}
