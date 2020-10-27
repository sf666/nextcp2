package nextcp.domainmodel.device.mediarenderer.avtransport;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.StAXEventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import nextcp.domainmodel.device.mediarenderer.MediaRendererDevice;
import nextcp.domainmodel.device.mediarenderer.avtransport.event.AvTransportCurrentTrackUriChangeEvent;
import nextcp.domainmodel.device.mediarenderer.avtransport.event.AvTransportStateChangedEvent;
import nextcp.domainmodel.device.mediarenderer.avtransport.event.AvTransportTransportStateChangeEvent;
import nextcp.dto.TrackInfoDto;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.CountersOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.DetailsOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.MetatextOutput;
import nextcp.upnp.modelGen.avopenhomeorg.info.actions.TrackOutput;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport.AVTransportServiceEventListenerImpl;

/**
 * Overwrite event class for notification reason ... Don't forget to call super implementation for overridden classes
 * 
 */
public class AvTransportEventListener extends AVTransportServiceEventListenerImpl
{
    private static final Logger log = LoggerFactory.getLogger(AvTransportEventListener.class.getName());

    private MediaRendererDevice device = null;

    private AvTransportState currentAvTransportState = new AvTransportState();

    public AvTransportState getCurrentAvTransportState()
    {
        return currentAvTransportState;
    }

    public AvTransportEventListener(MediaRendererDevice device)
    {
        this.device = device;
    }

    private ApplicationEventPublisher getEventPublisher()
    {
        return device.getEventPublisher();
    }

    protected MediaRendererDevice getDevice()
    {
        return device;
    }

    @Override
    public void lastChangeChange(String value)
    {
        super.lastChangeChange(value);

        if (StringUtils.isBlank(value))
        {
            return;
        }

        if (log.isDebugEnabled())
        {
            log.debug("Last Change : " + StringEscapeUtils.unescapeXml(value));
        }

        // AvTransport doesn't use "regular" upnp eventing, because of virtual instanceID's support.
        // All state changes are beeing send in the lastChange attribute.

        Document doc = getStAXParsedDocument(value);
        Element instanceID = getInstanceIDElement(doc);

        AvTransportState transportState = new AvTransportState();
        transportState.InstanceId = Long.parseLong(instanceID.getAttributeValue("val"));
        for (Element element : instanceID.getChildren())
        {
            String key = element.getName();
            String val = element.getAttributeValue("val");
            processEventAttribute(key, val, transportState);
        }
        
        // notify dependent classes
        processEvent(transportState);
        currentAvTransportState = transportState;
        
        publishCurrentAvTransportState(transportState);
        getEventPublisher().publishEvent(getAsTrackInfo(transportState));
    }

    private void publishCurrentAvTransportState(AvTransportState transportState)
    {
        AvTransportStateChangedEvent event = new AvTransportStateChangedEvent();
        event.state = transportState;
        event.device = device;
        getEventPublisher().publishEvent(event);
    }
    
    private TrackInfoDto getAsTrackInfo(AvTransportState transportState)
    {
        TrackInfoDto dto = new TrackInfoDto();

        dto.mediaRendererUdn = device.getUdnAsString();
        dto.metatext = transportState.CurrentTrackMetaData;
        dto.uri = transportState.CurrentTrackURI;
        dto.currentTrack = device.getDtoBuilder().extractXmlAsMusicItem(transportState.CurrentTrackMetaData);

        return dto;
        
    }

    private Element getInstanceIDElement(Document doc)
    {
        try
        {
            return doc.getRootElement().getChildren().get(0);
        }
        catch (NullPointerException e)
        {
            log.warn("Element InstanceID doesn't exist.");
            return null;
        }
    }

    private Document getStAXParsedDocument(final String text)
    {

        Document document = null;
        try
        {
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLEventReader reader = factory.createXMLEventReader(new StringReader(text));
            StAXEventBuilder builder = new StAXEventBuilder();
            document = builder.build(reader);
        }
        catch (JDOMException | XMLStreamException e)
        {
            e.printStackTrace();
        }
        return document;
    }

    public void currentTrackURIChange(String value)
    {
        AvTransportCurrentTrackUriChangeEvent event = new AvTransportCurrentTrackUriChangeEvent();
        event.currentTrackUri = value;
        getEventPublisher().publishEvent(event);
    }

