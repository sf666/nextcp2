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
        String userAgent = String.format("%s/%s UPnP/1.0 nextcp/2.0", SystemUtils.OS_NAME, SystemUtils.OS_VERSION);
        setRequestUserAgent(userAgent);
    }

}
