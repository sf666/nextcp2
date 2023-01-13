package nextcp.upnp;

import org.jupnp.model.UnsupportedDataException;
import org.jupnp.model.gena.CancelReason;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.state.StateVariableValue;

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
