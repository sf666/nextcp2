package nextcp.rating.repository;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.rating.domain.UserRating;

@Service
public class UserRatingPersistenceService
{
    @Autowired
    private SessionManager sessionManager = null;

    public Integer getRatingByAcoustID(String acoustID)
    {
        try (SqlSession session = sessionManager.getSessionFactory().openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectUserRatingByAcoustId", acoustID);
        }
    }

    public Integer getRatingByMusicBrainzID(String musicBrainzID)
    {
        try (SqlSession session = sessionManager.getSessionFactory().openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectUserRatingByMusicBrainzId", musicBrainzID);
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
            throw new RuntimeException("AcoustID und MuicBrainzID are both empty. Cannot update user rating.");
        }

        try (SqlSession session = sessionManager.getSessionFactory().openSession(true))
        {
            return session.insert("nextcp.rating.repository.sql.RatingMapping.insertUserRating", userRating);
        }
    }

    public int updateUserRating(UserRating userRating)
    {
        if (allIdsEmpty(userRating))
        {
            throw new RuntimeException("AcoustID und MuicBrainzID are both empty. Cannot update user rating.");
        }

        try (SqlSession session = sessionManager.getSessionFactory().openSession(true))
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
        try (SqlSession session = sessionManager.getSessionFactory().openSession(true))
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
