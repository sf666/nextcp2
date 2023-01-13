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



public abstract class ActionCallback
{

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
                throw new GenActionException(GenActionException.ACTION_ERROR, "error on action : " + actionInvocation.getFailure());
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
                throw new GenActionException(GenActionException.ACTION_ERROR, "error on action : " + actionInvocation.getFailure());
            }
            else if (response.getOperation().isFailed())
            {
                throw new GenActionException(GenActionException.ACTION_FAILED,
                        String.format("%s : %s", remoteService.getDevice().getDisplayString(), response.getOperation().toString()));
            }
        }

        return actionInvocation;
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
