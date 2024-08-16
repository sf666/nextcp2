package nextcp.mediaplayer;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import nextcp.dto.Config;
import nextcp2.upnp.localdevice.IMediaPlayerFactory;

@Component
public class MediaPlayerDiscoveryService {

	private static final Logger log = LoggerFactory.getLogger(MediaPlayerDiscoveryService.class.getName());

	@Autowired
	Config config = null;

	private List<IMediaPlayerFactory> availableMediaPlayer = new ArrayList<>();

	public List<IMediaPlayerFactory> getAvailableMediaPlayerFactories() {
		return availableMediaPlayer;
	}

	@PostConstruct
	public void init() {
		try {
			log.info("searching for media player in directory : " + config.applicationConfig.libraryPath);
			File loc = new File(config.applicationConfig.libraryPath);

			File[] flist = loc.listFiles(new FileFilter() {

				public boolean accept(File file) {
					boolean result = ((file.getName().startsWith("mp-")) && (file.getName().toLowerCase().endsWith(".jar"))); 
					return result;
				}
			});

			loadMediaPlayer(flist);
		} catch (Exception e) {
			log.error("ServiceLoader Error", e);
		}
	}

	private void loadMediaPlayer(File[] flist) throws MalformedURLException {
		if (flist != null) {
			List<URL> urls = new ArrayList<>();

			for (int i = 0; i < flist.length; i++) {
				urls.add(flist[i].toURI().toURL());
			}

			URLClassLoader ucl = new URLClassLoader(urls.toArray(new URL[0]), this.getClass().getClassLoader());

			ServiceLoader<IMediaPlayerFactory> loader = ServiceLoader.load(IMediaPlayerFactory.class, ucl);
			for (IMediaPlayerFactory factory : loader) {
				availableMediaPlayer.add(factory);
				log.info(String.format("Found media player factory backend : %s ", factory.getFactoryType()));
			}
		} else {
			log.debug("no device driver available.");
		}
	}
	
	public IMediaPlayerFactory getFirstFactory() {
		if (availableMediaPlayer.size() > 0) {
			return availableMediaPlayer.get(0);
		} else {
			return null;
		}
	}
}
