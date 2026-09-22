package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class DeviceDetailsDto
{

    public String udn;
    public String friendlyName;
    public String deviceType;
    @nextcp.handcoded.Nullable
    public String manufacturer;
    @nextcp.handcoded.Nullable
    public String manufacturerUrl;
    @nextcp.handcoded.Nullable
    public String modelName;
    @nextcp.handcoded.Nullable
    public String modelNumber;
    @nextcp.handcoded.Nullable
    public String modelDescription;
    @nextcp.handcoded.Nullable
    public String serialNumber;
    @nextcp.handcoded.Nullable
    public String presentationUrl;
    @nextcp.handcoded.Nullable
    public String descriptorUrl;
    @nextcp.handcoded.Nullable
    public String ipAddress;
    public Boolean mediaServer;
    public List<String> features;
    @nextcp.handcoded.Nullable
    public String searchCaps;
    public List<DeviceServiceDto> services;

    public DeviceDetailsDto()
    {
    }

    public DeviceDetailsDto(String udn, String friendlyName, String deviceType, String manufacturer, String manufacturerUrl, String modelName, String modelNumber, String modelDescription, String serialNumber, String presentationUrl, String descriptorUrl, String ipAddress, Boolean mediaServer, List<String> features, String searchCaps, List<DeviceServiceDto> services)
    {
        this.udn = udn;
        this.friendlyName = friendlyName;
        this.deviceType = deviceType;
        this.manufacturer = manufacturer;
        this.manufacturerUrl = manufacturerUrl;
        this.modelName = modelName;
        this.modelNumber = modelNumber;
        this.modelDescription = modelDescription;
        this.serialNumber = serialNumber;
        this.presentationUrl = presentationUrl;
        this.descriptorUrl = descriptorUrl;
        this.ipAddress = ipAddress;
        this.mediaServer = mediaServer;
        this.features = features;
        this.searchCaps = searchCaps;
        this.services = services;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("DeviceDetailsDto [");
        sb.append("udn=").append(this.udn).append(", ");
        sb.append("friendlyName=").append(this.friendlyName).append(", ");
        sb.append("deviceType=").append(this.deviceType).append(", ");
        sb.append("manufacturer=").append(this.manufacturer).append(", ");
        sb.append("manufacturerUrl=").append(this.manufacturerUrl).append(", ");
        sb.append("modelName=").append(this.modelName).append(", ");
        sb.append("modelNumber=").append(this.modelNumber).append(", ");
        sb.append("modelDescription=").append(this.modelDescription).append(", ");
        sb.append("serialNumber=").append(this.serialNumber).append(", ");
        sb.append("presentationUrl=").append(this.presentationUrl).append(", ");
        sb.append("descriptorUrl=").append(this.descriptorUrl).append(", ");
        sb.append("ipAddress=").append(this.ipAddress).append(", ");
        sb.append("mediaServer=").append(this.mediaServer).append(", ");
        sb.append("features=").append(this.features).append(", ");
        sb.append("searchCaps=").append(this.searchCaps).append(", ");
        sb.append("services=").append(this.services).append(", ");
        sb.append("]");
        return sb.toString();
    }

}