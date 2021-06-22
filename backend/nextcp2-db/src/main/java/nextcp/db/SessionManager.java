package nextcp.db;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.db.sql.DatabaseMapper;

/**
 * This service creates SqlSessionFactories for other modules. Supply a mapper list to acquire a SqlSessionFactory which can be reused.
 * 
 */
@Service
public class SessionManager
{
    private static final Logger log = LoggerFactory.getLogger(SessionManager.class.getName());

    private SqlSessionFactory sqlSessionFactory = null;

    private DatabaseConfig config;

    private TransactionFactory transactionFactory = null;

    private Environment environment = null;

    // Schema update constants
    private final int TARGET_DB_SCHEMA = 4;

    @Autowired
    public SessionManager(DatabaseConfig config)
    {
        this.config = config;

        if (StringUtils.isBlank(config.databaseFilename))
        {
            log.warn("Rating support disabled: Configuration 'databaseFilename' must be provided.");
        }
        else
        {
            try
            {
                transactionFactory = new JdbcTransactionFactory();
                environment = new Environment("development", transactionFactory, getDataSource());
            }
            catch (Exception e)
            {
                log.warn("cannot create datbase connection. Database support is disabled.", e);
            }
        }

        List<Class<?>> mapper = new ArrayList<Class<?>>();
        mapper.add(DatabaseMapper.class);
        sqlSessionFactory = getSessionFactory(mapper);

        updateDatabaseToCurrentSchema();
    }

    /**
     * This method returns the SqlSessionFactory used by this Session-Manager
     * 
     * @return
     */
    public SqlSessionFactory getSessionFactory()
    {
        return sqlSessionFactory;
    }

    /**
     * This method returns a new SqlSessionFactory configured with supplied Mapping classes
     * 
     * @return
     */
    public SqlSessionFactory getSessionFactory(List<Class<?>> mapperList)
    {
        if (environment == null)
        {
            throw new RuntimeException("database not initialized.");
        }
        Configuration configuration = new Configuration(environment);
        for (Class<?> mapper : mapperList)
        {
            configuration.addMapper(mapper);
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory;
    }

    /**
     * Easy schema update concept ... just increment sql-script number ... On heavy concurrent work, this will not suit. Use something like firefly instead
     */
    public void updateDatabaseToCurrentSchema()
    {
        try
        {
            int currentVersion = getCurrentSchemaVersion();
            while (currentVersion < TARGET_DB_SCHEMA)
            {
                currentVersion++;
                String sqlScriptPath = String.format("/sql/%d.sql", currentVersion);
                runScript(sqlScriptPath);
            }
        }
        catch (Exception e)
        {
            log.warn("cannot update database.", e);
        }
    }

    private void runScript(String sqlScriptPath)
    {
        try (InputStreamReader isr = new InputStreamReader(SessionManager.class.getResourceAsStream(sqlScriptPath)))
        {
            org.apache.ibatis.jdbc.ScriptRunner scriptRunner = new org.apache.ibatis.jdbc.ScriptRunner(sqlSessionFactory.openSession().getConnection());
            scriptRunner.runScript(isr);

        }
        catch (IOException e)
        {
            log.error("sql script error", e);
        }
    }

    /**
     * 
     * @return
     */
    public int getCurrentSchemaVersion()
    {
        try (SqlSession session = sqlSessionFactory.openSession())
        {
            Integer count = session.selectOne("nextcp.db.sql.DatabaseMapper.findTableName", "DATABASE_CONFIG");
            if (count == 0)
            {
                return 0;
            }
            return session.selectOne("nextcp.db.sql.DatabaseMapper.selectSchemaVersion");
        }
        catch (Exception e)
        {
            rebuildDbFromScratch();
            // remove this exception, after above method is implemented
            log.error("DB error", e);
            throw new RuntimeException("Database is corrupt.");
        }
    }

    private void rebuildDbFromScratch()
    {
        // Not implemented yet ...
    }

    private DataSource getDataSource()
    {
        if (StringUtils.isBlank(config.databaseFilename))
        {
            log.warn("missing database filename.");
            return null;
        }
        log.info("using database path : " + config.databaseFilename == null ? "UNSET" : config.databaseFilename);
        String dbUrl = String.format("jdbc:h2:%s", config.databaseFilename);
        PooledDataSource dataSource = new PooledDataSource("org.h2.Driver", dbUrl, "sa", "");
        return dataSource;
    }

}
