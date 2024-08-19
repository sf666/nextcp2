package nextcp.upnp.device.mediarenderer.ohradio;

import java.util.List;

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
