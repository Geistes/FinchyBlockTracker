package com.Geisteskranken.FinchyBlockTracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class FinchyBlockTrackerConfig {

	private static File f;
	static Properties prop = new Properties();
	static OutputStream output = null;

	public static boolean readConfig() {

		f = new File("plugins/FinchyBlockTracker/FinchyBlockTracker.conf");
		Properties prop = new Properties();
		InputStream input = null;

		if (f.exists() && !f.isDirectory()) {
			try {
				input = new FileInputStream(f);

				prop.load(input);

				FinchyBlockTracker.host = prop.getProperty("host");
				FinchyBlockTracker.port = prop.getProperty("port");
				FinchyBlockTracker.database = prop.getProperty("database");
				FinchyBlockTracker.dbuser = prop.getProperty("dbuser");
				FinchyBlockTracker.dbpass = prop.getProperty("dbpass");
			} catch (IOException ex) {
				FinchyBlockTracker.logger
						.warning("Disabled! Configuration error.");
			}
			try {
				input.close();
			} catch (IOException e) {
				FinchyBlockTracker.logger
						.warning("Disabled! Configuration error.");
				return false;
			}

			FinchyBlockTracker.logger.info("Config: OK");
			return true;
		} else {
			createConfig();
			return false;
		}

	}

	public static void createConfig() {
		try {

			f.getParentFile().mkdirs();
			f.createNewFile();
			output = new FileOutputStream(f);

			prop.setProperty("host", "localhost");
			prop.setProperty("port", "3306");
			prop.setProperty("database", "blocktracker");
			prop.setProperty("dbuser", "username");
			prop.setProperty("dbpass", "pasword");

			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
					FinchyBlockTracker.logger
							.warning("Configuration file created. Please edit and restart server");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
