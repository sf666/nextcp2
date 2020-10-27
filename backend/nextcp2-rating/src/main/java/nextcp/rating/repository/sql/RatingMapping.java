package nextcp.rating.repository.sql;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import nextcp.rating.domain.SongRating;

/**
 * 
 */
public interface RatingMapping
{

    @Select("SELECT * FROM blog WHERE id = #{id}")
    SongRating findRating(int id);

    @Insert("INSERT into Song_Rating_TMP(FILEPATH, ACOUSTID, MUSICBRAINZID, RATING) VALUES(#{filePath}, #{acoustID}, #{musicBrainzID}, #{rating})")
    void insertSong(SongRating song);

    @Select("SELECT * FROM Song_Rating WHERE ACOUSTID = '${acoustID}'")
    SongRating selectAcoustIDSong(String acoustID);

    @Select("SELECT * FROM Song_Rating WHERE MUSICBRAINZID = '${musicBrainzID}'")
    SongRating selectMusicBrainzIDSong(String musicBrainzID);

    /**
     * Update Statements are prepared statements. Therefore no need to embrace #{filePath} with " ' "
     * 
     * @param song
     */
    @Update("UPDATE Song_Rating SET RATING = #{rating} WHERE FILEPATH = #{filePath}")
    void updateRating(SongRating song);

    @Select("SELECT RATING FROM Song_Rating where ACOUSTID = '${acoustID}'")
    Integer selectRatingByAcoustId(String acoustID);

    @Select("SELECT RATING FROM Song_Rating where MUSICBRAINZID = '${musicBrainzID}'")
    Integer selectRatingByMusicBrainzId(String musicBrainzID);
}
