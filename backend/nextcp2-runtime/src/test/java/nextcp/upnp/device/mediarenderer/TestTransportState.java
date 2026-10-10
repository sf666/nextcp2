package nextcp.upnp.device.mediarenderer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestTransportState {
    @Test
    void recognizesOpenHomeAndAvTransportStateCasing() {
        assertTrue(MediaRendererDevice.isPlayingState("Playing"));
        assertTrue(MediaRendererDevice.isPlayingState("PLAYING"));
        assertTrue(MediaRendererDevice.isPlayingState("Transitioning"));
        assertFalse(MediaRendererDevice.isPlayingState("Paused"));
        assertFalse(MediaRendererDevice.isPlayingState(null));
    }
}
