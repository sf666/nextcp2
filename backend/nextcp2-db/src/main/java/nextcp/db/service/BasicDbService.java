package nextcp.db.service;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import nextcp.db.SessionManager;

@Service
public class BasicDbService
{
    private SqlSessionFactory factory = null;

    public BasicDbService(SessionManager sessionManager)
    {
        factory = sessionManager.getSessionFactory();
    }

    /**
     * 
     * @param tableName
     *            Tablename to find
     * @return 0 = not found, 1 = found
     */
    public Integer findTable(String tableName)
    {
        try (SqlSession session = factory.openSession())
        {
            return session.selectOne("nextcp.db.sql.DatabaseMapper.findTableName", tableName);
        }
    }

    /**
     * 
     * @param key
     *            lookup key
     * 
     * @return 0 = not found, 1 = found
     */
    public String selectConfigValue(String key)
    {
        try
        {
            try (SqlSession session = factory.openSession())
            {
                return session.selectOne("nextcp.db.sql.DatabaseMapper.selectConfigValue", key);
            }
        }
        catch (Exception e)
        {
            // No value ...
            return null;
        }
    }

    /**
     * 
     * @param key
     *            lookup key
     * 
     * @return 0 = not found, 1 = found
     */
    public Integer updateConfigValue(KeyValuePair keyValue)
    {
        try (SqlSession session = factory.openSession())
        {
            return session.selectOne("nextcp.db.sql.DatabaseMapper.updateConfigValue", keyValue);
        }
    }

    /**
     * 
     * @param key
     *            lookup key
     * 
     * @return 0 = not found, 1 = found
     */
    public String selectJsonStoreValue(String key)
    {
        try
        {
            try (SqlSession session = factory.openSession())
            {
                return session.selectOne("nextcp.db.sql.DatabaseMapper.selectJsonValue", key);
            }
        }
        catch (Exception e)
        {
            // No value ...
            return null;
        }
    }

    /**
     * 
     * @param key
     *            lookup key
     * 
     * @return 0 = not found, 1 = found
     */
    public Integer updateJsonStoreValue(KeyValuePair keyValue)
    {
        try (SqlSession session = factory.openSession())
        {
            return session.selectOne("nextcp.db.sql.DatabaseMapper.updateJsonValue", keyValue);
        }
    }
}
