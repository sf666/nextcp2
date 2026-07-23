package nextcp.upnp.device.mediarenderer.ohradio;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.domainmodel.device.services.IRadioService;
import nextcp.domainmodel.device.services.ITransport;
import nextcp.dto.MusicItemDto;
import nextcp.dto.TransportServiceStateDto;
import nextcp.rest.DtoBuilder;
import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.device.mediarenderer.OpenHomeUtils;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.RadioService;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ChannelOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.IdOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ReadInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ReadListInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ReadListOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.ReadOutput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SetChannelInput;
import nextcp.upnp.modelGen.avopenhomeorg.radio1.actions.SetIdInput;
import nextcp.upnp.modelGen.avopenhomeorg.transport1.actions.PlayAsInput;

public class OhRadioBridge implements IRadioService, ITransport
{
    private static final Logger log = LoggerFactory.getLogger(OhRadioBridge.class.getName());

    private RadioService radioService = null;
    private OpenHomeUtils ohUtil = null;
    private MediaRendererDevice device = null;

    public OhRadioBridge(RadioService radioService, DtoBuilder dtoBuilder, MediaRendererDevice device)
    {
        this.radioService = radioService;
        this.device = device;
        this.ohUtil = new OpenHomeUtils(dtoBuilder);
    }

    public void pause()
    {
        radioService.pause();
    }

    public void stop()
    {
        radioService.stop();
    }

    public ChannelOutput channel()
    {
        return radioService.channel();
    }

    public ReadOutput read(ReadInput inp)
    {
        return radioService.read(inp);
    }

    @Override
    public void play(MusicItemDto radioStation)
    {
        SetIdInput inp = new SetIdInput();
        inp.Value = Long.parseLong(radioStation.objectID);
        inp.Uri = radioStation.streamingURL;
        setId(inp);
        play();
    }

    @Override
    public void playStream(String uri, String metadata)
    {
        logIncomingMetadata(metadata);

        // Try three transports in order of preference, each falling through to the next on failure:
        //   1) OpenHome Transport service (Transport.PlayAs, Mode "Radio").
        //   2) Legacy OpenHome Radio service (SetChannel + Play).
        //   3) AVTransport (SetAVTransportURI + Play), as before.
        if (tryPlayViaTransport(uri, metadata))
        {
            return;
        }
        if (tryPlayViaRadioService(uri, metadata))
        {
            return;
        }
        playViaAvTransport(uri, metadata);
    }

    private void logIncomingMetadata(String metadata)
    {
        if (metadata != null && !metadata.isBlank())
        {
            log.info("playStream: incoming source metadata ({} chars): {}", metadata.length(), metadata);
        }
        else
        {
            log.info("playStream: incoming source metadata is EMPTY");
        }
    }

    /**
     * Attempt 1: OpenHome Transport.PlayAs with Mode "Radio". PlayAs has no separate Uri argument, so
     * the stream URI is carried inside the Command DIDL-Lite as a &lt;res&gt; element.
     *
     * @return true if the action was accepted, false if unavailable or rejected (so the caller falls
     *         through to the next transport).
     */
    private boolean tryPlayViaTransport(String uri, String metadata)
    {
        if (!device.hasOhTransport())
        {
            log.debug("playStream: device has no OpenHome Transport service; skipping Transport.PlayAs");
            return false;
        }
        try
        {
            String didl = buildRadioMetadata(metadata, uri);
            PlayAsInput inp = new PlayAsInput();
            inp.Mode = "Radio";
            inp.Command = didl;
            log.info("playStream: trying Transport.PlayAs Mode='Radio' Uri='{}' (command length={})", uri, didl.length());
            log.debug("playStream: PlayAs command = {}", didl);
            device.getOhTransportService().playAs(inp);
            log.info("playStream: Transport.PlayAs accepted");
            return true;
        }
        catch (Exception e)
        {
            log.warn("playStream: Transport.PlayAs failed for {} ({}); falling back to Radio service", uri, e.getMessage());
            return false;
        }
    }

