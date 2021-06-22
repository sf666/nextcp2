package nextcp.spotify.cache;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SpotifyMapping
{
    @Insert("insert into SPOTIFY_ARTIST(uri, artistName, artistJsonObj) values (#{uri}, #{artistName}, #{artistJsonObj})")
    int insertArtist(@Param("uri") String uri, @Param("artistName") String artistName, @Param("artistJsonObj") String artistJsonObj);

    @Select("SELECT artistJsonObj FROM SPOTIFY_ARTIST where artistName = #{artistName}")
    String selectArtistByName(@Param("artistName") String artistName);

    @Select("SELECT artistJsonObj FROM SPOTIFY_ARTIST where artistName = '#{spotifyId}'")
    String selectArtistBySpotifyId(@Param("spotifyId") String spotifyId);
}
