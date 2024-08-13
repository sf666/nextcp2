package nextcp2.upnp.localdevice;


public interface IMediaPlayerCallback {
	
	public void skipToNextSong();
	public void durationChanged(long seconds);
	public void positionChanged(long seconds);
	public void finished(String text);
}
