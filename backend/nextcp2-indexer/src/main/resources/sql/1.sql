-- Tables  
CREATE TABLE SONG_RATING(FILEPATH VARCHAR(4096) PRIMARY KEY, ACOUSTID UUID, MUSICBRAINZID UUID , RATING INT);

CREATE TABLE DATABASE_CONFIG(config_entry VARCHAR(64) PRIMARY KEY, config_value VARCHAR(2048));

-- Index  

create INDEX idx_acoustic on SONG_RATING(ACOUSTID);
create INDEX idx_musicbrainz on SONG_RATING(MUSICBRAINZID);

-- Update schema version to 1

INSERT INTO DATABASE_CONFIG VALUES ('SCHEMA_VERSION', 1); 