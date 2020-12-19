package nextcp.rating.repository;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
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
}
