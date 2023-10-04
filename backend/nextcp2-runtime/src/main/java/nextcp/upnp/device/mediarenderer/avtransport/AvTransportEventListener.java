package nextcp.upnp.device.mediarenderer.avtransport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.StAXEventBuilder;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nextcp.upnp.device.mediarenderer.MediaRendererDevice;
import nextcp.upnp.modelGen.schemasupnporg.aVTransport1.AVTransportServiceEventListenerImpl;

/**
 * Do not overwrite this class.
 */
public class AvTransportEventListener extends AVTransportServiceEventListenerImpl
{
    private static final Logger log = LoggerFactory.getLogger(AvTransportEventListener.class.getName());

    private MediaRendererDevice device = null;

    private AvTransportState currentAvTransportState = new AvTransportState();

    private CopyOnWriteArrayList<IAVTransportEvents> avEventListener = new CopyOnWriteArrayList<IAVTransportEvents>();

    public AvTransportEventListener(MediaRendererDevice device)
    {
        this.device = device;
    }

    public AvTransportState getCurrentAvTransportState()
    {
        return currentAvTransportState;
    }

    protected MediaRendererDevice getDevice()
    {
        return device;
    }

    public void addEventListener(IAVTransportEvents listener)
    {
        this.avEventListener.add(listener);
    }

    public void removeEventListener(IAVTransportEvents listener)
    {
        this.avEventListener.remove(listener);
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
        // All state changes are send in the lastChange attribute.

        Document doc = getStAXParsedDocument(value, false);
        if (doc == null)
        {
            doc = getStAXParsedDocument(fixEscapeErrors(value), true);
            if (doc == null)
            {
                log.warn("cannot process xml document.");
            }
        }
        Element instanceID = getInstanceIDElement(doc);

        if (instanceID != null && instanceID.getAttributeValue("val") != null)
        {
            // todo: have a map with instance-id's
            // long InstanceId = Long.parseLong(instanceID.getAttributeValue("val"));
            for (Element element : instanceID.getChildren())
            {
                String key = element.getName();
                String val = element.getAttributeValue("val");
                updateLocalAvTransportState(key, val, currentAvTransportState); // TODO use correct instance id
            }
        }
        else
        {
            log.warn("no InstanceId in AVTransport message ... ");
        }

        // notify dependent classes
        avEventListener.stream().forEach(e -> e.processingFinished(currentAvTransportState));
    }

    /**
     * Some devices, like Naim Mu-So QB escape XML message incorrectly
     * 
     * @param value
     * @return
     */
    String fixEscapeErrors(String value)
    {
        // TODO: check if we have a Naim device ? ...
        StringBuilder sb = new StringBuilder(value.length() + 40);
        BufferedReader sr = new BufferedReader(new StringReader(value));
        String line = "";
        do
        {
            try
            {
                if (line.contains("<CurrentTrackMetaData val="))
                {
                    replaceLine(sb, line, "CurrentTrackMetaData");
                }
                else if (line.contains("<AVTransportURIMetaData val="))
                {
                    replaceLine(sb, line, "AVTransportURIMetaData");
                }
                else if (line.contains("<NextAVTransportURIMetaData val="))
                {
                    replaceLine(sb, line, "NextAVTransportURIMetaData");
                }
                else
                {
                    sb.append(line);
                }
                line = sr.readLine();
            }
            catch (IOException e)
            {
                log.warn("cannot apply fix");
                return value;
            }
        }
        while (line != null);

        return sb.toString();
    }

    private void replaceLine(StringBuilder sb, String line, String ident)
    {
        sb.append("  <").append(ident).append(" val=\"");
        String val = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
        sb.append(StringEscapeUtils.escapeXml(val));
        sb.append("\"/>");
    }

    Element getInstanceIDElement(Document doc)
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

