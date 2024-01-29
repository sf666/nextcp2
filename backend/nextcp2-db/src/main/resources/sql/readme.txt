How to use:

After adding additional additions scripts, update attribute TARGET_DB_SCHEMA in the the class `nextcp.db.SessionManager` to the newest SQL
script number. The session manager will detect the current schema version and will update the database until the final target script is 
executed.


A script should end with upgrading the DB version to the current number:

`UPDATE DATABASE_CONFIG SET config_value = UPDATESCRIPT_NUM where config_entry = 'SCHEMA_VERSION';`
or 
`MERGE INTO DATABASE_CONFIG KEY (config_entry) VALUES ('SCHEMA_VERSION', UPDATESCRIPT_NUM);` 

