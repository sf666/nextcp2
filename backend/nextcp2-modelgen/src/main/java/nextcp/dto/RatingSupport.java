package nextcp.dto;

import java.util.List;

/**
 * ATTENTION: DO NOT MODIFY THIS CLASS. CLASS IS GENERATED AND WILL BE OVERWRITTEN
 */
public class RatingSupport
{

    public String musicRootPath;
    public String databaseFilename;
    public String supportedFileTypes;

    public RatingSupport()
    {
    }

    public RatingSupport(String musicRootPath, String databaseFilename, String supportedFileTypes)
    {
        this.musicRootPath = musicRootPath;
        this.databaseFilename = databaseFilename;
        this.supportedFileTypes = supportedFileTypes;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("RatingSupport [");
        sb.append("musicRootPath=").append(this.musicRootPath).append(", ");
        sb.append("databaseFilename=").append(this.databaseFilename).append(", ");
        sb.append("supportedFileTypes=").append(this.supportedFileTypes).append(", ");
        sb.append("]");
        return sb.toString();
    }

}