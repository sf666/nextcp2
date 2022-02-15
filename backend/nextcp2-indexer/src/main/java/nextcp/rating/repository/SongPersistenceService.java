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

    /**
     * Reads user rating from local CP database (USER_RATING table)
     * 
     * @param acoustID
     * @return
     */
    public Integer getRatingByAcoustID(String acoustID)
    {
        try (SqlSession session = sessionFactory.openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectUserRatingByAcoustId", acoustID);
        }
    }

    /**
     * Reads user rating from local CP database (USER_RATING table)
     * 
     * @param musicBrainzID
     * @return
     */
    public Integer getRatingByMusicBrainzID(String musicBrainzID)
    {
        try (SqlSession session = sessionFactory.openSession())
        {
            List<Integer> ratings = session.selectList("nextcp.rating.repository.sql.RatingMapping.selectUserRatingByMusicBrainzId", musicBrainzID);
            if (ratings.isEmpty())
            {
                return 0;
            }
            else
            {
                return ratings.get(0);
            }
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
            path = path.replaceAll("'", "''");
            String id = session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectMusicBrainzIDFromPath", path);
            return id;
        }
    }

    /**
     * adds star rating to USER_RATING table
     * 
     * @param userRating
     * @return
     */
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
            int num = session.insert("nextcp.rating.repository.sql.RatingMapping.insertUserRating", userRating);
            session.commit(true);
            return num;
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
            session.commit(true);
            return num;
        }
    }

    public int syncRating()
    {
        try (SqlSession session = sessionFactory.openSession(true))
        {
            int num = session.update("nextcp.rating.repository.sql.RatingMapping.syncUserRatingByMusicBrainzId", null);
            session.commit();
            return num;
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
