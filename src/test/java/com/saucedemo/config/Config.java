package com.saucedemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Lee config.properties y permite sobreescribir cualquier clave con -Dclave=valor.
 */
public final class Config {

    private static final Properties PROPS = new Properties();
    private static final List<String> AMBIENTES = List.of("DEV", "TEST", "PROD");

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("No se encontró config.properties en el classpath");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Error leyendo config.properties", e);
        }
    }

    private Config() {
    }

    public static String get(String key) {
        String value = System.getProperty(key, PROPS.getProperty(key));
        if (value == null) {
            throw new IllegalArgumentException("Clave de configuración no definida: " + key);
        }
        return value.trim();
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    /** DEV, TEST o PROD (sin distinguir mayúsculas al pasarlo); cualquier otro valor lanza excepción. */
    public static String ambiente() {
        String ambiente = get("ambiente").toUpperCase(Locale.ROOT);
        if (!AMBIENTES.contains(ambiente)) {
            throw new IllegalArgumentException(
                    "ambiente='" + ambiente + "' no es válido. Valores permitidos: " + String.join(", ", AMBIENTES));
        }
        return ambiente;
    }

    /** -Dbase.url=... manda; si no, base.url.&lt;ambiente&gt; de config.properties. */
    public static String baseUrl() {
        String override = System.getProperty("base.url");
        return override != null ? override.trim() : get("base.url." + ambiente().toLowerCase(Locale.ROOT));
    }
}
