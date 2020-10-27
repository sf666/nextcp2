package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class GenericBooleanRequest
{

    public String deviceUDN;
    public Boolean value;

    public GenericBooleanRequest()
    {
    }

    public GenericBooleanRequest(String deviceUDN, Boolean value)
    {
        this.deviceUDN = deviceUDN;
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("GenericBooleanRequest [");
        sb.append("deviceUDN=").append(this.deviceUDN).append(", ");
        sb.append("value=").append(this.value).append(", ");
        sb.append("]");
        return sb.toString();
    }

}