    public void transportStateChange(String value)
    {
        AvTransportTransportStateChangeEvent event = new AvTransportTransportStateChangeEvent();
        event.currentState = value;
        event.mediaRendererUdn = device.getUDN().getIdentifierString();
        getEventPublisher().publishEvent(event);
    }

    private void processEventAttribute(String key, String val, AvTransportState transportState)
    {
        switch (key)
        {
            case "AbsoluteTimePosition":
                transportState.AbsoluteTimePosition = val;
                break;
            case "CurrentTrackURI":
                transportState.CurrentTrackURI = val; // empty, or current URI
                break;
            case "CurrentTrackMetaData":
                transportState.CurrentTrackMetaData = val;
                break;
            case "RelativeCounterPosition":
                transportState.RelativeCounterPosition = Integer.parseInt(val);
                break;
            case "TransportStatus":
                transportState.TransportStatus = val;
                break;
            case "AVTransportURIMetaData":
                transportState.AVTransportURIMetaData = val;
                break;
            case "TransportState":
                transportState.TransportState = val; // PLAYING, STOPPED, etc.
                break;
            case "CurrentTrack":
                transportState.CurrentTrack = Long.parseLong(val);
                break;
            case "PlaybackStorageMedium":
                transportState.PlaybackStorageMedium = val;
                break;
            case "PossibleRecordQualityModes":
                transportState.PossibleRecordQualityModes = val;
                break;
            case "NextAVTransportURIMetaData":
                transportState.NextAVTransportURIMetaData = val;
                break;
            case "NumberOfTracks":
                transportState.NumberOfTracks = Long.parseLong(val);
                break;
            case "CurrentMediaDuration":
                transportState.CurrentMediaDuration = val;
                break;
            case "NextAVTransportURI":
                transportState.NextAVTransportURI = val;
                break;
            case "RecordStorageMedium":
                transportState.RecordStorageMedium = val;
                break;
            case "AVTransportURI":
                transportState.AVTransportURI = val;
                break;
            case "TransportPlaySpeed":
                transportState.TransportPlaySpeed = val;
                break;
            case "AbsoluteCounterPosition":
                transportState.AbsoluteCounterPosition = Integer.parseInt(val);
                break;
            case "RelativeTimePosition":
                transportState.RelativeTimePosition = val;
                break;
            case "CurrentPlayMode":
                transportState.CurrentPlayMode = val;
                break;
            case "CurrentTrackDuration":
                transportState.CurrentTrackDuration = val;
                break;
            case "PossiblePlaybackStorageMedia":
                transportState.PossiblePlaybackStorageMedia = val;
                break;
            case "CurrentRecordQualityMode":
                transportState.CurrentRecordQualityMode = val;
                break;
            case "RecordMediumWriteStatus":
                transportState.RecordMediumWriteStatus = val;
                break;
            case "CurrentTransportActions":
                transportState.CurrentTransportActions = val;
                break;
            case "PossibleRecordStorageMedia":
                transportState.PossibleRecordStorageMedia = val;
                break;
            default:
                log.warn("unknown state variable : " + key);
        }
    }

