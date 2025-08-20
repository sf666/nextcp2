package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class Config
{

    public ApplicationConfig applicationConfig;
    public List<RadioStation> radioStation;
    public MusicbrainzSupport musicbrainzSupport;
    public String lastFmSessionKey;
    public SpotifyConfigDto spotifyConfig;
    public MediaPlayerConfigDto mediaPlayerConfig;
    public AudioAddictConfig audioAddictConfig;

    public Config()
    {
    }

    public Config(ApplicationConfig applicationConfig, List<RadioStation> radioStation, MusicbrainzSupport musicbrainzSupport, String lastFmSessionKey, SpotifyConfigDto spotifyConfig, MediaPlayerConfigDto mediaPlayerConfig, AudioAddictConfig audioAddictConfig)
    {
        this.applicationConfig = applicationConfig;
        this.radioStation = radioStation;
        this.musicbrainzSupport = musicbrainzSupport;
        this.lastFmSessionKey = lastFmSessionKey;
        this.spotifyConfig = spotifyConfig;
        this.mediaPlayerConfig = mediaPlayerConfig;
        this.audioAddictConfig = audioAddictConfig;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Config [");
        sb.append("applicationConfig=").append(this.applicationConfig).append(", ");
        sb.append("radioStation=").append(this.radioStation).append(", ");
        sb.append("musicbrainzSupport=").append(this.musicbrainzSupport).append(", ");
        sb.append("lastFmSessionKey=").append(this.lastFmSessionKey).append(", ");
        sb.append("spotifyConfig=").append(this.spotifyConfig).append(", ");
        sb.append("mediaPlayerConfig=").append(this.mediaPlayerConfig).append(", ");
        sb.append("audioAddictConfig=").append(this.audioAddictConfig).append(", ");
        sb.append("]");
        return sb.toString();
    }

}