    /**
     * Attempt 2: legacy OpenHome Radio service (SetChannel + Play).
     *
     * @return true if accepted, false if rejected (caller falls through to AVTransport).
     */
    private boolean tryPlayViaRadioService(String uri, String metadata)
    {
        try
        {
            // Some OpenHome renderers reject SetChannel with empty or overly complex metadata, so send
            // a minimal, well-formed audioBroadcast DIDL-Lite (title + class) instead of the full media
            // server document.
            String didl = buildRadioMetadata(metadata, null);
            SetChannelInput inp = new SetChannelInput();
            inp.Uri = uri;
            inp.Metadata = didl;
            log.info("playStream: calling Radio.SetChannel with Uri='{}' (metadata length={})", inp.Uri, didl.length());
            log.debug("playStream: SetChannel metadata = {}", didl);
            radioService.setChannel(inp);
            log.info("playStream: Radio.SetChannel accepted; calling Radio.Play");
            play();
            log.info("playStream: Radio.Play sent");
            return true;
        }
        catch (Exception e)
        {
            log.warn("playStream: OpenHome Radio play failed for {} ({}); falling back to AVTransport", uri, e.getMessage());
            return false;
        }
    }

    /**
     * Attempt 3 (last resort): AVTransport SetAVTransportURI + Play.
     */
    private void playViaAvTransport(String uri, String metadata)
    {
        if (device.getAvTransportBridge() != null)
        {
            log.info("playStream: playing via AVTransport: {}", uri);
            device.getAvTransportBridge().play(uri, metadata);
        }
        else
        {
            log.warn("playStream: device has neither a working Transport/Radio service nor AVTransport - cannot play stream {}", uri);
        }
    }

    private static final Pattern TITLE_PATTERN = Pattern.compile("<dc:title>(.*?)</dc:title>", Pattern.DOTALL);

    /**
     * Builds a minimal, well-formed DIDL-Lite metadata document carrying the title (taken from the
     * supplied metadata when present) and the audioBroadcast class. When {@code resUri} is non-null a
     * &lt;res&gt; element with the (XML-escaped) stream URI is included — required for Transport.PlayAs,
     * which has no separate Uri argument; pass {@code null} for Radio.SetChannel, which carries the Uri
     * separately.
     */
    private static String buildRadioMetadata(String sourceMetadata, String resUri)
    {
        String title = "Radio";
        if (sourceMetadata != null)
        {
            Matcher m = TITLE_PATTERN.matcher(sourceMetadata);
            if (m.find() && !m.group(1).trim().isEmpty())
            {
                // The captured value is already XML-escaped within the source DIDL.
                title = m.group(1).trim();
            }
        }
        String resElement = "";
        if (resUri != null && !resUri.isBlank())
        {
            resElement = "<res protocolInfo=\"http-get:*:*:*\">" + escapeXml(resUri) + "</res>";
        }
        return "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\""
            + " xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\""
            + " xmlns:dc=\"http://purl.org/dc/elements/1.1/\">"
            + "<item id=\"0\" parentID=\"0\" restricted=\"1\">"
            + "<dc:title>" + title + "</dc:title>"
            + "<upnp:class>object.item.audioItem.audioBroadcast</upnp:class>"
            + resElement
            + "</item></DIDL-Lite>";
    }

    private static String escapeXml(String s)
    {
        if (s == null)
        {
            return "";
        }
        return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
    }

    public void play()
    {
        radioService.play();
    }

    /**
     * Retuns list of radio stations.
     * 
     * @return
     */
    public List<MusicItemDto> getRadioStations()
    {
    	log.info("Requesting radio stations ... ");
        byte[] ba = radioService.idArray().Array;
        String idList = ohUtil.convertUintByteArrayToStringList(ba);
        ReadListInput inp = new ReadListInput();
        inp.IdList = idList;
        ReadListOutput songs = radioService.readList(inp);

        return ohUtil.convertToMediaItemDto(songs.ChannelList, "ChannelList");
    }

    public void setChannel(SetChannelInput inp)
    {
        radioService.setChannel(inp);
    }

    public IdOutput id()
    {
        return radioService.id();
    }

    public void setId(SetIdInput inp)
    {
        radioService.setId(inp);
    }

    @Override
    public void next()
    {
        log.debug("play next is unsupported on radio service.");
    }

    @Override
    public TransportServiceStateDto getCurrentTransportServiceState()
    {
        TransportServiceStateDto dto = new TransportServiceStateDto();

        dto.canRepeat = false;
        dto.canShuffle = false;
        dto.canSkipNext = false;
        dto.canSkipPrevious = false;

        dto.transportState = radioService.transportState().Value;

        dto.canPause = true;
        dto.canSeek = false;

        dto.udn = device.getUdnAsString();

        return dto;
    }

    @Override
    public void seek(long secondsAbsolute)
    {
        throw new RuntimeException("Radio Service doesn't implement seek action.");
    }
}