    Document getStAXParsedDocument(final String text, boolean printError)
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
            if (printError)
            {
                log.warn("couldn't parse Message from media renderer. Text :", e);
                log.warn(text);
            }
        }
        return document;
    }

    private void updateLocalAvTransportState(String key, String val, AvTransportState transportState)
    {
        switch (key)
        {
            case "AbsoluteTimePosition":
                transportState.AbsoluteTimePosition = val;
                avEventListener.stream().forEach(e -> e.absoluteTimePositionChange(val));
                break;
            case "CurrentTrackURI":
                transportState.CurrentTrackURI = val;
                avEventListener.stream().forEach(e -> e.currentTrackURIChange(val));

                break;
            case "CurrentTrackMetaData":
                transportState.CurrentTrackMetaData = val;
                avEventListener.stream().forEach(e -> e.currentTrackMetaDataChange(val));
                break;
            case "RelativeCounterPosition":
                transportState.RelativeCounterPosition = Integer.parseInt(val);
                avEventListener.stream().forEach(e -> e.relativeCounterPositionChange(transportState.RelativeCounterPosition));
                break;
            case "TransportStatus":
                transportState.TransportStatus = val;
                avEventListener.stream().forEach(e -> e.transportStatusChange(val));
                break;
            case "AVTransportURIMetaData":
                transportState.AVTransportURIMetaData = val;
                avEventListener.stream().forEach(e -> e.aVTransportURIMetaDataChange(val));
                break;
            case "TransportState":
                transportState.TransportState = val; // PLAYING, STOPPED, etc.
                avEventListener.stream().forEach(e -> e.transportStateChange(val));
                break;
            case "CurrentTrack":
                transportState.CurrentTrack = Long.parseLong(val);
                avEventListener.stream().forEach(e -> e.currentTrackChange(transportState.CurrentTrack));
                break;
            case "PlaybackStorageMedium":
                transportState.PlaybackStorageMedium = val;
                avEventListener.stream().forEach(e -> e.playbackStorageMediumChange(val));
                break;
            case "PossibleRecordQualityModes":
                transportState.PossibleRecordQualityModes = val;
                avEventListener.stream().forEach(e -> e.possibleRecordQualityModesChange(val));
                break;
            case "NextAVTransportURIMetaData":
                transportState.NextAVTransportURIMetaData = val;
                avEventListener.stream().forEach(e -> e.nextAVTransportURIChange(val));
                break;
            case "NumberOfTracks":
                transportState.NumberOfTracks = Long.parseLong(val);
                avEventListener.stream().forEach(e -> e.numberOfTracksChange(transportState.NumberOfTracks));
                break;
            case "CurrentMediaDuration":
                transportState.CurrentMediaDuration = val;
                avEventListener.stream().forEach(e -> e.currentMediaDurationChange(val));
                break;
            case "NextAVTransportURI":
                transportState.NextAVTransportURI = val;
                avEventListener.stream().forEach(e -> e.nextAVTransportURIMetaDataChange(val));
                break;
            case "RecordStorageMedium":
                transportState.RecordStorageMedium = val;
                avEventListener.stream().forEach(e -> e.recordStorageMediumChange(val));
                break;
            case "AVTransportURI":
                transportState.AVTransportURI = val;
                avEventListener.stream().forEach(e -> e.aVTransportURIChange(val));
                break;
            case "TransportPlaySpeed":
                transportState.TransportPlaySpeed = val;
                avEventListener.stream().forEach(e -> e.transportPlaySpeedChange(val));
                break;
            case "AbsoluteCounterPosition":
                transportState.AbsoluteCounterPosition = Integer.parseInt(val);
                avEventListener.stream().forEach(e -> e.absoluteCounterPositionChange(transportState.AbsoluteCounterPosition));
                break;
            case "RelativeTimePosition":
                transportState.RelativeTimePosition = val;
                avEventListener.stream().forEach(e -> e.relativeTimePositionChange(val));
                break;
            case "CurrentPlayMode":
                transportState.CurrentPlayMode = val;
                avEventListener.stream().forEach(e -> e.currentPlayModeChange(val));
                break;
            case "CurrentTrackDuration":
                transportState.CurrentTrackDuration = val;
                avEventListener.stream().forEach(e -> e.currentTrackDurationChange(val));
                break;
            case "PossiblePlaybackStorageMedia":
                transportState.PossiblePlaybackStorageMedia = val;
                avEventListener.stream().forEach(e -> e.possiblePlaybackStorageMediaChange(val));
                break;
            case "CurrentRecordQualityMode":
                transportState.CurrentRecordQualityMode = val;
                avEventListener.stream().forEach(e -> e.currentRecordQualityModeChange(val));
                break;
            case "RecordMediumWriteStatus":
                transportState.RecordMediumWriteStatus = val;
                avEventListener.stream().forEach(e -> e.recordMediumWriteStatusChange(val));
                break;
            case "CurrentTransportActions":
                transportState.CurrentTransportActions = val;
                avEventListener.stream().forEach(e -> e.currentTransportActionsChange(val));
                break;
            case "PossibleRecordStorageMedia":
                transportState.PossibleRecordStorageMedia = val;
                avEventListener.stream().forEach(e -> e.possibleRecordStorageMediaChange(val));
                break;
            default:
                log.warn("unknown state variable : " + key);
        }

    }

    //
    // Subscription
    //

    @Override
    public void ended(CancelReason reason, UpnpResponse responseStatus)
    {
        log.warn(String.format("Subscription ended for device %s. Reason: %s. StatusMessage: %s. Code: %d", getDevice().getFriendlyName(), reason.toString(),
                responseStatus != null ? responseStatus.getStatusMessage() : "NULL", responseStatus != null ? responseStatus.getStatusCode() : "response status is NULL"));
    }
}
