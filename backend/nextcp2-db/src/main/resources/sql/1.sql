-- Tables  
-- ========================================


-- config entries 
CREATE TABLE DATABASE_CONFIG(config_entry VARCHAR(64) PRIMARY KEY, config_value VARCHAR(2048));

-- Key Value table store
CREATE TABLE JSON_STORE(lookupKey VARCHAR(64) PRIMARY KEY, lookupValue CLOB);

-- Spotify
CREATE TABLE SPOTIFY_ARTIST(uri VARCHAR(64) PRIMARY KEY, artistName VARCHAR(64), artistJsonObj CLOB);
CREATE UNIQUE INDEX IDX_SPOTIFY_ARTIST ON SPOTIFY_ARTIST(artistName);


-- Update schema version to 1

INSERT INTO DATABASE_CONFIG VALUES ('SCHEMA_VERSION', 1); 