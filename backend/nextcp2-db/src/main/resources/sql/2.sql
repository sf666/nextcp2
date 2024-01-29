-- Tables  
-- ========================================


-- config entries 
ALTER TABLE DATABASE_CONFIG ALTER COLUMN config_value CHARACTER VARYING;

-- Update schema version to 2

MERGE INTO DATABASE_CONFIG KEY (config_entry) VALUES ('SCHEMA_VERSION', 2); 
