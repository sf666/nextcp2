package nextcp2.upnp.localdevice;

public class LocalDeviceConfig {

    public enum FileType
    {
        ALBUM,
        SINGLE,
        COMPILATION
    };

    public String workdir = "";

    public FileType fileType = FileType.ALBUM;

    public Boolean overwrite = false;

    public String script = "";

}
