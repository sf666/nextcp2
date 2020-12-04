package nextcp.db.sql;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import nextcp.db.service.KeyValuePair;

/**
 * This interface describes some basic DB support for SQL schema updates and key/value stores.
 */
public interface DatabaseMapper
{
    @Select("SELECT count(*) FROM INFORMATION_SCHEMA.TABLES where TABLE_NAME = '${tableName}'")
    Integer findTableName(String tableName);

    @Select("SELECT config_value FROM DATABASE_CONFIG where config_entry = 'SCHEMA_VERSION'")
    Integer selectSchemaVersion();

    @Update("UPDATE DATABASE_CONFIG SET config_value = ${num} where config_entry = 'SCHEMA_VERSION'")
    Integer updateSchemaVersion(Integer num);

    /**
     * Read any config value
     * 
     * @param configEntry
     * @return
     */
    @Select("SELECT config_value FROM DATABASE_CONFIG where config_entry = '${key}'")
    String selectConfigValue(String key);

    /**
     * Insert or update any config value
     * 
     * @param key
     *            config key
     * @param value
     *            config value
     * @return 0 = unsuccess, 1 = success
     */
    @Update("MERGE INTO DATABASE_CONFIG KEY (config_entry) VALUES (#{key}, #{value})")
    Integer updateConfigValue(KeyValuePair keyValue);

    /**
     * Read any config value
     * 
     * @param configEntry
     * @return
     */
    @Select("SELECT lookupValue FROM JSON_STORE where lookupKey = '${key}'")
    String selectJsonValue(String key);

    /**
     * Insert or update any config value
     * 
     * @param key
     *            config key
     * @param value
     *            config value
     * @return 0 = unsuccess, 1 = success
     */
    @Update("MERGE INTO JSON_STORE KEY (lookupKey) VALUES (#{key}, #{value})")
    Integer updateJsonValue(KeyValuePair keyValue);
}
