package nextcp2.upnp.localdevice;


public interface IMediaPlayerFactory {
	IMediaPlayer createPlayer(IMediaPlayerCallback callback);
	String getFactoryType();
}