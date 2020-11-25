package nextcp.rating.repository;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nextcp.rating.RatingConfig;
import nextcp.rating.RatingException;
import nextcp.rating.repository.sql.DatabaseVersion;
import nextcp.rating.repository.sql.RatingMapping;

@Service
public class SessionManager
{
    private static final Logger log = LoggerFactory.getLogger(SessionManager.class.getName());

    private RatingConfig config = null;
    private SqlSessionFactory sqlSessionFactory = null;

    @Autowired
    public SessionManager(RatingConfig config)
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
                TransactionFactory transactionFactory = new JdbcTransactionFactory();
                Environment environment = new Environment("development", transactionFactory, getDataSource());
                Configuration configuration = new Configuration(environment);
                configuration.addMapper(RatingMapping.class);
                configuration.addMapper(DatabaseVersion.class);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            }
            catch (Exception e)
            {
                log.warn("Rating support disabled.", e);
            }
        }
    }

    private DataSource getDataSource()
    {
        if (StringUtils.isBlank(config.databaseFilename))
        {
            log.warn("missong database filename.");
            return null;
        }
        String dbUrl = String.format("jdbc:h2:%s", config.databaseFilename);
        PooledDataSource dataSource = new PooledDataSource("org.h2.Driver", dbUrl, "sa", "");
        return dataSource;
    }

    public SqlSessionFactory getSessionFactory()
    {
        if (sqlSessionFactory == null)
        {
            log.debug("session factory is null. Rating support is disabled.");
            throw new RatingException(RatingException.DATABASE_ACCESS_ERROR, "Session Factory cannot be created.");
        }

        return sqlSessionFactory;
    }

}
