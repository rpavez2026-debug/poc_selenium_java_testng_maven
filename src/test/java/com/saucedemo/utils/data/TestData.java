package com.saucedemo.utils.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Datos de prueba por CP desde src/test/resources/utils.json (un bloque por clase CP).
 * El archivo se lee una sola vez; si falta un CP o un dato, falla con un mensaje claro en vez de devolver null.
 */
public final class TestData {

    private static final String FILE = "utils.json";
    private static final JsonNode ROOT = load();
    private static final ThreadLocal<String> CURRENT_CP = new ThreadLocal<>();

    private TestData() {
    }

    private static JsonNode load() {
        try (InputStream in = TestData.class.getClassLoader().getResourceAsStream(FILE)) {
            if (in == null) {
                throw new IllegalStateException("No se encontró " + FILE + " en el classpath");
            }
            return new ObjectMapper().readTree(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Error leyendo " + FILE, e);
        }
    }

    /** Lo llama TestListener al comenzar cada test con el nombre de la clase CP. */
    public static void iniciar(String cp) {
        CURRENT_CP.set(cp);
    }

    /** Dato del CP que se está ejecutando. */
    public static String get(String key) {
        return get(currentCp(), key);
    }

    public static String get(String cp, String key) {
        return node(cp, key).asText();
    }

    /** Lista de valores, p. ej. para un DataProvider (que corre antes de iniciar el test, así que va con el CP explícito). */
    public static List<String> getList(String cp, String key) {
        JsonNode node = node(cp, key);
        if (!node.isArray()) {
            throw new IllegalArgumentException("El dato '" + key + "' de " + cp + " en " + FILE + " no es una lista");
        }
        List<String> values = new ArrayList<>();
        node.forEach(n -> values.add(n.asText()));
        return values;
    }

    private static JsonNode node(String cp, String key) {
        JsonNode block = ROOT.get(cp);
        if (block == null) {
            throw new IllegalArgumentException("Falta el bloque \"" + cp + "\" en " + FILE);
        }
        JsonNode value = block.get(key);
        if (value == null) {
            throw new IllegalArgumentException("Falta el dato '" + key + "' en " + cp + " de " + FILE);
        }
        return value;
    }

    private static String currentCp() {
        String cp = CURRENT_CP.get();
        if (cp == null) {
            throw new IllegalStateException("No hay CP en ejecución; usa TestData.get(cp, clave)");
        }
        return cp;
    }
}
