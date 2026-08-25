package nextcp.rest;

import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jupnp.support.contentdirectory.DIDLParser;
import org.jupnp.support.model.DIDLContent;
import org.jupnp.support.model.DIDLObject;
import org.jupnp.support.model.DIDLObject.Property;
import org.jupnp.support.model.DIDLAttribute;
import org.jupnp.support.model.DescMeta;
import org.jupnp.support.model.PersonWithRole;
import org.jupnp.support.model.Res;
import org.jupnp.support.model.container.MusicAlbum;
import org.jupnp.support.model.container.MusicArtist;
import org.jupnp.support.model.item.AudioItem;
import org.jupnp.support.model.item.Item;
import org.jupnp.support.model.item.MusicTrack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import nextcp.dto.AudioFormat;
import nextcp.dto.ContainerDto;
import nextcp.dto.DiscogsId;
import nextcp.dto.MediaRendererDto;
import nextcp.dto.MediaServerDto;
import nextcp.dto.MusicBrainzId;
import nextcp.dto.MusicItemDto;
import nextcp.dto.MusicItemIdDto;
import nextcp.dto.UpnpAvTransportState;
import nextcp.spotify.SpotifyArtistService;
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

    /**
     * Generates XML MEtadata
     */
    private DIDLParser didlParser = new DIDLParser();

    public UpnpAvTransportState buildAvTransportStateDto(AvTransportState stateVariable, MediaRendererDevice rendererDevice)
    {
        UpnpAvTransportState stateDto = new UpnpAvTransportState();

        stateDto.mediaRenderer = rendererDevice.getAsDto();
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

    /**
     * 
     * @param xml
     * @return Empty MusicItemDto if xml is not supplied, otherwise DIDL Object is parsed and mapped.
     */
    public MusicItemDto extractXmlAsMusicItem(String xml)
    {
        if (StringUtils.isBlank(xml) || "NOT_IMPLEMENTED".equalsIgnoreCase(xml))
        {
            return new MusicItemDto();
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
            log.info(String.format("cannot create didl item with content '%s'. ErrorMessage : %s", xml, e.getMessage()), e);
        }

        return null;
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
                if (log.isDebugEnabled() && StringUtils.isEmpty(container.getDate()))
                {
                    log.debug("dc:date is empty for title : " + container.getTitle());
                }
            }
            if (container.getGenres().length > 0)
            {
                dto.genre = container.getFirstGenre();
            }

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
        Optional<Property<?>> uri = extractProperty("albumArtURI", container.getProperties());
        if (uri.isPresent())
        {
            dto.albumartUri = uri.get().toString();
        }
        dto.albumartUriMedium = selectMediumAlbumArtUri(container);

        // The user rating is supported by any kind of resource, containers as well as items.
        Optional<Property<?>> rating = extractProperty("rating", container.getProperties());
        if (rating.isPresent())
        {
            addRating(dto, rating.get().getValue().toString());
        }

        // Add music album infos
        if (container instanceof MusicAlbum)
        {
            addMusicAlbum((MusicAlbum) container, dto);
        }

        // Read artists image from Spotify. This is an optional enrichment and must never break
        // the search result, so any failure (e.g. Spotify not configured) is swallowed here.
        if (container instanceof MusicArtist)
        {
            try
            {
                String url = spotifyArtistService.getArtistImageUrlByName(container.getTitle());
                if (!StringUtils.isBlank(url))
                {
                    dto.albumartUri = url;
                }
            }
            catch (Exception e)
            {
                log.warn("Could not resolve artist image for '{}': {}", container.getTitle(), e.getMessage());
            }
        }

        // apply some defaults
        if (dto.albumartUri == null)
        {
            dto.albumartUri = ASSET_FOLDER + "/images/directory-icon.png";
        }
        return dto;
    }

    public Optional<Property<?>> extractProperty(String name, List<Property<?>> list)
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
        	
        extractKnownProperties(itemDto, item);
        extractAudioFormat(item, itemDto);

        if (item instanceof AudioItem)
        {
            itemDto.songId = new MusicItemIdDto();
            itemDto.songId.objectID = item.getId();
            addAudioItem((AudioItem) item, itemDto);
            extractDescMetadata(itemDto, item);
        }

        if (item instanceof MusicTrack)
        {
            addMusicTrack((MusicTrack) item, itemDto);
        }
        if (StringUtils.isAllBlank(itemDto.albumArtUrl)) {
        	log.debug("no track selected. Giving default img src-path");
        	itemDto.albumArtUrl = "/assets/images/folder-bg.png";
        }
        
        return itemDto;
    }

    private void extractKnownProperties(MusicItemDto itemDto, Item item)
    {
        itemDto.streamingURL = readStreamingUrl(item);
        // The bigger variants are separate fields: browse grids show hundreds of tiles and keep using
        // the small albumArtUrl (the medium one only on high density displays), while the now playing
        // view asks for the big cover.
        itemDto.albumArtUrlLarge = selectLargeAlbumArtUri(item);
        itemDto.albumArtUrlMedium = selectMediumAlbumArtUri(item);

        for (Property<?> property : item.getProperties())
        {
            switch (property.getDescriptorName())
            {
                case "artist":
                    if (property.getValue() instanceof PersonWithRole r)
                    {
                        if ("composer".equalsIgnoreCase(r.getRole()))
                        {
                            itemDto.composer = r.getName();
                        }
                        else if ("conductor".equalsIgnoreCase(r.getRole()))
                        {
                            itemDto.conductor = r.getName();
                        }
                        else
                        {
                            itemDto.artistName = r.getName();
                        }
                    }
                    else
                    {
                        log.debug("author not handled properly ! " + property.getDescriptorName() + " : " + property.getValue());
                    }
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
                    break;
                case "date":
                    itemDto.date = property.getValue().toString();
                    break;
                case "rating":
                    addRating(itemDto, property.getValue().toString());
                    break;
                case "author":
                    if (property.getValue() instanceof PersonWithRole r)
                    {
                        if ("composer".equalsIgnoreCase(r.getRole()))
                        {
                            itemDto.composer = r.getName();
                        }
                        else if ("conductor".equalsIgnoreCase(r.getRole()))
                        {
                            itemDto.conductor = r.getName();
                        }
                    }
                    else
                    {
                        log.debug("author not handled properly ! " + property.getDescriptorName() + " : " + property.getValue());
                    }
                    break;
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
        itemDto.musicBrainzId = mb;
        DiscogsId discogsId = new DiscogsId();
        itemDto.discogsId = discogsId;

        
        // 
        // tags given by MPD server
        // 
        Optional<DescMeta<?>> descMetadata = item.getDescMetadata().stream().filter(n -> n.getType() != null && n.getType().equalsIgnoreCase("mpd-tags")).findFirst();
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
                    	itemDto.songId.musicBrainzIdTrackId = n.getTextContent();
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
        descMetadata = item.getDescMetadata().stream().filter(n -> n.getType() != null && n.getType().equalsIgnoreCase("ums-tags")).findFirst();
        if (descMetadata.isPresent())
        {
            Node metaChildNodes = ((Node) descMetadata.get().getMetadata()).getFirstChild();
            for (int i = 0; i < metaChildNodes.getChildNodes().getLength(); i++)
            {
                Node n = metaChildNodes.getChildNodes().item(i);
                String nodeName = n.getNodeName().toLowerCase();
                switch (nodeName)
                {
                    case "resourceid":
                        itemDto.resourceId = getTextAndCheckForNull(n);
                        log.debug("resourceId : " + itemDto.resourceId);
                        break;
                    case "musicbrainztrackid":
                        itemDto.songId.musicBrainzIdTrackId = getTextAndCheckForNull(n);
                        log.debug("musicbrainztrackid : " + itemDto.songId.musicBrainzIdTrackId);
                        break;
                    case "musicbrainzreleaseid":
                        mb.ReleaseTrackId = getTextAndCheckForNull(n);
                        log.debug("musicbrainzreleaseid : " + mb.ReleaseTrackId);
                        break;
                    case "discogsreleaseid":
                    	try {
                    		itemDto.discogsId.ReleaseId = Long.parseLong(getTextAndCheckForNull(n));
                    		log.debug("discogsreleaseid : " + mb.ReleaseTrackId);
                    	} catch (Exception e) {
                    		log.warn("cannot parse discogs release id : {}", getTextAndCheckForNull(n), e);
                    	}
                        break;
                    case "numberofthisdisc":
                        itemDto.numberOfThisDisc = getTextAndCheckForNull(n);
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
                    case "audiotrackid":
                        log.debug("ignoring audiotrackid");
                        break;
                    case "audioaddictchannelid":
                        try
                        {
                            String channelId = getTextAndCheckForNull(n);
                            if (channelId != null)
                            {
                                itemDto.audioAddictChannelId = Integer.valueOf(channelId);
                                log.debug("audioaddictchannelid : {}", itemDto.audioAddictChannelId);
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            log.warn("cannot parse audioaddictchannelid : {}", getTextAndCheckForNull(n));
                        }
                        break;
                    case "audioaddictnetwork":
                        itemDto.audioAddictNetwork = getTextAndCheckForNull(n);
                        log.debug("audioaddictnetwork : {}", itemDto.audioAddictNetwork);
                        break;
                    case "audioaddictplaylistid":
                        try
                        {
                            String playlistId = getTextAndCheckForNull(n);
                            if (playlistId != null)
                            {
                                itemDto.audioAddictPlaylistId = Integer.valueOf(playlistId);
                                log.debug("audioaddictplaylistid : {}", itemDto.audioAddictPlaylistId);
                            }
                        }
                        catch (NumberFormatException e)
                        {
                            log.warn("cannot parse audioaddictplaylistid : {}", getTextAndCheckForNull(n));
                        }
                        break;
                    case "rating":
                        try
                        {
                            String strRating = n.getTextContent();
                            addRating(itemDto, strRating);
                        }
                        catch (Exception e)
                        {
                            log.debug("parsing rating information failed", e);
                        }
                        break;
                    default:
                        log.warn("unknown ums-tags attribute : " + n.getNodeName());
                        break;
                }
            }
        }
    }

    private void addRating(MusicItemDto itemDto, String strRating)
    {
        if (NumberUtils.isParsable(strRating))
        {
            itemDto.rating = Integer.parseInt(strRating);
            log.debug("rating : " + strRating);
        }
    }

    private void addRating(ContainerDto containerDto, String strRating)
    {
        if (NumberUtils.isParsable(strRating))
        {
            containerDto.rating = Integer.parseInt(strRating);
            log.debug("container rating : " + strRating);
        }
    }

    /** Preferred DLNA image profiles for the big cover of the now playing view. */
    private static final List<String> LARGE_ART_PROFILES = List.of("JPEG_LRG", "JPEG_MED", "JPEG_SM", "JPEG_TN");

    /**
     * Preferred DLNA image profiles for a grid tile on a high density display. A tile is only ~130-250
     * CSS px wide, so the mid sized profiles fit it best; a large one is still preferable to a 160x160
     * thumbnail, which such a display would visibly upscale.
     */
    private static final List<String> MEDIUM_ART_PROFILES = List.of("JPEG_MED", "JPEG_SM", "JPEG_LRG", "JPEG_TN");

    /**
     * Matches the DLNA image profile inside a UMS thumbnail URL, e.g.
     * {@code http://host:5001/get/0$1$5/thumbnail0000JPEG_TN_cover.jpg}. UMS embeds the requested
     * profile in the path and renders that size on demand, so asking for a bigger one just works.
     */
    private static final Pattern UMS_THUMBNAIL_PROFILE = Pattern.compile("(/thumbnail0000)JPEG_(?:TN|SM|MED|LRG)_");

    /**
     * Album art URI in the largest size available, or {@code null} when the object carries none. A media
     * server offers the same cover in several sizes, each as its own {@code upnp:albumArtURI} with a
     * {@code dlna:profileID}; the small one leaves the now playing view scaling up a 160x160 JPEG_TN
     * thumbnail. Only used for the single cover the now playing view shows - browse grids keep the small
     * {@code albumArtUrl}, which is what makes a listing of a thousand tiles cheap.
     */
    String selectLargeAlbumArtUri(DIDLObject didlObject)
    {
        return selectAlbumArtUri(didlObject, LARGE_ART_PROFILES, "JPEG_LRG");
    }

    /**
     * Album art URI in a mid size, for grid tiles on displays with a device pixel ratio of 2 or more.
     * Same selection as {@link #selectLargeAlbumArtUri(DIDLObject)}, only aiming at a smaller profile:
     * a wall of a thousand tiles must not pull full size covers.
     */
    String selectMediumAlbumArtUri(DIDLObject didlObject)
    {
        return selectAlbumArtUri(didlObject, MEDIUM_ART_PROFILES, "JPEG_MED");
    }

    private String selectAlbumArtUri(DIDLObject didlObject, List<String> preference, String umsProfile)
    {
        String bestUri = null;
        int bestRank = Integer.MAX_VALUE;
        for (Property<?> property : didlObject.getProperties())
        {
            if (!"albumArtURI".equals(property.getDescriptorName()) || property.getValue() == null)
            {
                continue;
            }
            String uri = property.getValue().toString();
            if (StringUtils.isBlank(uri))
            {
                continue;
            }
            String profile = albumArtProfileOf(property);
            // An URI without a (known) profile ranks last, but is still used when it is all we get.
            // Note List.of() throws on indexOf(null), and a server may well omit the profile.
            int rank = profile != null ? preference.indexOf(profile) : -1;
            rank = rank < 0 ? preference.size() : rank;
            if (bestUri == null || rank < bestRank)
            {
                bestUri = uri;
                bestRank = rank;
            }
            log.trace("album art candidate: profile={}, uri={}", profile, uri);
        }
        return withUmsThumbnailProfile(bestUri, umsProfile);
    }

    /** Value of the {@code dlna:profileID} attribute of an albumArtURI property, or {@code null}. */
    private String albumArtProfileOf(Property<?> albumArtUri)
    {
        Property<DIDLAttribute> profile = albumArtUri.getAttribute("profileID");
        return profile != null && profile.getValue() != null ? profile.getValue().getValue() : null;
    }

    /**
     * Rewrites a UMS thumbnail URL to the given DLNA image profile. UMS advertises the cover in one
     * profile only, depending on which renderer profile matches, but it renders whatever profile the
     * URL names - so asking for JPEG_LRG yields the full size cover instead of an upscaled thumbnail,
     * and JPEG_MED a tile sized one. URLs of other servers do not match the pattern and are returned
     * untouched.
     */
    String withUmsThumbnailProfile(String albumArtUri, String profile)
    {
        if (albumArtUri == null)
        {
            return null;
        }
        Matcher matcher = UMS_THUMBNAIL_PROFILE.matcher(albumArtUri);
        if (!matcher.find())
        {
            return albumArtUri;
        }
        String rewritten = matcher.replaceFirst("$1" + profile + "_");
        if (!rewritten.equals(albumArtUri))
        {
            log.debug("album art rewritten to profile {}: {} -> {}", profile, albumArtUri, rewritten);
        }
        return rewritten;
    }

    private String getTextAndCheckForNull(Node n)
    {
        return n.getTextContent().equals("null") ? null : n.getTextContent();
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
        	// TODO pick best "res" for streaming !!!
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
        if (resUrl.isPresent())
        {
            return resUrl.get().getValue();
        }

        // Nothing the server declared as audio. Web radio entries of a server side playlist regularly
        // announce something generic like application/octet-stream, and giving up here left the item
        // without a stream URL - which the UI can only answer by doing nothing at all. The item is an
        // audio item by its upnp:class, so its first resource is the audio; log what had to be
        // accepted, because a wrong guess here is worth seeing.
        Optional<Res> fallback = item.getResources().stream()
                .filter(res -> StringUtils.isNotBlank(res.getValue()))
                .findFirst();
        if (fallback.isPresent())
        {
            log.info("no resource declared as audio for item '{}'; using its first resource instead (content format: {})",
                    item.getTitle(), contentFormatOf(fallback.get()));
            return fallback.get().getValue();
        }

        log.debug(String.format("Empty URL for item : %s ", item.getTitle()));
        return "";
    }

    private boolean isAudioResource(Res res)
    {
        String contentFormat = contentFormatOf(res);
        if (contentFormat == null)
        {
            return false;
        }
        if (contentFormat.startsWith("audio"))
        {
            return true;
        }
        if (contentFormat.startsWith("MIMETYPE_AUTO"))
        {
            return true; // UMS unknown renderer ... maybe it should be fixed in UMS ?
        }

        return false;
    }

    /** Content format of a resource, or {@code null} when the server did not announce one. */
    private String contentFormatOf(Res res)
    {
        return res.getProtocolInfo() != null ? res.getProtocolInfo().getContentFormat() : null;
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
    	if (StringUtils.isBlank(item.getId())) {
    		return "";
    	}
        DIDLContent c = new DIDLContent();
        c.addItem(item);
        try
        {
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
        if (res.getProtocolInfo() != null && res.getProtocolInfo().getContentFormat().startsWith("audio")) {
	        af = new AudioFormat();
	        af.bitrate = res.getBitrate();
	        af.bitsPerSample = res.getBitsPerSample();
	        af.nrAudioChannels = res.getNrAudioChannels();
	        af.sampleFrequency = res.getSampleFrequency();
	        af.durationDisp = res.getDuration();
	        af.size = res.getSize();
	        if (af.size != null) {
	        	if (af.size <= 1 || af.size > Integer.MAX_VALUE) {
	        		af.isStreaming = true;
	        	} else {
	        		af.isStreaming = false;
	        	}
	        }
	        if (res.getProtocolInfo() != null)
	        {
	            af.contentFormat = res.getProtocolInfo().getContentFormat();
	        }
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
            mediaRenderer.add(device.getAsDto());
        }
        return mediaRenderer;
    }

    public List<MediaServerDto> getMediaServerAsDto(Collection<MediaServerDevice> mediaServerDevices)
    {
        List<MediaServerDto> mediaServer = new ArrayList<>();
        for (MediaServerDevice device : mediaServerDevices)
        {
            mediaServer.add(device.getAsDto());
        }
        return mediaServer;
    }
}
