package nextcp.rest;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.fourthline.cling.support.contentdirectory.DIDLParser;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.DIDLObject.Property;
import org.fourthline.cling.support.model.DescMeta;
import org.fourthline.cling.support.model.Res;
import org.fourthline.cling.support.model.container.MusicAlbum;
import org.fourthline.cling.support.model.container.MusicArtist;
import org.fourthline.cling.support.model.item.AudioItem;
import org.fourthline.cling.support.model.item.Item;
import org.fourthline.cling.support.model.item.MusicTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import nextcp.dto.AudioFormat;
import nextcp.dto.ContainerDto;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.MediaServerDto;
import nextcp.dto.MusicBrainzId;
import nextcp.dto.MusicItemDto;
import nextcp.dto.MusicItemIdDto;
import nextcp.dto.UpnpAvTransportState;
import nextcp.service.RatingService;
import nextcp.spotify.SpotifyArtistService;
import nextcp.upnp.device.BaseDevice;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.avtransport.AvTransportState;
import nextcp.upnp.device.mediaserver.MediaServerDevice;

@Service
public class DtoBuilder
{
    private static final Logger log = LoggerFactory.getLogger(DtoBuilder.class.getName());

    public static final String ASSET_FOLDER = "assets";

    private SimpleDateFormat dispParse = new SimpleDateFormat("HH:mm:ss.SSS Z");

    @Autowired
    private SpotifyArtistService spotifyArtistService = null;

    @Autowired
    private RatingService ratingService = null;

    /**
     * Generates XML MEtadata
     */
    private DIDLParser didlParser = new DIDLParser();

    /**
     * Builds DTOs
     * 
     * @param device
     * 
     * @return DTO
     */
    public MediaServerDto buildMediaServerDevice(BaseDevice device)
    {
        MediaServerDto dto = new MediaServerDto();
        dto.friendlyName = device.getFriendlyName();
        dto.udn = device.getUDN().getIdentifierString();
        return dto;
    }

    public UpnpAvTransportState buildAvTransportStateDto(AvTransportState stateVariable, MediaRendererDevice rendererDevice)
    {
        UpnpAvTransportState stateDto = new UpnpAvTransportState();

        stateDto.mediaRenderer = buildMediaRendererDevice(rendererDevice);
        stateDto.AbsoluteTimePosition = stateVariable.AbsoluteTimePosition;
        stateDto.CurrentTrackURI = stateVariable.CurrentTrackURI;
        stateDto.RelativeCounterPosition = stateVariable.RelativeCounterPosition;
        stateDto.TransportStatus = stateVariable.TransportStatus;
        stateDto.TransportState = stateVariable.TransportState;
        stateDto.CurrentTrack = stateVariable.CurrentTrack;
        stateDto.PlaybackStorageMedium = stateVariable.PlaybackStorageMedium;
        stateDto.PossibleRecordQualityModes = stateVariable.PossibleRecordQualityModes;
        stateDto.NextAVTransportURIMetaData = stateVariable.NextAVTransportURIMetaData;
        stateDto.NumberOfTracks = stateVariable.NumberOfTracks;
        stateDto.CurrentMediaDuration = stateVariable.CurrentMediaDuration;
        stateDto.NextAVTransportURI = stateVariable.NextAVTransportURI;
        stateDto.RecordStorageMedium = stateVariable.RecordStorageMedium;
        stateDto.AVTransportURI = stateVariable.AVTransportURI;
        stateDto.TransportPlaySpeed = stateVariable.TransportPlaySpeed;
        stateDto.AbsoluteCounterPosition = stateVariable.AbsoluteCounterPosition;
        stateDto.RelativeTimePosition = stateVariable.RelativeTimePosition;
        stateDto.CurrentPlayMode = stateVariable.CurrentPlayMode;
        stateDto.CurrentTrackDuration = stateVariable.CurrentTrackDuration;
        stateDto.PossiblePlaybackStorageMedia = stateVariable.PossiblePlaybackStorageMedia;
        stateDto.CurrentRecordQualityMode = stateVariable.CurrentRecordQualityMode;
        stateDto.RecordMediumWriteStatus = stateVariable.RecordMediumWriteStatus;
        stateDto.CurrentTransportActions = stateVariable.CurrentTransportActions;
        stateDto.PossibleRecordStorageMedia = stateVariable.PossibleRecordStorageMedia;
        stateDto.CurrentTrackMetaData = extractXmlAsMusicItem(stateVariable.CurrentTrackMetaData);
        stateDto.AVTransportURIMetaData = extractXmlAsMusicItem(stateVariable.AVTransportURIMetaData);

        return stateDto;
    }

