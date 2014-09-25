package camelJms;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import camelJms.conf.ConfChangeListener;

/**
 * Main class
 *
 * @author raul
 */
public final class Main {

	final String DEFAULT_CONF_LOCATION = "src/main/resources/config.properties";
	final String CONF_ENV_VAR = "camelJms-conf";

	final CamelContext camelContext = new DefaultCamelContext();

	Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) throws Exception {

		new Main().init(args);
	}

	// Setting up MQ connection and Camel route
	private final void init(final String[] args) throws Exception {

		camelContext.start();

		Path confPath = getConfPath(args);
		final ConfChangeListener confChangeListener = new ConfChangeListener(confPath);
		Route route = new Route(camelContext, confChangeListener);
		confChangeListener.setRoute(route);
		new Thread(confChangeListener).start();

		logger.info("Everything up and running!");
	}

	/**
	 * Searches conf file
	 *
	 * @param args - main args array
	 * @return - Path to conf location
	 */
	private Path getConfPath(final String[] args) {

		Path confPath = null;

		// First let's check the environment
		final String confEnv = System.getenv(CONF_ENV_VAR);
		if(confEnv != null && ! confEnv.isEmpty()) {
			confPath = Paths.get(confEnv);
		}

		if(confPath == null || ! Files.isRegularFile(confPath)) {
			// Secondly we look if conf path is specified in command line
			final Map<String, String> paramMap = parseArgs(args);
			if(paramMap.containsKey("conf")) {
				confPath = Paths.get(paramMap.get("conf"));
			}
			// Use default conf location
			if(confPath == null || ! Files.isRegularFile(confPath)) {
				confPath = Paths.get(DEFAULT_CONF_LOCATION);
			}
		}

		logger.info("Using conf from: " + confPath.toString());

		return confPath;
	}

	/**
	 * Parses command line arguments
	 *
	 * @param args - main args array
	 * @return - Map of recognized conf parameters
	 */
	private Map<String, String> parseArgs(final String[] args) {

		final Map<String, String> paramMap = new HashMap<String, String>();

		for(final String strArg : args) {

			if(strArg.startsWith("--conf=")) {
				paramMap.put("conf", strArg.substring(7));
			}
		}

		return paramMap;
	}
}
