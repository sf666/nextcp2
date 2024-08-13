package nextcp2.upnp.localdevice;


public interface IMediaPlayerCallback {
	public void durationChanged(long seconds);
	public void positionChanged(long seconds);
}
