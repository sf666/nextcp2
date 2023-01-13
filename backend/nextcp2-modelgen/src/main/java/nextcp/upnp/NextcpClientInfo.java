package nextcp.upnp;

import org.jupnp.model.message.UpnpHeaders;
import org.jupnp.model.profile.ClientInfo;


public class NextcpClientInfo extends ClientInfo
{

    public NextcpClientInfo()
    {
        setUserAgent();
    }

    public NextcpClientInfo(UpnpHeaders requestHeaders)
    {
        super(requestHeaders);
        setUserAgent();
    }

    private void setUserAgent()
    {
        String userAgent = String.format("nextcp/2");
        setRequestUserAgent(userAgent);
    }

}
