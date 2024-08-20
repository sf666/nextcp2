package nextcp2.upnp.localdevice;

import nextcp.dto.MediaPlayerConfigDto;

public class LocalDeviceConfig {

	public enum FileType {
		ALBUM, SINGLE, COMPILATION
	};

	public String workdir = "";

	public FileType fileType = FileType.ALBUM;

	public Boolean overwrite = false;

	public String script = "";

	public LocalDeviceConfig() {
	}

	public LocalDeviceConfig(String workdir, FileType fileType, Boolean overwrite, String script) {
		super();
		this.workdir = workdir;
		this.fileType = fileType;
		this.overwrite = overwrite;
		this.script = script;
	}

	public MediaPlayerConfigDto asDto() {
		return new MediaPlayerConfigDto(workdir, script, fileType.toString(), overwrite);
	}

	@Override
	public String toString() {
		return "LocalDeviceConfig [workdir=" + workdir + ", fileType=" + fileType + ", overwrite=" + overwrite + ", script=" + script + "]";
	}

}
