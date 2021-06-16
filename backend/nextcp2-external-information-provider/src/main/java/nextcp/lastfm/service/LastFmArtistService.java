package nextcp.lastfm.service;

import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.lastfm.ILastFmConfig;
import nextcp.lastfm.LastFmException;
import nextcp.lastfm.dto.artist.info.ArtistInfoResponse;
import nextcp.lastfm.dto.artist.info.Image;
import nextcp.lastfm.request.LastFmApiRequestService;
import nextcp.lastfm.request.Parameter;

@Service
public class LastFmArtistService
{

    public enum ImageSize
    {
        small,
        medium,
        large,
        extralarge,
        mega
    };

    @Autowired
    private LastFmApiRequestService api = null;

    @Autowired
    private ILastFmConfig config = null;

    public LastFmArtistService()
    {
    }

    public ArtistInfoResponse getInfoByMbId(String mbId)
    {
        return getInfo(null, mbId, null, false, null);
    }

    public ArtistInfoResponse getInfoByArtist(String artist)
    {
        return getInfo(artist, null, null, false, null);
    }

    /**
     * Size can be
     * 
     * @param size
     * @return
     */
    public String extractImageUrl(ArtistInfoResponse response, ImageSize size)
    {
        String backupUrl = "";
        for (Image img : response.getArtist().getImage())
        {
            if (StringUtils.isBlank(img.getSize()))
            {
                backupUrl = img.getText();
            }
            else if (ImageSize.valueOf(img.getSize()).equals(size))
            {
                return img.getText();
            }
        }
        return backupUrl;
    }

    /**
     * @param artist
     *            (Required (unless mbid)] : The artist name
     * @param mbid
     *            mbid (Optional) : The musicbrainz id for the artist
     * @param lang
     *            lang (Optional) : The language to return the biography in, expressed as an ISO 639 alpha-2 code.
     * @param autoCorrect
     *            autocorrect[false|true] (Optional) : Transform misspelled artist names into correct artist names, returning the correct version instead. The corrected artist name
     *            will be returned in the response.
     * @param userName
     *            username (Optional) : The username for the context of the request. If supplied, the user's playcount for this artist is included in the response.
     */
    public ArtistInfoResponse getInfo(String artist, String mbid, String lang, boolean autoCorrect, String userName)
    {
        if (artist == null && mbid == null)
        {
            throw new LastFmException(9999, "artist or MusicBrainzID must be provided");
        }

        TreeSet<Parameter> params = new TreeSet<>();
        params.add(new Parameter("api_key", config.getApiKey()));
        params.add(new Parameter("method", "artist.getinfo"));

        addParameter("artist", artist, params);
        addParameter("mbid", mbid, params);
        addParameter("lang", lang, params);
        addParameter("autoCorrect", autoCorrect ? "1" : "0", params);
        addParameter("userName", userName, params);

        ArtistInfoResponse artistInfoResponse = api.doApiRequest(params, ArtistInfoResponse.class);
        return artistInfoResponse;
    }

    private void addParameter(String name, String value, TreeSet<Parameter> params)
    {
        if (value != null)
        {
            params.add(new Parameter(name, value));
        }
    }
}
