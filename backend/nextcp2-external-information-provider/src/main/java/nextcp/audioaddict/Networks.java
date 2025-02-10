package nextcp.audioaddict;

import java.util.ArrayList;
import java.util.List;
import nextcp.dto.RadioNetworkQuality;

/**
 * Audio Addict Networks 
 */
public enum Networks {

	CLASSIC_RADIO(	"Classical Radio", 	"http://listen.classicalradio.com",		"classicalradio", 
		new StreamListQuality[]{StreamListQuality.AAC_64, StreamListQuality.AAC_128, StreamListQuality.MP3_320}),
	RADIO_TUNES(	"Radio Tunes", 		"http://listen.radiotunes.com",			"radiotunes",
		new StreamListQuality[]{StreamListQuality.AAC_64, StreamListQuality.AAC_128, StreamListQuality.MP3_320}),
	ROCK_RADIO(		"Rock Radio", 		"http://listen.rockradio.com",			"rockradio",
		new StreamListQuality[]{StreamListQuality.AAC_64, StreamListQuality.AAC_128, StreamListQuality.MP3_320}),
	JAZZ_RADIO(		"Jazz Radio", 		"http://listen.jazzradio.com",			"jazzradio",
		new StreamListQuality[]{StreamListQuality.AAC_64, StreamListQuality.AAC_128, StreamListQuality.MP3_320}),
	ZEN_RADIO(		"Zen Radio", 		"http://listen.zenradio.com",			"zenradio",
		new StreamListQuality[]{StreamListQuality.AAC_64, StreamListQuality.AAC_128, StreamListQuality.MP3_320}),
	DI_FM(			"DI.fm", 			"http://listen.di.fm",					"di",
		new StreamListQuality[]{StreamListQuality.AAC_64, StreamListQuality.AAC_128, StreamListQuality.MP3_320});

	
	public String displayName = "";
	public String listenUrl = "";
	public String shortName = "";
	public StreamListQuality[] streamList;
	
	private static List<RadioNetworkQuality> streamListAsDto = null;

	Networks(String displayName, String listenUrl, String shortName, StreamListQuality[] streamList) {
		this.displayName = displayName;
		this.listenUrl = listenUrl;
		this.shortName = shortName;
		this.streamList = streamList;
	} 
	
	public synchronized List<RadioNetworkQuality> getStreamListQualityDto() {
		if (streamListAsDto == null) {
			streamListAsDto = new ArrayList<>();		
			for (StreamListQuality streamListQuality : streamList) {
				streamListAsDto.add(new RadioNetworkQuality(streamListQuality.name(), streamListQuality.displayName));
			}
		}
		return streamListAsDto;
	}
}
