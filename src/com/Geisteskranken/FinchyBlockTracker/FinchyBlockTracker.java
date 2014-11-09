package com.Geisteskranken.FinchyBlockTracker;

import java.util.logging.Logger;

import net.canarymod.Canary;
import net.canarymod.plugin.Plugin;

public class FinchyBlockTracker extends Plugin {
	public static String host;
	public static String port;
	public static String database;
	public static String dbuser;
	public static String dbpass;
	public static boolean Track = true;

	public static String Name = "FinchyBlockTracker";
	public static String Version = "1.0";
	public static String Author = "Geistes ";
	
	public static Logger logger = Logger.getLogger(Name + " " + Version);

	@Override
	public void disable() {
		Track = false;
		logger.warning(Name + " " + Version + " disabled.");
	}

	@Override
	public boolean enable() {
		logger.info("Loading " + Name + " " + Version);
		Canary.hooks().registerListener(new FinchyEventHandler(), this);
		this.setName("FinchyBlockTracker");
		if (FinchyBlockTrackerConfig.readConfig()) {
			logger.info("Checking SQL DB...");
			if (FinchyBlockTrackerSQL.checkDB()) {
				logger.info("Checking SQL Table...");
				if (FinchyBlockTrackerSQL.checkTable()) {
					logger.info("Everything appears OK");
					Track = true;
					logger.info(Name + " " + Version + " " + "Enabled!");
				} else {
					disable();
					
				}
			} else {
				disable();
			}
		} else {
			disable();
		}
		return true;
	}
}