    public MusicItemDto extractXmlAsMusicItem(String xml)
    {
        if (StringUtils.isBlank(xml) || "NOT_IMPLEMENTED".equalsIgnoreCase(xml))
        {
            return null;
        }

        if (xml.startsWith("&lt;"))
        {
            xml = StringEscapeUtils.unescapeXml(xml);
        }
        try
        {
            if (!xml.contains("<upnp:class"))
            {
                log.debug("fixing missing upnp:class element in DIDL object ...");
                xml = xml.replace("<item>", "<item>\n<upnp:class>" + "object.item.audioItem.musicTrack" + "</upnp:class>");
            }
            DIDLContent didlMeta = generateDidlContent(xml);
            MusicItemDto itemDto = buildItemDto(didlMeta.getItems().get(0), "");
            return itemDto;
        }
        catch (Exception e)
        {
            log.info(String.format("cannot create didl item with content '%s'. ErrorMessage : %s", xml, e.getMessage()));
        }

        return null;
    }


    public MediaRendererDto buildMediaRendererDevice(MediaRendererDevice device)
    {
        MediaRendererDto dto = new MediaRendererDto();
        dto.friendlyName = device.getFriendlyName();
        dto.udn = device.getUDN().getIdentifierString();
        return dto;
    }

    private ContainerDto addMusicAlbum(MusicAlbum container, ContainerDto dto)
    {
        dto.creator = container.getCreator();
        try
        {
            if (container.getAlbumArtURIs().length > 0)
            {
                dto.albumartUri = container.getAlbumArtURIs()[0].toURL().toString();
            }
            if (container.getArtists().length > 0)
            {
                dto.artist = container.getFirstArtist().getName();
            }
            if (container.getDate() != null)
            {
                dto.media_date = container.getDate();
            }
            if (container.getGenres().length > 0)
            {
                dto.genre = container.getFirstGenre();
            }
            // TODO extracting more info's necessary ... Role, etc. ?
        }
        catch (MalformedURLException e)
        {
            log.warn("album art uri error", e);
        }
        return dto;
    }

    public ContainerDto buildContainerDto(DIDLObject container)
    {
        ContainerDto dto = new ContainerDto();
        dto.title = container.getTitle();
        dto.id = container.getId();
        dto.parentID = container.getParentID();
        dto.objectClass = container.getClazz().getValue();

        // Add album art in any case if possible
        Optional<Property> uri = extractProperty("albumArtURI", container.getProperties());
        if (uri.isPresent())
        {
            dto.albumartUri = uri.get().toString();
        }

        // Add music album infos
        if (container instanceof MusicAlbum)
        {
            addMusicAlbum((MusicAlbum) container, dto);
        }

        // Read artists image from Spotify
        if (container instanceof MusicArtist)
        {
            String url = spotifyArtistService.getArtistImageUrlByName(container.getTitle());
            if (!StringUtils.isBlank(url))
            {
                dto.albumartUri = url;
            }
        }

        // apply some defaults
        if (dto.albumartUri == null)
        {
            dto.albumartUri = ASSET_FOLDER + "/images/directory-icon.png";
        }
        return dto;
    }

    public Optional<Property> extractProperty(String name, List<Property> list)
    {
        return list.stream().filter(p -> p.getDescriptorName().equalsIgnoreCase(name)).findFirst();
    }

    public MusicItemDto buildItemDto(Item item, String mediaServerUdn)
    {
        MusicItemDto itemDto = new MusicItemDto();
        itemDto.musicBrainzId = new MusicBrainzId();
        itemDto.title = item.getTitle();
        itemDto.parentId = item.getParentID();
        itemDto.objectClass = item.getClazz().getValue();
        itemDto.objectID = item.getId();
        itemDto.refId = item.getRefID();
        itemDto.mediaServerUDN = mediaServerUdn;

        extractDescMetadata(itemDto, item);

        extractKnownProperties(itemDto, item);
        extractAudioFormat(item, itemDto);

        if (item instanceof AudioItem)
        {
            addAudioItem((AudioItem) item, itemDto);
        }

        if (item instanceof MusicTrack)
        {
            addMusicTrack((MusicTrack) item, itemDto);
        }
        return itemDto;
    }

    private void extractKnownProperties(MusicItemDto itemDto, Item item)
    {
        itemDto.streamingURL = readStreamingUrl(item);

        for (Property<?> property : item.getProperties())
        {
            switch (property.getDescriptorName())
            {
                case "artist":
                    itemDto.artistName = property.getValue().toString();
                    break;
                case "album":
                    itemDto.album = property.getValue().toString();
                    break;
                case "albumArtURI":
                    itemDto.albumArtUrl = property.getValue().toString();
                    break;
                case "originalTrackNumber":
                    itemDto.originalTrackNumber = property.getValue().toString();
                    break;
                case "genre":
                    itemDto.genre = property.getValue().toString();
                case "date":
                    itemDto.date = property.getValue().toString();
                default:
                    log.debug("unprocessed property : " + property.getDescriptorName() + " : " + property.getValue());
            }
        }
    }