    // TODO instanceID support: keep track of state variable per instance. This impementation uses only one instance.
    private void processEvent(AvTransportState newState)
    {
        if (!this.currentAvTransportState.AbsoluteCounterPosition.equals(newState.AbsoluteCounterPosition))
        {
            absoluteCounterPositionChange(newState.AbsoluteCounterPosition);
        }
        else if (!this.currentAvTransportState.AbsoluteTimePosition.equals(newState.AbsoluteTimePosition))
        {
            absoluteTimePositionChange(newState.AbsoluteTimePosition);
        }
        else if (!this.currentAvTransportState.AVTransportURI.equals(newState.AVTransportURI))
        {
            aVTransportURIChange(newState.AVTransportURI);
        }
        else if (!this.currentAvTransportState.AVTransportURIMetaData.equals(newState.AVTransportURIMetaData))
        {
            aVTransportURIMetaDataChange(newState.AVTransportURIMetaData);
        }
        else if (!this.currentAvTransportState.CurrentMediaDuration.equals(newState.CurrentMediaDuration))
        {
            currentMediaDurationChange(newState.CurrentMediaDuration);
        }
        else if (!this.currentAvTransportState.CurrentPlayMode.equals(newState.CurrentPlayMode))
        {
            currentPlayModeChange(newState.CurrentPlayMode);
        }
        else if (!this.currentAvTransportState.CurrentRecordQualityMode.equals(newState.CurrentRecordQualityMode))
        {
            currentRecordQualityModeChange(newState.CurrentRecordQualityMode);
        }
        else if (!this.currentAvTransportState.CurrentTrack.equals(newState.CurrentTrack))
        {
            currentTrackChange(newState.CurrentTrack);
        }
        else if (!this.currentAvTransportState.CurrentTrackDuration.equals(newState.CurrentTrackDuration))
        {
            currentTrackDurationChange(newState.CurrentTrackDuration);
        }
        else if (!this.currentAvTransportState.CurrentTrackMetaData.equals(newState.CurrentTrackMetaData))
        {
            currentTrackMetaDataChange(newState.CurrentTrackMetaData);
        }
        else if (!this.currentAvTransportState.CurrentTrackURI.equals(newState.CurrentTrackURI))
        {
            currentTrackURIChange(newState.CurrentTrackURI);
        }
        else if (!this.currentAvTransportState.CurrentTransportActions.equals(newState.CurrentTransportActions))
        {
            currentTransportActionsChange(newState.CurrentTransportActions);
        }
        else if (!this.currentAvTransportState.NextAVTransportURI.equals(newState.NextAVTransportURI))
        {
            nextAVTransportURIChange(newState.NextAVTransportURI);
        }
        else if (!this.currentAvTransportState.NextAVTransportURIMetaData.equals(newState.NextAVTransportURIMetaData))
        {
            nextAVTransportURIMetaDataChange(newState.NextAVTransportURIMetaData);
        }
        else if (!this.currentAvTransportState.NumberOfTracks.equals(newState.NumberOfTracks))
        {
            numberOfTracksChange(newState.NumberOfTracks);
        }
        else if (!this.currentAvTransportState.PlaybackStorageMedium.equals(newState.PlaybackStorageMedium))
        {
            playbackStorageMediumChange(newState.PlaybackStorageMedium);
        }
        else if (!this.currentAvTransportState.PossiblePlaybackStorageMedia.equals(newState.PossiblePlaybackStorageMedia))
        {
            possiblePlaybackStorageMediaChange(newState.PossiblePlaybackStorageMedia);
        }
        else if (!this.currentAvTransportState.PossibleRecordQualityModes.equals(newState.PossibleRecordQualityModes))
        {
            possibleRecordQualityModesChange(newState.PossibleRecordQualityModes);
        }
        else if (!this.currentAvTransportState.PossibleRecordStorageMedia.equals(newState.PossibleRecordStorageMedia))
        {
            possibleRecordStorageMediaChange(newState.PossibleRecordStorageMedia);
        }
        else if (!this.currentAvTransportState.RecordMediumWriteStatus.equals(newState.RecordMediumWriteStatus))
        {
            recordMediumWriteStatusChange(newState.RecordMediumWriteStatus);
        }
        else if (!this.currentAvTransportState.RecordStorageMedium.equals(newState.RecordStorageMedium))
        {
            recordStorageMediumChange(newState.RecordStorageMedium);
        }
        else if (!this.currentAvTransportState.RelativeCounterPosition.equals(newState.RelativeCounterPosition))
        {
            relativeCounterPositionChange(newState.RelativeCounterPosition);
        }
        else if (!this.currentAvTransportState.RelativeTimePosition.equals(newState.RelativeTimePosition))
        {
            relativeTimePositionChange(newState.RelativeTimePosition);
        }
        else if (!this.currentAvTransportState.TransportPlaySpeed.equals(newState.TransportPlaySpeed))
        {
            transportPlaySpeedChange(newState.TransportPlaySpeed);
        }
        else if (!this.currentAvTransportState.TransportState.equals(newState.TransportState))
        {
            transportStateChange(newState.TransportState);
        }
        else if (!this.currentAvTransportState.TransportStatus.equals(newState.TransportStatus))
        {
            transportStatusChange(newState.TransportStatus);
        }
    }

    public void transportStatusChange(String transportStatus)
    {
        if (log.isDebugEnabled())
        {
            log.debug("transportStatusChange : " + transportStatus);
        }
    }

    public void transportPlaySpeedChange(String transportPlaySpeed)
    {
        if (log.isDebugEnabled())
        {
            log.debug("transportPlaySpeed : " + transportPlaySpeed);
        }

    }

    public void relativeTimePositionChange(String relativeTimePosition)
    {
        if (log.isDebugEnabled())
        {
            log.debug("relativeTimePosition : " + relativeTimePosition);
        }

    }

