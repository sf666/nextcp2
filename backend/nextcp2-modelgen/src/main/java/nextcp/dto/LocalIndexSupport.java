package nextcp.dto;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class LocalIndexSupport
{

    public Boolean isActive;
    public String musicRootPath;
    public String databaseFilename;
    public String supportedFileTypes;

    public LocalIndexSupport()
    {
    }

    public LocalIndexSupport(Boolean isActive, String musicRootPath, String databaseFilename, String supportedFileTypes)
    {
        this.isActive = isActive;
        this.musicRootPath = musicRootPath;
        this.databaseFilename = databaseFilename;
        this.supportedFileTypes = supportedFileTypes;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("LocalIndexSupport [");
        sb.append("isActive=").append(this.isActive).append(", ");
        sb.append("musicRootPath=").append(this.musicRootPath).append(", ");
        sb.append("databaseFilename=").append(this.databaseFilename).append(", ");
        sb.append("supportedFileTypes=").append(this.supportedFileTypes).append(", ");
        sb.append("]");
        return sb.toString();
    }

}