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
import jakarta.annotation.PostConstruct;
import nextcp.dto.Config;

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
			log.info("searching for device driver in directory : " + config.applicationConfig.libraryPath);
			File loc = new File(config.applicationConfig.libraryPath);

			File[] flist = loc.listFiles(new FileFilter() {

				public boolean accept(File file) {
					return file.getPath().toLowerCase().endsWith(".jar");
				}
			});

			loadMediaPlayer(flist);
		} catch (Exception e) {
			log.error("ServiceLoader Error", e);
		}
	}

	private void loadMediaPlayer(File[] flist) throws MalformedURLException {
		if (flist != null) {
			URL[] urls = new URL[flist.length];

			for (int i = 0; i < flist.length; i++) {
				if (flist[i].getName().startsWith("mp-")) {
					urls[i] = flist[i].toURI().toURL();
				}
			}

			URLClassLoader ucl = new URLClassLoader(urls, this.getClass().getClassLoader());

			ServiceLoader<IMediaPlayerFactory> loader = ServiceLoader.load(IMediaPlayerFactory.class, ucl);
			for (IMediaPlayerFactory factory : loader) {
				availableMediaPlayer.add(factory);
			}
		} else {
			log.debug("no device driver available.");
		}
	}
}
