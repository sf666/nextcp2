package nextcp.db.sql;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
     * @param configEntry
     * @return
     */
    @Select("SELECT config_value FROM DATABASE_CONFIG where config_entry = '${configEntry}'")
    String selectConfigValue(String configEntry);
    
}
