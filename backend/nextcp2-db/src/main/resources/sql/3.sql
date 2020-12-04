CREATE TABLE JSON_STORE(lookupKey VARCHAR(64) PRIMARY KEY, lookupValue JSON);

-- Cleanup task
delete from DATABASE_CONFIG where config_value = 'RENDERER_DEVICES';

-- Update schema version

UPDATE DATABASE_CONFIG SET config_value = 3 where config_entry = 'SCHEMA_VERSION';