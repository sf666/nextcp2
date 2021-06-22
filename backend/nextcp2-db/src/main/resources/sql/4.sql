-- Spotify
--  

CREATE TABLE SPOTIFY_ARTIST(uri VARCHAR(64) PRIMARY KEY, artistName VARCHAR(64), artistJsonObj CLOB);
CREATE UNIQUE INDEX IDX_SPOTIFY_ARTIST ON SPOTIFY_ARTIST(artistName);

-- Update schema version to 4

UPDATE DATABASE_CONFIG SET config_value = 4 where config_entry = 'SCHEMA_VERSION';
