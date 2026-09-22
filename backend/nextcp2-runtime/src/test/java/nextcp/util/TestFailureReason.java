package nextcp.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import nextcp.upnp.GenActionException;

/**
 * What a user is told when an action failed. The cases here are the ones that reached a toast
 * unreadable at some point: a whole SOAP envelope, an empty string, and the device's one useful
 * sentence buried behind the context nextcp wrapped around it.
 */
public class TestFailureReason
{
    /** What a media server answers when the song is already referenced by that playlist. */
    private static final String FAULT_BODY = """
            <?xml version="1.0"?>
            <s:Envelope xmlns:s="http://schemas.xmlsoap.org/soap/envelope/">
              <s:Body>
                <s:Fault>
                  <detail>
                    <UPnPError xmlns="urn:schemas-upnp-org:control-1-0">
                      <errorCode>501</errorCode>
                      <errorDescription>entry already in Playlist.</errorDescription>
                    </UPnPError>
                  </detail>
                </s:Fault>
              </s:Body>
            </s:Envelope>""";

    /** Built the way ActionCallback builds it: log wording outside, the device's own words alongside. */
    private static GenActionException deviceRefused(String deviceReason)
    {
        return new GenActionException(GenActionException.ACTION_FAILED,
                "device MusicServer (BS) rejected action CreateReference : 501 : " + deviceReason,
                deviceReason);
    }

    @Test
    public void deviceReasonReplacesTheWrappingItArrivedIn()
    {
        String reason = FailureReason.of(deviceRefused("entry already in Playlist."));

        assertEquals("entry already in Playlist.", reason);
    }

    @Test
    public void anActionTheDeviceNeverOfferedIsNotReportedAsAFailure()
    {
        GenActionException notOffered = new GenActionException(GenActionException.ACTION_NOT_SUPPORTED,
                "device does not offer action SearchRadioStations of service UmsExtendedServices");

        String reason = FailureReason.of(notOffered);

        assertEquals("This media server does not offer that function.", reason);
        assertTrue(!reason.contains("SearchRadioStations"), "the action name is our vocabulary, not the user's");
    }

    @Test
    public void whatTheUserSeesCarriesNoDeviceNameActionNameOrErrorCode()
    {
        GenActionException fromDevice = deviceRefused("entry already in Playlist.");
        BackendException wrapped = new BackendException(BackendException.DIDL_PARSE_ERROR,
                FailureReason.of(fromDevice), fromDevice);

        String shown = FailureReason.describe("Cannot add the song to the playlist", wrapped);

        assertEquals("Cannot add the song to the playlist : entry already in Playlist.", shown);
        assertTrue(!shown.contains("501"), shown);
        assertTrue(!shown.contains("rejected action"), shown);
        assertTrue(!shown.contains("MusicServer"), shown);
    }

    @Test
    public void withoutADeviceDescriptionTheTechnicalSentenceIsAllThereIs()
    {
        // Nothing more precise exists, so dropping the wrapping would leave an empty toast.
        GenActionException noReason = new GenActionException(GenActionException.ACTION_FAILED,
                "device MusicServer (BS) rejected action CreateReference : 501", "");

        assertEquals("device MusicServer (BS) rejected action CreateReference : 501",
                FailureReason.of(noReason));
    }

    @Test
    public void aFaultBodyThatReachedTheExceptionIsUnwrappedRatherThanShownAsXml()
    {
        // A device answering outside ActionCallback has no deviceReason filled in.
        GenActionException raw = new GenActionException(GenActionException.ACTION_FAILED, FAULT_BODY);

        assertEquals("entry already in Playlist.", FailureReason.of(raw));
    }

    @Test
    public void theDeviceOutranksAnOuterExceptionEvenWhenThatOneHasAMessage()
    {
        GenActionException fromDevice = deviceRefused("entry already in Playlist.");

        assertEquals("entry already in Playlist.",
                FailureReason.of(new RuntimeException("adding song to server playlist", fromDevice)));
    }

    @Test
    public void aSpringStatusExceptionReportsItsReasonWithoutTheStatusLine()
    {
        ResponseStatusException status = new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,
                "please provide output device (media-renderer).");

        assertEquals("please provide output device (media-renderer).", FailureReason.of(status));
    }

    @Test
    public void aFailureWithNothingToSayIsNamedRatherThanLeftBlank()
    {
        assertEquals("IllegalStateException", FailureReason.of(new IllegalStateException()));
    }

    @Test
    public void aSelfReferencingCauseChainTerminates()
    {
        RuntimeException loop = new RuntimeException("outer")
        {
            private static final long serialVersionUID = 1L;

            @Override
            public synchronized Throwable getCause()
            {
                return this;
            }
        };

        assertEquals("outer", FailureReason.of(loop));
    }
}
