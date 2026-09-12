package nextcp.upnp;

import java.net.URL;
import org.jupnp.controlpoint.ControlPoint;
import org.jupnp.model.action.ActionException;
import org.jupnp.model.action.ActionInvocation;
import org.jupnp.model.message.UpnpResponse;
import org.jupnp.model.message.control.IncomingActionResponseMessage;
import org.jupnp.model.meta.LocalService;
import org.jupnp.model.meta.RemoteService;
import org.jupnp.model.meta.Service;
import org.jupnp.protocol.sync.SendingAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public abstract class ActionCallback
{
	private static final Logger log = LoggerFactory.getLogger(ActionCallback.class.getName());
	
    protected final ActionInvocation actionInvocation;

    protected ControlPoint controlPoint;

    protected ActionCallback(ActionInvocation actionInvocation, ControlPoint controlPoint)
    {
        this.actionInvocation = actionInvocation;
        this.controlPoint = controlPoint;
    }

    protected ActionCallback(ActionInvocation actionInvocation)
    {
        this.actionInvocation = actionInvocation;
    }

    public ActionInvocation getActionInvocation()
    {
        return actionInvocation;
    }

    synchronized public ControlPoint getControlPoint()
    {
        return controlPoint;
    }

    synchronized public ActionCallback setControlPoint(ControlPoint controlPoint)
    {
        this.controlPoint = controlPoint;
        return this;
    }

    protected ActionInvocation execute()
    {
        Service service = actionInvocation.getAction().getService();

        // Local execution
        if (service instanceof LocalService)
        {
            LocalService localService = (LocalService) service;

            // Executor validates input inside the execute() call immediately
            localService.getExecutor(actionInvocation.getAction()).execute(actionInvocation);

            if (actionInvocation.getFailure() != null)
            {
                throw new GenActionException(GenActionException.ACTION_ERROR, "action " + actionInvocation.getAction().getName()
                        + " failed : " + actionInvocation.getFailure());
            }
        }
        else if (service instanceof RemoteService)
        {
            if (getControlPoint() == null)
            {
                throw new IllegalStateException("Callback must be executed through ControlPoint");
            }

            RemoteService remoteService = (RemoteService) service;

            URL controLURL;
            try
            {
                controLURL = remoteService.getDevice().normalizeURI(remoteService.getControlURI());
            }
            catch (IllegalArgumentException e)
            {
                throw new GenActionException(GenActionException.ACTION_BAD_CONTROL_URL, remoteService.getControlURI().toString());
            }

            SendingAction prot = getControlPoint().getProtocolFactory().createSendingAction(actionInvocation, controLURL);
            prot.run();

            IncomingActionResponseMessage response = prot.getOutputMessage();

            if (response == null)
            {
                // No HTTP response at all: the device is unreachable (powered off / standby) while
                // jUPnP still holds it in its registry until the SSDP lease expires.
                String msg = "device " + deviceName(remoteService) + " did not respond to action "
                    + actionInvocation.getAction().getName() + " (no HTTP response from " + controLURL
                    + ") - device may be switched off or in standby"
                    + (actionInvocation.getFailure() != null ? " : " + actionInvocation.getFailure().getMessage() : "");
                log.warn("{}", msg);
                throw new GenActionException(GenActionException.ACTION_ERROR, msg);
            }
            else if (response.getOperation().isFailed())
            {
            	log.error("UPnP error for device {} : {}" , remoteService.getDevice().getDisplayString(), response.getBodyString());
                throw new GenActionException(GenActionException.ACTION_FAILED, "device " + deviceName(remoteService)
                    + " rejected action " + actionInvocation.getAction().getName() + " : " + response.getBodyString());
            }
        }

        return actionInvocation;
    }

    /** Friendly name of the device behind a service, falling back to the UDN based display string. */
    private static String deviceName(RemoteService service)
    {
        if (service.getDevice() != null && service.getDevice().getDetails() != null
                && service.getDevice().getDetails().getFriendlyName() != null)
        {
            return service.getDevice().getDetails().getFriendlyName();
        }
        return service.getDevice() != null ? service.getDevice().getDisplayString() : "<unknown>";
    }

    protected String createDefaultFailureMessage(ActionInvocation invocation, UpnpResponse operation)
    {
        String message = "Error: ";
        final ActionException exception = invocation.getFailure();
        if (exception != null)
        {
            message = message + exception.getMessage();
        }
        if (operation != null)
        {
            message = message + " (HTTP response was: " + operation.getResponseDetails() + ")";
        }
        return message;
    }

    @Override
    public String toString()
    {
        return "(ActionCallback) " + actionInvocation;
    }
}
