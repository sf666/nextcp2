-- Table for liked albums
--  


CREATE TABLE MY_ALBUM(mb_release_id UUID PRIMARY KEY);

-- Update schema version to 4

UPDATE DATABASE_CONFIG SET config_value = 5 where config_entry = 'SCHEMA_VERSION';
