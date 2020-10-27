package nextcp.upnp;

import org.fourthline.cling.model.UnsupportedDataException;
import org.fourthline.cling.model.gena.CancelReason;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.RemoteService;
import org.fourthline.cling.model.state.StateVariableValue;

public interface ISubscriptionEventListener
{

    void invalidMessage(UnsupportedDataException ex);

    void failed(UpnpResponse responseStatus);

    void ended(CancelReason reason, UpnpResponse responseStatus);

    void eventsMissed(int numberOfMissedEvents);

    void established();

    void eventReceived(String key, StateVariableValue<RemoteService> stateVar);

    void eventProcessed();
}
