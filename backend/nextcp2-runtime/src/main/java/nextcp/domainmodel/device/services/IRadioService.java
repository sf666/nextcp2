package nextcp.domainmodel.device.services;

import java.util.List;

import nextcp.dto.MusicItemDto;

public interface IRadioService
{

    List<MusicItemDto> getRadioStations();

    void play(MusicItemDto radioStation);

    /**
     * Plays an arbitrary stream URL (not a renderer preset) via the Radio service, using
     * SetChannel(Uri, Metadata) so the renderer shows the supplied DIDL metadata and consumes any
     * ICY in-band metadata of the stream.
     */
    void playStream(String uri, String metadata);

    void pause();

    void stop();
}
