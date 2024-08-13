package nextcp2.upnp.localdevice;


public interface IMediaPlayer {
	
	void play(String uri, String currentTrackMeta, LocalDeviceConfig config);
	
	void pause();
	void stop();
}
