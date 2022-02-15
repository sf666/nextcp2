package nextcp.rating.repository.sql;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import nextcp.rating.domain.SongIndexed;
import nextcp.rating.domain.UserRating;

/**
 * 
 */
public interface RatingMapping
{

    @Select("SELECT * FROM blog WHERE id = #{id}")
    SongIndexed findRating(int id);

    @Insert("INSERT into Song_Rating_TMP(FILEPATH, ACOUSTID, MUSICBRAINZID, RATING) VALUES(#{filePath}, #{acoustID}, #{musicBrainzID}, #{rating})")
    void insertSong(SongIndexed song);

    @Select("SELECT * FROM Song_Rating WHERE ACOUSTID = '${acoustID}'")
    SongIndexed selectAcoustIDSong(String acoustID);

    @Select("SELECT * FROM Song_Rating WHERE MUSICBRAINZID = '${musicBrainzID}'")
    SongIndexed selectMusicBrainzIDSong(String musicBrainzID);

    @Select("SELECT musicbrainzid FROM Song_Rating WHERE filepath = '${path}'")
    String selectMusicBrainzIDFromPath(String path);

    /**
     * Update Statements are prepared statements. Therefore no need to embrace #{filePath} with " ' "
     * 
     * @param song
     */
    @Update("UPDATE Song_Rating SET RATING = ${rating} WHERE MUSICBRAINZID = '${musicBrainzID}'")
    int updateRatingByMusicBrainzID(String musicBrainzID, int rating);

    @Update("UPDATE Song_Rating SET RATING = #{rating} WHERE FILEPATH = #{filePath}")
    void updateRating(SongIndexed song);

    @Select("SELECT RATING FROM Song_Rating where ACOUSTID = '${acoustID}'")
    Integer selectRatingByAcoustId(String acoustID);

    @Select("SELECT RATING FROM Song_Rating where MUSICBRAINZID = '${musicBrainzID}'")
    Integer selectRatingByMusicBrainzId(String musicBrainzID);

    //
    // User Ratings
    // ===================================================

    @Insert("insert into USER_RATING(MUSICBRAINZID,RATING) values (#{musicBrainzId}, #{rating})")
    int insertUserRating(UserRating userRating);

    @Update("UPDATE USER_RATING SET RATING = #{rating} WHERE MUSICBRAINZID = #{musicBrainzId}")
    void updateUserRatingByMusicBrainzId(UserRating userRating);

    @Update("UPDATE USER_RATING SET RATING = #{rating} WHERE ACOUSTID = #{acoustId}")
    void updateUserRatingByMusicAcoustId(UserRating userRating);

    @Select("SELECT RATING FROM USER_RATING where ACOUSTID = '${acoustID}'")
    Integer selectUserRatingByAcoustId(String acoustID);

    @Select("SELECT RATING FROM USER_RATING where MUSICBRAINZID = '${musicBrainzID}'")
    Integer selectUserRatingByMusicBrainzId(String musicBrainzID);
    
    //
    // Copy file ratings to user rating
    //
    @Update("MERGE INTO USER_RATING(MUSICBRAINZID, RATING) KEY (MUSICBRAINZID) SELECT MUSICBRAINZID, RATING FROM Song_Rating where MUSICBRAINZID is not null")
    void syncUserRatingByMusicBrainzId();
}
