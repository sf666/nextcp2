package nextcp.rating.repository;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.rating.domain.SongIndexed;

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

    public SongIndexed getSongByMusicBrainzId(String musicBrainzId)
    {
        try (SqlSession session = sessionFactory.openSession())
        {
            return session.selectOne("nextcp.rating.repository.sql.RatingMapping.selectMusicBrainzIDSong", musicBrainzId);
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
}
