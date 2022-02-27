/**
 * Project: A01258927 Assignment 01
 * File: CustomerReader.java
 */

package a01258927;

/**
 * @author Dalvir Chiount, A01258927
 */
import java.awt.EventQueue;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

public class Assignment01 {

	private static final Instant startTime = Instant.now();
	private static final String LOG4J_CONFIG_FILENAME = "log4j2.xml";
	
	static {
		configureLogging();
	}

	private static final Logger LOG = LogManager.getLogger();

	private static void configureLogging() {
		ConfigurationSource source;
		try {
			source = new ConfigurationSource(new FileInputStream(LOG4J_CONFIG_FILENAME));
			Configurator.initialize(null, source);
		} catch (IOException e) {
			System.out.println(String.format("Can't find the log4j logging configuration file %s.", LOG4J_CONFIG_FILENAME));
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		LOG.info("Start: " + startTime);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AssignmentFrame window = new AssignmentFrame();
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Instant endTime = Instant.now();
					LOG.info("End: " + endTime);
					LOG.info(String.format("Duration: %d ms", Duration.between(startTime, endTime).toMillis()));
				}
			}
		});
	}

	

}
