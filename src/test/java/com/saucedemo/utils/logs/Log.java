package com.saucedemo.utils.logs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Fachada estática sobre log4j2 (configuración en src/test/resources/log4j2.properties).
 */
public final class Log {

    private static final Logger LOGGER = LogManager.getLogger(Log.class);

    private Log() {
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void error(String message, Throwable t) {
        LOGGER.error(message, t);
    }

    public static void fatal(String message) {
        LOGGER.fatal(message);
    }

    public static void debug(String message) {
        LOGGER.debug(message);
    }
}
