package nextcp.rating.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.rating.domain.SongRating;

@Service
public class SongPersistenceService
{
    @Autowired
    private SessionManager sessionManager = null;

    public SongRating getSongByAcoustId(String acoustId)
    {
        try (SqlSession session = sessionManager.getSessionFactory().openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectAcoustIDSong", acoustId);
        }
    }

    public SongRating getSongByMusicBrainzId(String musicBrainzId)
    {
        try (SqlSession session = sessionManager.getSessionFactory().openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectMusicBrainzIDSong", musicBrainzId);
        }
    }

    public Integer getRatingByAcoustID(String acoustID)
    {
        try (SqlSession session = sessionManager.getSessionFactory().openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectRatingByAcoustId", acoustID);
        }
    }

    public Integer getRatingByMusicBrainzID(String musicBrainzID)
    {
        try (SqlSession session = sessionManager.getSessionFactory().openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectRatingByMusicBrainzId", musicBrainzID);
        }
    }

    public int updateSong(SongRating song)
    {
        try (SqlSession session = sessionManager.getSessionFactory().openSession())
        {
            return session.update("nextcp.rating.repository.sql.RatingMapping.updateRating", song);
        }
    }
}
