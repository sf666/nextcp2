package nextcp.domainmodel.device.services;

import nextcp.dto.TransportServiceStateDto;

public interface ITransport
{

    /**
     * Starts playing
     */
    void play();

    void pause();

    void stop();

    void next();

    TransportServiceStateDto getCurrentTransportServiceState();
}