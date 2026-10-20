package com.saucedemo.utils.logs;

import com.saucedemo.config.Config;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * Fachada estática sobre log4j2. Consola (log4j2.properties) + archivo por ambiente y día:
 * logs/&lt;AMBIENTE&gt;/&lt;aaaa&gt;/&lt;mm&gt;/&lt;dd&gt;/test-automation.log, en modo append.
 */
public final class Log {

    private static final Logger LOGGER = init();

    private Log() {
    }

    /** Ruta del log del día para un ambiente. */
    public static Path logPath(String ambiente, LocalDate fecha) {
        return Paths.get("logs", ambiente,
                String.format("%04d", fecha.getYear()),
                String.format("%02d", fecha.getMonthValue()),
                String.format("%02d", fecha.getDayOfMonth()),
                "test-automation.log");
    }

    private static Logger init() {
        String ambiente = Config.ambiente(); // valida y pasa a mayúsculas; lanza excepción si no es DEV/TEST/PROD

        // Reconfigura para que la consola tome el ambiente (alguna librería pudo inicializar log4j2 antes)
        System.setProperty("log.ambiente", ambiente);
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        ctx.reconfigure();

        Configuration config = ctx.getConfiguration();
        FileAppender file = FileAppender.newBuilder()
                .setName("FileAppender")
                .withFileName(logPath(ambiente, LocalDate.now()).toString())
                .withAppend(true)
                .setLayout(PatternLayout.newBuilder()
                        .withConfiguration(config)
                        .withPattern("%d{dd-MM-yyyy HH:mm:ss.SSS} | " + ambiente + " | %-5level | [%t] %msg%n")
                        .build())
                .setConfiguration(config)
                .build();
        file.start();
        config.addAppender(file);
        config.getRootLogger().addAppender(file, null, null);
        ctx.updateLoggers();

        return LogManager.getLogger(Log.class);
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
