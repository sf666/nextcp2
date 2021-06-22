package nextcp.spotify.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.db.SessionManager;

@Service
public class SpotifySessionFactory
{
    private SqlSessionFactory sessionFactory = null;

    public SpotifySessionFactory()
    {
    }

    @Autowired
    public SpotifySessionFactory(SessionManager sessionManager)
    {
        List<Class<?>> mapper = new ArrayList<Class<?>>();
        mapper.add(SpotifyMapping.class);
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
