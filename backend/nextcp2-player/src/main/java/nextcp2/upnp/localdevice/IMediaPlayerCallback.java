package nextcp2.upnp.localdevice;

import java.io.File;

public interface IMediaPlayerCallback {
	
	public void skipToNextSong();
	public void durationChanged(long seconds);
	public void positionChanged(long seconds);
	public void stopped(File text);
}
