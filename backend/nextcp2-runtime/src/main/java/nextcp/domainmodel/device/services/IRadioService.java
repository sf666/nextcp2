package nextcp.domainmodel.device.services;

import java.util.List;

import nextcp.dto.MusicItemDto;

public interface IRadioService
{

    List<MusicItemDto> getRadioStations();

    void play(MusicItemDto radioStation);

    void pause();
    
    void stop();
}
