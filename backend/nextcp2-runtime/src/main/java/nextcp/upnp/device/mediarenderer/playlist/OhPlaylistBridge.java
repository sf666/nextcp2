package nextcp.upnp.device.mediarenderer.playlist;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import nextcp.domainmodel.device.services.IPlaylistService;
import nextcp.dto.ContainerItemDto;
import nextcp.dto.MusicBrainzId;
import nextcp.dto.MusicItemDto;
import nextcp.dto.PlaylistState;
import nextcp.dto.TransportServiceStateDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.OpenHomeUtils;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.PlaylistService;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.PlaylistServiceEventListenerImpl;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.DeleteIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.IdArrayOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.IdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.InsertInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ReadInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ReadListInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ReadListOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.ReadOutput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekIndexInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekSecondAbsoluteInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SeekSecondRelativeInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SetRepeatInput;
import nextcp.upnp.modelGen.avopenhomeorg.playlist1.actions.SetShuffleInput;

/**
 * Devices with Open Home playlist support.
 */
public class OhPlaylistBridge extends PlaylistServiceEventListenerImpl implements IPlaylistService
{
    private static final Logger log = LoggerFactory.getLogger(OhPlaylistBridge.class.getName());

    private PlaylistService playlistService = null;

    private OpenHomeUtils ohUtil = null;

    private LinkedList<Long> playlistIds = new LinkedList<>();
    private CopyOnWriteArrayList<String> playlistUrls = new CopyOnWriteArrayList<>();
    private MediaRendererDevice device = null;
    
    private boolean isInsertingContainerInPlaylist = false;

    public OhPlaylistBridge(PlaylistService playlistService, DtoBuilder dtoBuilder, MediaRendererDevice device)
    {
    	super(device.getDevice());
        this.playlistService = playlistService;
        this.device = device;
        ohUtil = new OpenHomeUtils(dtoBuilder);
    }

    @Override
    public PlaylistState getState()
    {
        PlaylistState dto = new PlaylistState();
        try
        {
            dto.Id = playlistService.id().Value;
        }
        catch (Exception e)
        {
            log.error("read id: ", e);
        }
        dto.ProtocolInfo = playlistService.protocolInfo().Value;
        try
        {
            dto.Repeat = playlistService.repeat().Value;
        }
        catch (Exception e)
        {
            log.error("read repeat: ", e);
        }
        try
        {
            dto.Shuffle = playlistService.shuffle().Value;
        }
        catch (Exception e)
        {
            log.error("read shuffle: ", e);
        }
        dto.TracksMax = playlistService.tracksMax().Value;
        dto.TransportState = playlistService.transportState().Value;

        return dto;
    }

    @Override
    public List<MusicItemDto> getPlaylistItems()
    {
        byte[] ba = playlistService.idArray().Array;
        return convertIdArrayToMusicItemList(ba);
    }

    public List<MusicItemDto> convertIdArrayToMusicItemList(byte[] ba)
    {
        this.playlistIds = ohUtil.convertUintByteArrayToLong(ba);
        ReadListOutput tracks = readList(ohUtil.makeStringList(this.playlistIds));
        List<MusicItemDto> musicList = ohUtil.convertToMediaItemDto(tracks.TrackList, "TrackList");
        playlistUrls.clear();
        for (MusicItemDto musicItemDto : musicList)
        {
            playlistUrls.add(musicItemDto.streamingURL);
			log.debug("[convertIdArrayToMusicItemList] {} " , musicItemDto.title);
        }
        return musicList;
    }

    public long insert(InsertInput inp)
    {
        if (playlistUrls.contains(inp.Uri))
        {
        	log.debug("{} is already in playlist.");
        }

        return playlistService.insert(inp).NewId;
    }

    @Override
    public void seekSecondAbsolute(long sec)
    {
        SeekSecondAbsoluteInput inp = new SeekSecondAbsoluteInput();
        inp.Value = sec;
        playlistService.seekSecondAbsolute(inp);
    }

    /**
     * Do not use this method. Use seekId instead.
     * 
     * @param index
     */
    public void seekIndex(long index)
    {
        SeekIndexInput inp = new SeekIndexInput();
        inp.Value = index;
        playlistService.seekIndex(inp);
    }

    public ReadOutput read(long id)
    {
        ReadInput inp = new ReadInput();
        inp.Id = id;
        ReadOutput ro = playlistService.read(inp);
        return ro;
    }

    @Override
    public void seekId(long id)
    {
        SeekIdInput inp = new SeekIdInput();
        inp.Value = id;
        playlistService.seekId(inp);
    }

    @Override
    public boolean seekId(String streamUrl)
    {
        log.debug("Seeking to musicItem with URL of : " + streamUrl);
        int listIndex = playlistUrls.indexOf(streamUrl);
        if (listIndex > -1)
        {
            log.debug("index is : " + listIndex);
            Long arrayId = playlistIds.get(listIndex);
            if (arrayId != null)
            {
                log.debug("seeking to id : " + arrayId);
                seekId(arrayId);
                return true;
            }
            else
            {
                log.warn("couldn't acquire song's plalyist index.");
                return false;
            }
        }
        else
        {
            log.warn("song is not in playlist");
            return false;
        }
    }

    public ReadListOutput readList(String idList)
    {
        ReadListInput inp = new ReadListInput();
        inp.IdList = idList;
        ReadListOutput out = playlistService.readList(inp);
        return out;
    }