    public void relativeCounterPositionChange(Integer relativeCounterPosition)
    {
        if (log.isDebugEnabled())
        {
            log.debug("relativeCounterPosition : " + relativeCounterPosition);
        }

    }

    public void recordStorageMediumChange(String recordStorageMedium)
    {
        if (log.isDebugEnabled())
        {
            log.debug("recordStorageMedium : " + recordStorageMedium);
        }

    }

    public void recordMediumWriteStatusChange(String recordMediumWriteStatus)
    {
        if (log.isDebugEnabled())
        {
            log.debug("recordMediumWriteStatus : " + recordMediumWriteStatus);
        }
    }

    public void possibleRecordStorageMediaChange(String possibleRecordStorageMedia)
    {
        if (log.isDebugEnabled())
        {
            log.debug("possibleRecordStorageMedia : " + possibleRecordStorageMedia);
        }
    }

    public void possibleRecordQualityModesChange(String possibleRecordQualityModes)
    {
        if (log.isDebugEnabled())
        {
            log.debug("possibleRecordQualityModes : " + possibleRecordQualityModes);
        }

    }

    public void possiblePlaybackStorageMediaChange(String possiblePlaybackStorageMedia)
    {
        if (log.isDebugEnabled())
        {
            log.debug("possiblePlaybackStorageMedia : " + possiblePlaybackStorageMedia);
        }

    }

    public void playbackStorageMediumChange(String playbackStorageMedium)
    {
        if (log.isDebugEnabled())
        {
            log.debug("playbackStorageMedium : " + playbackStorageMedium);
        }

    }

    public void numberOfTracksChange(Long numberOfTracks)
    {
        if (log.isDebugEnabled())
        {
            log.debug("numberOfTracks : " + numberOfTracks);
        }

    }

    public void nextAVTransportURIMetaDataChange(String nextAVTransportURIMetaData)
    {
        if (log.isDebugEnabled())
        {
            log.debug("nextAVTransportURIMetaData : " + nextAVTransportURIMetaData);
        }

    }

    public void nextAVTransportURIChange(String nextAVTransportURI)
    {
        if (log.isDebugEnabled())
        {
            log.debug("nextAVTransportURI : " + nextAVTransportURI);
        }

    }

    public void currentTransportActionsChange(String currentTransportActions)
    {
        if (log.isDebugEnabled())
        {
            log.debug("currentTransportActions : " + currentTransportActions);
        }

    }

    public void currentTrackMetaDataChange(String currentTrackMetaData)
    {
        if (log.isDebugEnabled())
        {
            log.debug("currentTrackMetaData : " + currentTrackMetaData);
        }
    }

    public void currentTrackDurationChange(String currentTrackDuration)
    {
        if (log.isDebugEnabled())
        {
            log.debug("currentTrackDuration : " + currentTrackDuration);
        }

    }

    public void currentRecordQualityModeChange(String currentRecordQualityMode)
    {
        if (log.isDebugEnabled())
        {
            log.debug("currentRecordQualityMode : " + currentRecordQualityMode);
        }

    }

    public void currentTrackChange(Long currentTrack)
    {
        if (log.isDebugEnabled())
        {
            log.debug("currentTrack : " + currentTrack);
        }
    }

    public void currentPlayModeChange(String currentPlayMode)
    {
        if (log.isDebugEnabled())
        {
            log.debug("currentPlayMode : " + currentPlayMode);
        }
    }

    public void currentMediaDurationChange(String currentMediaDuration)
    {
        if (log.isDebugEnabled())
        {
            log.debug("currentMediaDuration : " + currentMediaDuration);
        }
    }

    public void aVTransportURIMetaDataChange(String aVTransportURIMetaData)
    {
        if (log.isDebugEnabled())
        {
            log.debug("aVTransportURIMetaData : " + aVTransportURIMetaData);
        }
    }

    public void aVTransportURIChange(String aVTransportURI)
    {
        if (log.isDebugEnabled())
        {
            log.debug("aVTransportURI : " + aVTransportURI);
        }

    }

    public void absoluteTimePositionChange(String absoluteTimePosition)
    {
        if (log.isDebugEnabled())
        {
            log.debug("absoluteTimePosition : " + absoluteTimePosition);
        }

    }

    public void absoluteCounterPositionChange(Integer absoluteCounterPosition)
    {
        if (log.isDebugEnabled())
        {
            log.debug("absoluteCounterPosition : " + absoluteCounterPosition);
        }
    }
}
