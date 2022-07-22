package nextcp.upnp;

import org.apache.commons.lang.SystemUtils;
import org.fourthline.cling.model.message.UpnpHeaders;
import org.fourthline.cling.model.profile.ClientInfo;

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
        String userAgent = String.format("nextcp/2.0");
        setRequestUserAgent(userAgent);
    }

}
