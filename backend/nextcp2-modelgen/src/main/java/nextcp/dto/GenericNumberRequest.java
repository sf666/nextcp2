package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class GenericNumberRequest
{

    public String deviceUDN;
    public Long value;

    public GenericNumberRequest()
    {
    }

    public GenericNumberRequest(String deviceUDN, Long value)
    {
        this.deviceUDN = deviceUDN;
        this.value = value;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("GenericNumberRequest [");
        sb.append("deviceUDN=").append(this.deviceUDN).append(", ");
        sb.append("value=").append(this.value).append(", ");
        sb.append("]");
        return sb.toString();
    }

}