    // itemDto.musicBrainzId
    private void extractDescMetadata(MusicItemDto itemDto, Item item)
    {
        extractMusicBrainzId(itemDto, item);
    }

    private void extractMusicBrainzId(MusicItemDto itemDto, Item item)
    {
        //
        // Support for Mediaplayer Tags (https://petemanchester.github.io/MediaPlayer/)
        //
        MusicBrainzId mb = new MusicBrainzId();
        MusicItemIdDto ids = new MusicItemIdDto();
        itemDto.songId = ids;
        itemDto.musicBrainzId = mb;
        
        Optional<DescMeta> descMetadata = item.getDescMetadata().stream().filter(n -> n.getType().equalsIgnoreCase("mpd-tags")).findFirst();
        if (descMetadata.isPresent())
        {
            Node metaChildNodes = ((Node) descMetadata.get().getMetadata()).getFirstChild();
            for (int i = 0; i < metaChildNodes.getChildNodes().getLength(); i++)
            {
                Node n = metaChildNodes.getChildNodes().item(i);
                switch (n.getNodeName().toLowerCase())
                {
                    case "musicbrainzidalbumid":
                        mb.AlbumId = n.getTextContent();
                        break;
                    case "musicbrainzidartistid":
                        mb.ArtistId = n.getTextContent();
                        break;
                    case "musicbrainzidalbumartistid":
                        mb.AlbumArtistId = n.getTextContent();
                        break;
                    case "musicbrainzidreleasetrackid":
                        mb.ReleaseTrackId = n.getTextContent();
                        break;
                    case "musicbrainzidworkid":
                        mb.WorkId = n.getTextContent();
                        break;
                    case "musicbrainzidtrackid":
                        ids.musicBrainzIdTrackId = n.getTextContent();
                        break;
                    default:
                        log.warn("unknown mpd-tags attribute : " + n.getNodeName());
                        break;
                }
            }
        }

        //
        // Support for UniversalMediaServer Tags (https://www.universalmediaserver.com/)
        //
        descMetadata = item.getDescMetadata().stream().filter(n -> n.getType().equalsIgnoreCase("ums-tags")).findFirst();
        if (descMetadata.isPresent())
        {
            Node metaChildNodes = ((Node) descMetadata.get().getMetadata()).getFirstChild();
            for (int i = 0; i < metaChildNodes.getChildNodes().getLength(); i++)
            {
                Node n = metaChildNodes.getChildNodes().item(i);
                String nodeName = n.getNodeName().toLowerCase(); 
                switch (nodeName)
                {
                    case "musicbrainztrackid":
                        ids.musicBrainzIdTrackId = n.getTextContent();
                        log.debug("musicbrainztrackid : " + ids.musicBrainzIdTrackId);
                        break;
                    case "musicbrainzreleaseid":
                        mb.ReleaseTrackId = n.getTextContent();
                        log.debug("musicbrainzreleaseid : " + mb.ReleaseTrackId);
                        break;
                    case "numberofthisdisc":
                        itemDto.numberOfThisDisc = n.getTextContent();
                        log.debug("numberofthisdisc : " + itemDto.numberOfThisDisc);
                        break;
                    case "date":
                        itemDto.date = n.getTextContent();
                        log.debug("date : " + itemDto.date);
                        break;
                    case "genre":
                        itemDto.genre = n.getTextContent();
                        log.debug("genre : " + itemDto.genre);
                        break;
                    case "rating":
                        try
                        {
                            String strRating = n.getTextContent();
                            if (NumberUtils.isParsable(strRating))
                            {
                                itemDto.rating = Integer.parseInt(strRating);
                                log.debug("rating : " + strRating);
                            }
                        }
                        catch (Exception e)
                        {
                            log.debug("parsing rating information failed", e);
                        }
                        break;
                    case "audiotrackid":
                    	try
                    	{
                            String strFileId = n.getTextContent();
                            if (NumberUtils.isParsable(strFileId)) {
                                ids.umsAudiotrackId = Integer.parseInt(strFileId);
                                log.debug("audiotrackId : " + strFileId);
                            }
                    	}
                    	catch (Exception e )
                    	{
                            log.debug("parsing fileid failed", e);
                    	}
                        break;
                    default:
                        log.warn("unknown ums-tags attribute : " + n.getNodeName());
                        break;
                }
            }
        }
    }

