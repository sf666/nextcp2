package nextcp.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import nextcp.db.service.BasicDbService;
import nextcp.db.service.KeyValuePair;
import nextcp.dto.Config;

/**
 * Properties saved in database.
 * 
 * Not used at the moment, because we deleted client config persistence in the database.
 * 
 * Remove class if it has no use any more ... 
 */
public class DatabaseConfigPersistence
{

    private static final Logger log = LoggerFactory.getLogger(DatabaseConfigPersistence.class.getName());

    private BasicDbService db = null;
    private static final String UI_DEFAULT_CLIENT_CONFIG_VALUE = "UI_CLIENT_CONFIG";

    private Config config = null;
    private ObjectMapper om = new ObjectMapper();

    public DatabaseConfigPersistence(BasicDbService db, Config config)
    {
        super();
        this.db = db;
        this.config = config;
    }

    public void writeDatabaseConfig()
    {
//        try
//        {
//            String cc_json = om.writeValueAsString(config.clientConfig);
//            db.updateJsonStoreValue(new KeyValuePair(UI_DEFAULT_CLIENT_CONFIG_VALUE, cc_json));
//        }
//        catch (Exception e)
//        {
//            log.error("couldn't write CLIENT_CONFIG to database : ", e);
//        }
    }

//    public List<UiClientConfig> readClientConfigDatabaseProperties()
//    {
//        String cc_json = db.selectJsonStoreValue(UI_DEFAULT_CLIENT_CONFIG_VALUE);
//        if (!StringUtils.isAllBlank(cc_json))
//        {
//            try
//            {
//                return om.readValue(cc_json, new TypeReference<List<UiClientConfig>>()
//                {
//                });
//            }
//            catch (Exception e)
//            {
//                log.warn("client config list : initializing empty array list", e);
//            }
//        }
//        log.debug("no client configuration available yet ...");
//        return new ArrayList<>();
//    }

}
