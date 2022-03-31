package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 *
 * Template: javadataclass.ftl
 *   
 */
public class NextcpFileConfigDto
{

    public String libraryPath;
    public Integer embeddedServerPort;
    public String databaseFilename;

    public NextcpFileConfigDto()
    {
    }

    public NextcpFileConfigDto(String libraryPath, Integer embeddedServerPort, String databaseFilename)
    {
        this.libraryPath = libraryPath;
        this.embeddedServerPort = embeddedServerPort;
        this.databaseFilename = databaseFilename;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("NextcpFileConfigDto [");
        sb.append("libraryPath=").append(this.libraryPath).append(", ");
        sb.append("embeddedServerPort=").append(this.embeddedServerPort).append(", ");
        sb.append("databaseFilename=").append(this.databaseFilename).append(", ");
        sb.append("]");
        return sb.toString();
    }

}