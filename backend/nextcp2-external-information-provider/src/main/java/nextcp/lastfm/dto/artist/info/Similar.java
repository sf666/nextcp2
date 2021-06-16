
package nextcp.lastfm.dto.artist.info;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
{ "artist" })
public class Similar
{

    @JsonProperty("artist")
    private List<SimilarArtist> artist = null;

    @JsonProperty("artist")
    public List<SimilarArtist> getArtist()
    {
        return artist;
    }

    @JsonProperty("artist")
    public void setArtist(List<SimilarArtist> artist)
    {
        this.artist = artist;
    }

    @Override
    public String toString()
    {
        return "Similar [artist=" + artist + "]";
    }

}
