package camelJms.conf;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import camelJms.Route;

/**
 * Configuration auto reload
 *
 * @author raul
 */
public final class ConfChangeListener implements Runnable {

	private String configFileName = null;
	private Path filePath = null;
	private Route route;

	Logger logger = LoggerFactory.getLogger(ConfChangeListener.class);

	public ConfChangeListener(final Path filePath) {

		this.filePath = filePath;
	}

	@Override
	public void run() {

		register(this.filePath);
	}

	private void register(final Path filePath) {

		this.configFileName = filePath.getFileName().toString();

		confChanged(filePath);
		try {
			startWatcher(filePath);
		} catch(final IOException ioe) {
			logger.error(ioe.toString());
		}
	}

	private void startWatcher(final Path filePath) throws IOException {

		final WatchService watchService = FileSystems.getDefault().newWatchService();
		filePath.getParent().register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

		WatchKey watchKey = null;
		while(true) {

			try {
				watchKey = watchService.take();
				for(final WatchEvent<?> watchEvent: watchKey.pollEvents()) {
					if(watchEvent.context().toString().equals(configFileName)) {
						confChanged(filePath);
					}
				}

				final boolean reset = watchKey.reset();
				if(!reset) {
					logger.warn("Could not reset the watch key!");
					break;
				}
			} catch(final InterruptedException ie) {
				logger.error(ie.toString());
			}
		}
		if(watchService != null) {
			watchService.close();
		}
	}

	public void confChanged(final Path filePath) {

		logger.info("Refreshing the configuration.");
		AppConf.getInstance().initialize(filePath);
		try {

			this.route.loadRoutes();
		} catch(Exception e) {

			logger.error("Error in loading routes: " + e);
		}
	}

	public void setRoute(final Route route) {

		this.route = route;
	}
}
