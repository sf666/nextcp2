package nextcp.rating.domain;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.flac.FlacTag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v11Tag;
import org.jaudiotagger.tag.vorbiscomment.VorbisCommentTag;

/**
 * <pre>
 * 
 * ID3v2 Tags support:
 * ======================================= 
 * 
 * There is a "Popularimeter" frame in the ID3v2 specification meant for this purpose. 
 * The frame is called POPM and Windows Explorer, Windows Media Player, Winamp, foobar2000, MediaMonkey, 
 * and other software all map roughly the same ranges of 0–255 to a 0–5 stars value for display.
 * 
 * The following list details how Windows Explorer reads and writes the POPM frame:
 * 
 * 224–255 = 5 stars when READ with Windows Explorer, writes 255
 * 160–223 = 4 stars when READ with Windows Explorer, writes 196
 * 096-159 = 3 stars when READ with Windows Explorer, writes 128
 * 032-095 = 2 stars when READ with Windows Explorer, writes 64
 * 001-031 = 1 star when READ with Windows Explorer, writes 1
 * 
 * 
 * Vorbis
 * =======================================
 *  
 *  Ratings are usually mapped as 1-5 stars with 20,40,60,80,100 as the actual string values.
 * 
 * </pre>
 * 
 */
public class SongIndexed
{
    private String filePath;
    private String acoustID;
    private String musicBrainzID;
    private int rating;

    public String getFilename()
    {
        return FilenameUtils.getName(filePath);
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    public String getAcoustID()
    {
        return acoustID;
    }

    public void setAcoustID(String acoustID)
    {
        this.acoustID = acoustID;
    }

    public String getMusicBrainzID()
    {
        return musicBrainzID;
    }

    public void setMusicBrainzID(String musicBrainzID)
    {
        this.musicBrainzID = musicBrainzID;
    }

    public int getRating()
    {
        return rating;
    }

    public void setRating(int rating)
    {
        this.rating = rating;
    }

    public String getRatingForTag(Tag tag)
    {
        int num = 0;
        if (tag instanceof FlacTag || tag instanceof VorbisCommentTag)
        {
            num = convertStarsToVorbis();
        }
        else if (tag instanceof AbstractID3v2Tag || tag instanceof ID3v11Tag)
        {
            num = convertStarsToID3();
        }
        else
        {
            // Dont't know ... maybe we use vorbis tags by default
            num = convertStarsToVorbis();
        }

        return "" + num;
    }

    private int convertStarsToID3()
    {
        if (rating == 0)
        {
            return 0;
        }
        else if (rating == 1)
        {
            return 1;
        }
        else if (rating == 2)
        {
            return 64;
        }
        else if (rating == 3)
        {
            return 128;
        }
        else if (rating == 4)
        {
            return 196;
        }
        else
        {
            return 255;
        }
    }

    private int convertStarsToVorbis()
    {
        return rating * 20;
    }

    public void setRatingFromTag(Tag tag)
    {
        String value = tag.getFirst(FieldKey.RATING);
        if (!StringUtils.isBlank(value))
        {
            int num = Integer.parseInt(value);
            if (tag instanceof FlacTag || tag instanceof VorbisCommentTag)
            {
                convertVorbisToStars(num);
            }
            else if (tag instanceof AbstractID3v2Tag || tag instanceof ID3v11Tag)
            {
                convertID3ToStars(num);
            }
            else
            {
                // Dont't know ... maybe we use vorbis tags by default
                convertVorbisToStars(num);
            }
        }
    }

    private void convertID3ToStars(int num)
    {
        if (num == 0)
        {
            rating = 0;
        }
        else if (num < 32)
        {
            rating = 1;
        }
        else if (num < 96)
        {
            rating = 2;
        }
        else if (num < 160)
        {
            rating = 3;
        }
        else if (num < 224)
        {
            rating = 4;
        }
        else
        {
            rating = 5;
        }
    }

    private void convertVorbisToStars(int num)
    {
        if (num == 0)
        {
            rating = 0;
        }
        else if (num < 21)
        {
            rating = 1;
        }
        else if (num < 41)
        {
            rating = 2;
        }
        else if (num < 61)
        {
            rating = 3;
        }
        else if (num < 81)
        {
            rating = 4;
        }
        else
        {
            rating = 5;
        }
    }
}
