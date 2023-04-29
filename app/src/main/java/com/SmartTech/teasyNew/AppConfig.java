package com.SmartTech.teasyNew;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by muddvayne on 29/03/2017.
 */

public class AppConfig {

    public static final long DELAY_BEFORE_PAUSED_APP_LOCKS = 5 * 60 * 1000;    //in millis

    public static final long INACTIVITY_TIMEOUT = 5 * 60 * 1000;

    private static final Properties properties;

    private static final Logger logger = Logger.getLogger(AppConfig.class.getCanonicalName());

    static {
        properties = new Properties();
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream("config.properties"));
            logger.info("Loaded app properties: " + properties);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load app properties file", e);
        }
    }


    public static Properties getProperties() {
        return properties;
    }
}
