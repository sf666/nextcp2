How to use:

After adding additional scripts, update the `nextcp.db.SessionManager.TARGET_DB_SCHEMA` attribute to reflect which scripts to run ... 

The session manager will detect the current schema version and will update until the `nextcp.db.SessionManager.TARGET_DB_SCHEMA` scriptnumber is executed.

The script should end with upgrading the DB version to the current number:

`UPDATE DATABASE_CONFIG SET config_value = UPDATESCRIPT_NUM where config_entry = 'SCHEMA_VERSION';`
