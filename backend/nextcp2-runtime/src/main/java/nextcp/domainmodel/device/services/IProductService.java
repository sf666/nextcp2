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

    /**
     * Switches the renderer's active source to the first source of the given type (e.g. "Radio"),
     * matched case-insensitively against {@link InputSourceDto#Type}. No-op if no such source.
     */
    void switchToSource(String type);
}
