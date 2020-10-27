package nextcp.rating.repository.sql;

import org.apache.ibatis.annotations.Select;

public interface DatabaseVersion
{
    @Select("SELECT count(*) FROM INFORMATION_SCHEMA.TABLES where TABLE_NAME = '${tableName}'")
    Integer findRating(String tableName);

    @Select("SELECT config_value FROM DATABASE_CONFIG where config_entry = 'SCHEMA_VERSION'")
    Integer selectSchemaVersion();

    /**
     * Read any config value
     * @param configEntry
     * @return
     */
    @Select("SELECT config_value FROM DATABASE_CONFIG where config_entry = '${configEntry}'")
    String selectConfigValue(String configEntry);
    
}
