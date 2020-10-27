package nextcp.domainmodel.device.services;

import java.util.LinkedList;

import nextcp.dto.InputSourceDto;

public interface IProductService
{

    LinkedList<InputSourceDto> getSourceList();

    InputSourceDto getCurrentInputSource();

    InputSourceDto getInputSource(Long value);

    void setStandby(boolean standbyState);

    boolean getStandby();
}
