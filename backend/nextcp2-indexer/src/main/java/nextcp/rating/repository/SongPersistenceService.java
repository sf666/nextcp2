package nextcp.rating.repository;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.rating.domain.SongIndexed;
import nextcp.rating.domain.UserRating;

/**
 * Database layer for reading and updating song objects
 */
@Service
public class SongPersistenceService
{
    private IndexerSessionFactory sessionFactory = null;

    @Autowired
    public SongPersistenceService(IndexerSessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    public SongIndexed getSongByAcoustId(String acoustId)
    {
        try (SqlSession session = sessionFactory.openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectAcoustIDSong", acoustId);
        }
    }

    public List<SongIndexed> getSongByMusicBrainzId(String musicBrainzId)
    {
        try (SqlSession session = sessionFactory.openSession())
        {
            return session.selectList("nextcp.rating.repository.sql.RatingMapping.selectMusicBrainzIDSong", musicBrainzId);
        }
    }

    public Integer getRatingByAcoustID(String acoustID)
    {
        try (SqlSession session = sessionFactory.openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectRatingByAcoustId", acoustID);
        }
    }

    public Integer getRatingByMusicBrainzID(String musicBrainzID)
    {
        try (SqlSession session = sessionFactory.openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectRatingByMusicBrainzId", musicBrainzID);
        }
    }

    public int updateSong(SongIndexed song)
    {
        try (SqlSession session = sessionFactory.openSession(true))
        {
            int num = session.update("nextcp.rating.repository.sql.RatingMapping.updateRating", song);
            session.commit(true);
            return num;
        }
    }

    public String selectMusicBrainzIDFromPath(String path)
    {
        try (SqlSession session = sessionFactory.openSession(true))
        {
            path = path.replaceAll("'","''");
            String id = session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectMusicBrainzIDFromPath", path);
            return id;
        }        
    }
    
    public int insertOrUpdateUserRating(UserRating userRating)
    {
        int up = updateUserRating(userRating);
        if (up == 0)
        {
            return insertUserRating(userRating);
        }
        return up;
    }

    public int insertUserRating(UserRating userRating)
    {
        if (allIdsEmpty(userRating))
        {
            throw new RuntimeException("AcoustID and MuicBrainzID are both empty. Cannot update user rating.");
        }

        try (SqlSession session = sessionFactory.openSession(true))
        {
            return session.insert("nextcp.rating.repository.sql.RatingMapping.insertUserRating", userRating);
        }
    }

    public int updateUserRating(UserRating userRating)
    {
        if (allIdsEmpty(userRating))
        {
            throw new RuntimeException("AcoustID and MuicBrainzID are both empty. Cannot update user rating.");
        }

        try (SqlSession session = sessionFactory.openSession(true))
        {
            int num = 0;
            if (StringUtils.isAllBlank(userRating.musicBrainzId))
            {
                num = session.update("nextcp.rating.repository.sql.RatingMapping.updateUserRatingByMusicAcoustId", userRating);
            }
            else
            {
                num = session.update("nextcp.rating.repository.sql.RatingMapping.updateUserRatingByMusicBrainzId", userRating);
            }
            return num;
        }
    }
    
    public int syncRating()
    {
        try (SqlSession session = sessionFactory.openSession(true))
        {
            return session.update("nextcp.rating.repository.sql.RatingMapping.syncUserRatingByMusicBrainzId",null);
        }
    }

    private boolean allIdsEmpty(UserRating userRating)
    {
        if (StringUtils.isAllBlank(userRating.musicBrainzId) && StringUtils.isAllBlank(userRating.acoustId))
        {
            return true;
        }
        return false;
    }    
}
