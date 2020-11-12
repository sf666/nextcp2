package nextcp.domainmodel.device.mediarenderer.avtransport;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.StringReader;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.StAXEventBuilder;
import org.junit.jupiter.api.Test;

public class NaimTest
{
    @Test
    public void testAvTransportEventMessage()
    {
        AvTransportEventListener av = new AvTransportEventListener(null);
        String eventText = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\">\n" + "  <InstanceID val=\"0\">\n"
                + "    <CurrentTransportActions val=\"Play,Next,Previous\"/>\n"
                + "    <AVTransportURI val=\"http://10.18.112.237:9790/minimserver/*/music/unknown/still/still_1.flac\"/>\n"
                + "    <CurrentTrackMetaData val=\"&lt;DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:sec=\"http://www.sec.co.kr/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"&gt;&lt;item id=\"-1\" parentID=\"-1\" restricted=\"1\"&gt;&lt;dc:title&gt;still_1.ogg&lt;/dc:title&gt;&lt;upnp:class&gt;object.item.audioItem.musicTrack&lt;/upnp:class&gt;&lt;upnp:originalTrackNumber&gt;1&lt;/upnp:originalTrackNumber&gt;&lt;upnp:albumArtURI xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" dlna:profileID=\"JPEG_LRG\"&gt;http://192.168.112.237:9790/minimserver/*/music/unknown/Stille/still_1.flac/$!picture-176-167405.jpg&lt;/upnp:albumArtURI&gt;&lt;res bitrate=\"264600\" bitsPerSample=\"24\" duration=\"0:00:10.070\" nrAudioChannels=\"2\" protocolInfo=\"http-get:*:audio/x-flac:DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000\" sampleFrequency=\"44100\" size=\"640407\"&gt;http://192.168.112.237:9790/minimserver/*/music/unknown/Stille/still_1.flac&lt;/res&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;\"/>\n"
                + "    <CurrentTrackURI val=\"http://10.18.112.237:9790/minimserver/*/music/unknown/still/still_1.flac\"/>\n"
                + "    <AVTransportURIMetaData val=\"&lt;DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:sec=\"http://www.sec.co.kr/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"&gt;&lt;item id=\"-1\" parentID=\"-1\" restricted=\"1\"&gt;&lt;dc:title&gt;still_1.ogg&lt;/dc:title&gt;&lt;upnp:class&gt;object.item.audioItem.musicTrack&lt;/upnp:class&gt;&lt;upnp:originalTrackNumber&gt;1&lt;/upnp:originalTrackNumber&gt;&lt;upnp:albumArtURI xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" dlna:profileID=\"JPEG_LRG\"&gt;http://192.168.112.237:9790/minimserver/*/music/unknown/Stille/still_1.flac/$!picture-176-167405.jpg&lt;/upnp:albumArtURI&gt;&lt;res bitrate=\"264600\" bitsPerSample=\"24\" duration=\"0:00:10.070\" nrAudioChannels=\"2\" protocolInfo=\"http-get:*:audio/x-flac:DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000\" sampleFrequency=\"44100\" size=\"640407\"&gt;http://192.168.112.237:9790/minimserver/*/music/unknown/Stille/still_1.flac&lt;/res&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;\"/>\n"
                + "  </InstanceID>\n" + "</Event>\n" + "";
        Document doc = av.getStAXParsedDocument(av.fixEscapeErrors(eventText), true);
        Element instanceID = av.getInstanceIDElement(doc);
        assertNotNull(instanceID);
    }

    @Test
    public void testNextTransportURIEvent()
    {
        AvTransportEventListener av = new AvTransportEventListener(null);
        String eventText = "<Event xmlns=\"urn:schemas-upnp-org:metadata-1-0/AVT/\">\n" + "  <InstanceID val=\"0\">\n"
                + "    <NextAVTransportURI val=\"http://192.168.112.237:9790/minimserver/*/music/unknown/Stille/still_2.flac\"/>\n"
                + "    <NextAVTransportURIMetaData val=\"&lt;DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:sec=\"http://www.sec.co.kr/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"&gt;&lt;item id=\"-1\" parentID=\"-1\" restricted=\"1\"&gt;&lt;dc:title&gt;still_2.flac&lt;/dc:title&gt;&lt;upnp:class&gt;object.item.audioItem.musicTrack&lt;/upnp:class&gt;&lt;upnp:albumArtURI xmlns:dlna=\"urn:schemas-dlna-org:metadata-1-0/\" dlna:profileID=\"JPEG_LRG\"&gt;http://192.168.112.237:9790/minimserver/*/music/unknown/Stille/still_2.flac/$!picture-138-167405.jpg&lt;/upnp:albumArtURI&gt;&lt;res bitrate=\"264600\" bitsPerSample=\"24\" duration=\"0:00:10.070\" nrAudioChannels=\"2\" protocolInfo=\"http-get:*:audio/x-flac:DLNA.ORG_OP=01;DLNA.ORG_FLAGS=01700000000000000000000000000000\" sampleFrequency=\"44100\" size=\"640407\"&gt;http://192.168.112.237:9790/minimserver/*/music/unknown/Stille/still_2.flac&lt;/res&gt;&lt;/item&gt;&lt;/DIDL-Lite&gt;\"/>\n"
                + "  </InstanceID>\n" + "</Event>";
        Document doc = av.getStAXParsedDocument(av.fixEscapeErrors(eventText), true);
        Element instanceID = av.getInstanceIDElement(doc);
        assertNotNull(instanceID);
    }
}
