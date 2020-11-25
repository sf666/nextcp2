drop INDEX idx_acoustic;
drop INDEX idx_musicbrainz;

drop TABLE SONG_RATING;

ALTER TABLE SONG_RATING_TMP RENAME TO SONG_RATING;

create INDEX idx_acoustic on SONG_RATING(ACOUSTID);
create INDEX idx_musicbrainz on SONG_RATING(MUSICBRAINZID);
