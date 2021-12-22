package nextcp.mymusic;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.rating.repository.IndexerSessionFactory;

@Service
public class MyMusicRepository
{
    private static final Logger log = LoggerFactory.getLogger(MyMusicRepository.class.getName());
    private IndexerSessionFactory sessionFactory = null;

    @Autowired
    public MyMusicRepository(IndexerSessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    public void likeAlbum(String uuid)
    {
        checkParam(uuid);
        
        try (SqlSession session = sessionFactory.openSession())
        {
            MyMusicMapping myMusic = session.getMapper(MyMusicMapping.class);
            int inserts = myMusic.addAlbum(uuid);
            if (inserts == 0)
            {
                log.warn(uuid + " cannot be inserted.");
            }
            session.commit();
        }
    }

   
    public void deleteAlbumLike(String uuid)
    {
        checkParam(uuid);
        
        try (SqlSession session = sessionFactory.openSession())
        {
            MyMusicMapping myMusic = session.getMapper(MyMusicMapping.class);
            boolean deleted = myMusic.deleteAlbum(uuid);
            if (!deleted)
            {
                log.warn(uuid + " cannot be deleted from album liked list.");
            }
            session.commit();
        }
    }
    
    public boolean isAlbumLiked(String uuid)
    {
        checkParam(uuid);
        
        try (SqlSession session = sessionFactory.openSession())
        {
            MyMusicMapping myMusic = session.getMapper(MyMusicMapping.class);
            int count = myMusic.findAlbum(uuid);
            if (count > 0)
            {
                return true;
            }
            return false;
        }
    }
    
    private void checkParam(String uuid)
    {
        if (StringUtils.isAllBlank(uuid))
        {
            log.warn("no uuid provided");
            throw new RuntimeException("no uuid provided");
        }
    }
    
}
