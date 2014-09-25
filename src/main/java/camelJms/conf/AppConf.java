package camelJms.conf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration loader/holder
 * 
 * @author raul
 */
public final class AppConf {

	private static final AppConf INSTANCE = new AppConf();
	private static final Properties CONFIGURATION = new Properties();
	
	Logger logger = LoggerFactory.getLogger(AppConf.class);
	
	private AppConf() {}
	
	public static final AppConf getInstance() {
		return INSTANCE;
	}
	private static final Properties getConfiguration() {
		return CONFIGURATION;
	}
	
	public void initialize(final Path filePath) {
		
		InputStream inStream = null;
		try {
			inStream = new FileInputStream(filePath.toFile());
			CONFIGURATION.load(inStream);
		} catch (IOException ioe) {
			logger.error(ioe.toString());
		}
	}
	
	public String getConfValue(final String key) {
		return (String) getConfiguration().getProperty(key);
	}
}