    private void addAudioItem(AudioItem item, MusicItemDto itemDto)
    {
        itemDto.creator = item.getCreator();
        itemDto.currentTrackMetadata = generateMetadataFromItem(item);
        if (StringUtils.isBlank(itemDto.albumArtUrl))
        {
            itemDto.albumArtUrl = ASSET_FOLDER + "/images/music-icon.png";
        }
    }

    private void extractAudioFormat(Item item, MusicItemDto itemDto)
    {
        for (Res res : item.getResources())
        {
            AudioFormat format = extractAudioFormatFromResourceField(res);
            if (format != null)
            {
                itemDto.audioFormat = format;
            }
            String url = res.getValue();
            if (!StringUtils.isBlank(url) && isAudioResource(res))
            {
                itemDto.streamingURL = url;
            }
        }
    }

    public String readStreamingUrl(Item item)
    {
        Optional<Res> resUrl = item.getResources().stream().filter(res -> isAudioResource(res)).findFirst();
        if (!resUrl.isEmpty())
        {
            return resUrl.get().getValue();
        }
        else
        {
            log.debug(String.format("Empty URL for ID : %s ", item.getId()));
            return "";
        }
    }

    private boolean isAudioResource(Res res)
    {
        if (res.getProtocolInfo().getContentFormat().startsWith("audio"))
        {
            return true;
        }
        if (res.getProtocolInfo().getContentFormat().startsWith("MIMETYPE_AUTO"))
        {
            // UMS unknown renderer
            return true;
        }

        return false;
    }

    public void addMusicTrack(MusicTrack item, MusicItemDto itemDto)
    {
        itemDto.album = item.getAlbum();
        if (item.getFirstArtist() != null)
        {
            itemDto.artistName = item.getFirstArtist().getName();
        }
        itemDto.originalTrackNumber = "" + item.getOriginalTrackNumber();

    }

    public String generateMetadataFromItem(AudioItem item)
    {
        DIDLContent c = new DIDLContent();
        c.addItem(item);
        try
        {
            // add some missing data
            item.setId("-1");
            item.setParentID("-1");
            return didlParser.generate(c, false);
        }
        catch (Exception e)
        {
            log.warn("couldn't create metadata.", e);
            return "";
        }
    }

    AudioFormat extractAudioFormatFromResourceField(Res res)
    {
        AudioFormat af = null;
        if (res.getBitrate() != null)
        {
            af = new AudioFormat();
            af.bitrate = res.getBitrate();
            af.bitsPerSample = res.getBitsPerSample();
            af.nrAudioChannels = res.getNrAudioChannels();
            af.sampleFrequency = res.getSampleFrequency();
            af.durationDisp = res.getDuration();
            try
            {
                if (!StringUtils.isBlank(af.durationDisp))
                {
                    String s = normalizeDuration(res.getDuration());
                    af.durationInSeconds = dispParse.parse(s).getTime() / 1000;
                }
            }
            catch (Exception e)
            {
                log.warn("cannot parse duration : " + e.getMessage());
            }
        }
        return af;
    }

    private String normalizeDuration(String duration)
    {
        if (StringUtils.isBlank(duration))
        {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        if (StringUtils.countMatches(duration, ":") == 1)
        {
            sb.append("00:");
        }
        sb.append(duration);
        int idx = duration.indexOf('.');
        if (idx == -1)
        {
            sb.append(".000");
        }
        sb.append(" UTC");
        return sb.toString();
    }

    protected DIDLContent generateDidlContent(String didlContent) throws Exception
    {
        if (StringUtils.isBlank(didlContent))
        {
            log.debug("[generateDidlContent] didlContent is empty.");
            return null;
        }
        DIDLParser didlParser = new DIDLParser();
        DIDLContent didl = didlParser.parse(didlContent);
        return didl;
    }

    public List<MediaRendererDto> getMediaRendererAsDto(Collection<MediaRendererDevice> mediaRendererDevices)
    {
        List<MediaRendererDto> mediaRenderer = new ArrayList<>();
        for (MediaRendererDevice device : mediaRendererDevices)
        {
            mediaRenderer.add(buildMediaRendererDevice(device));
        }
        return mediaRenderer;
    }

    public List<MediaServerDto> getMediaServerAsDto(Collection<MediaServerDevice> mediaServerDevices)
    {
        List<MediaServerDto> mediaServer = new ArrayList<>();
        for (MediaServerDevice device : mediaServerDevices)
        {
            mediaServer.add(buildMediaServerDevice(device));
        }
        return mediaServer;
    }
}
