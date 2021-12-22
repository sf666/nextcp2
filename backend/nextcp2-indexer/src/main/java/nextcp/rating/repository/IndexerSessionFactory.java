package nextcp.rating.repository;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.db.SessionManager;
import nextcp.mymusic.MyMusicMapping;
import nextcp.rating.repository.sql.RatingMapping;

@Service
public class IndexerSessionFactory
{
    private SqlSessionFactory sessionFactory = null;

    @Autowired
    public IndexerSessionFactory(SessionManager sessionManager)
    {
        List<Class<?>> mapper = new ArrayList<Class<?>>();
        mapper.add(RatingMapping.class);
        mapper.add(MyMusicMapping.class);
        sessionFactory = sessionManager.getSessionFactory(mapper);
    }

    public SqlSessionFactory getSessionFactroy()
    {
        return sessionFactory;
    }

    // delegates
    
    public SqlSession openSession(boolean autoCommit)
    {
        return sessionFactory.openSession(autoCommit);
    }

    public SqlSession openSession()
    {
        return sessionFactory.openSession();
    }
}