    @Override
    public void seekSecondRelative(int sec)
    {
        SeekSecondRelativeInput inp = new SeekSecondRelativeInput();
        inp.Value = sec;
        playlistService.seekSecondRelative(inp);
    }

    @Override
    public void setShuffle(boolean shuffle)
    {
        SetShuffleInput inp = new SetShuffleInput();
        inp.Value = shuffle;
        playlistService.setShuffle(inp);
    }

    @Override
    public void pause()
    {
        playlistService.pause();
    }

    @Override
    public void stop()
    {
        playlistService.stop();
    }

    @Override
    public long getTracksMax()
    {
        return playlistService.tracksMax().Value;
    }

    @Override
    public boolean getShuffle()
    {
        return playlistService.shuffle().Value;
    }

    @Override
    public void deleteAll()
    {
    	IdOutput id = playlistService.id();
    	if (id.Value != 0) {
    		log.info("[deleteAll] deleting device playlist ...");
            playlistService.deleteAll();
    	} else {
    		log.info("[deleteAll] device has already an empty playlist.");
    	}
    }

    @Override
    public boolean getRepeat()
    {
        return playlistService.repeat().Value;
    }

    @Override
    public void deleteId(long id)
    {
        DeleteIdInput inp = new DeleteIdInput();
        inp.Value = id;
        playlistService.deleteId(inp);
    }

    @Override
    public void play()
    {
    	log.info("start playing playlist at position : {} ", playlistService.id().Value);
        playlistService.play();        
    }

    @Override
    public void next()
    {
    	log.info("next ... ");
        playlistService.next();
    }

    @Override
    public void setRepeat(boolean repeat)
    {
        SetRepeatInput inp = new SetRepeatInput();
        inp.Value = repeat;
        playlistService.setRepeat(inp);
    }

    @Override
    public void previous()
    {
    	log.info("previous ... ");
        playlistService.previous();
    }

    public MusicItemDto extractMusicItem(Node node)
    {
        MusicItemDto dto = new MusicItemDto();
        dto.musicBrainzId = new MusicBrainzId();
        dto.objectID = extractValue("Id", node);
        dto.streamingURL = extractValue("Uri", node);
        dto.currentTrackMetadata = extractValue("Metadata", node);

        return dto;
    }

    public String extractValue(String attribute, Node node) // Id
    {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try
        {
            Node resultNode = (Node) xpath.evaluate(attribute, node, XPathConstants.NODE);
            return resultNode.getTextContent();
        }
        catch (XPathExpressionException e)
        {
            log.warn("cannot extact Value");
            return null;
        }
    }

    @Override
    public void insertContainer(ContainerItemDto items)
    {
    	try {
        	log.info("[insertContainer] enter ... ");
        	isInsertingContainerInPlaylist = true;
            int sumInsert = 0;
            Long lastid = getLastSongId();

            for (MusicItemDto music : items.musicItemDto)
            {
                InsertInput insertInput = new InsertInput();
                insertInput.Metadata = music.currentTrackMetadata;
                insertInput.Uri = music.streamingURL;
                insertInput.AfterId = lastid;

                // Workaround for some rare situations where a media player is reporting negative IDs.
                long insertPos = insert(insertInput);
                if (insertPos < 0) {
                	log.warn("device gave negative insert position {}. Assuming 0.", insertPos);
                }
                lastid = Math.max(0, insertPos);
            	log.info("[insertContainer] adding uri {}. Received lastid {} ", music.streamingURL, lastid);
                sumInsert++;
                if (sumInsert % 10 == 0)
                {
                    waitSomeTime(20);
                }
            }
        	log.info("[insertContainer] added {} items", sumInsert);
        	IdArrayOutput id = playlistService.idArray();
        	log.info("Playlist items according to device state : {}", id.Array != null ? id.Array.length / 4 : "NULL");
        	isInsertingContainerInPlaylist = false;    		
    	} catch (Exception e) {
    		log.error("insertContainer", e);
    	}
    }

    private void waitSomeTime(int millis)
    {
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            log.warn("cannot sleep", e);
        }
    }

    public Long getLastSongId()
    {
        if (playlistIds.isEmpty())
        {
            return 0L;
        }
        return playlistIds.getLast();
    }

    public Long getFirstSongId()
    {
        return playlistIds.getFirst();
    }

    @Override
    public void insertAndPlayContainer(ContainerItemDto items)
    {
        deleteAll();
        waitSomeTime(30);
        insertContainer(items);
        waitSomeTime(30);
        play();
    }

    public TransportServiceStateDto getCurrentTransportServiceState()
    {
        TransportServiceStateDto dto = new TransportServiceStateDto();

        dto.canRepeat = true;
        dto.canShuffle = true;
        dto.canSkipNext = true;
        dto.canSkipPrevious = true;

        dto.transportState = playlistService.transportState().Value;
        dto.repeat = playlistService.repeat().Value;
        dto.shuffle = playlistService.shuffle().Value;

        dto.canPause = true;
        dto.canSeek = true;

        dto.udn = device.getUdnAsString();

        return dto;
    }
    
    @Override
    public void idArrayChange(byte[] value) {
    	super.idArrayChange(value);
    	if (!isInsertingContainerInPlaylist) {
        	log.info("idArrayChanged ... rebuilding playlist ...");
        	convertIdArrayToMusicItemList(value);
    	}
    }
}
