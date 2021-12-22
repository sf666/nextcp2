package nextcp.mymusic;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface MyMusicMapping
{
    
    //
    // Why ${} and nor #{}
    // =================================
    // 
    // ${uuid} is used, so the actual given string is inserted into the select statement without conversion.
    //
    
    @Select("SELECT count(*) FROM MY_ALBUM WHERE mb_release_id = '${uuid}'")
    int findAlbum(@Param("uuid") String uuid);
    
    @Update("MERGE INTO MY_ALBUM(mb_release_id) KEY (mb_release_id) VALUES ('${uuid}')")
    int addAlbum(@Param("uuid") String uuid);
    
    @Delete("DELETE FROM MY_ALBUM WHERE mb_release_id = '${uuid}'")
    boolean deleteAlbum(@Param("uuid") String uuid);
}
