
package nextcp.lastfm.dto.artist.info;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "name", "url", "image" })

public class SimilarArtist
{

    @JsonProperty("name")
    private String name;
    @JsonProperty("url")
    private String url;
    @JsonProperty("image")
    private List<Image> image = null;

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

    @Override
    public String toString()
    {
        return "SimilarArtist [name=" + name + ", url=" + url + ", image=" + image + "]";
    }

}
