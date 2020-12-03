package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class GenericStringRequest
{

    public String deviceUDN;
    public String value;

    public GenericStringRequest()
    {
    }

    public GenericStringRequest(String deviceUDN, String value)
    {
        this.deviceUDN = deviceUDN;
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("GenericStringRequest [");
        sb.append("deviceUDN=").append(this.deviceUDN).append(", ");
        sb.append("value=").append(this.value).append(", ");
        sb.append("]");
        return sb.toString();
    }

}