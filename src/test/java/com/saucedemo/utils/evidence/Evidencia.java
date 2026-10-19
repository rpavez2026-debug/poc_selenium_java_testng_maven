package com.saucedemo.utils.evidence;

import com.aventstack.extentreports.ExtentTest;
import com.saucedemo.driver.DriverFactory;
import com.saucedemo.utils.reports.ExtentTestManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Evidencias por paso: guarda la captura en target/evidencias/&lt;CP&gt;/NN_mensaje.png
 * y la adjunta al reporte Extent.
 */
public final class Evidencia {

    private static final Path BASE_DIR = Paths.get("target", "evidencias");
    private static final ThreadLocal<String> TEST_NAME = new ThreadLocal<>();
    private static final ThreadLocal<Integer> STEP = ThreadLocal.withInitial(() -> 0);

    private Evidencia() {
    }

    /** Lo llama TestListener al comenzar cada test. */
    public static void iniciar(String testName) {
        TEST_NAME.set(testName);
        STEP.set(0);
    }

    public static void registrarPaso(String mensaje) {
        String base64 = capturaBase64();
        ExtentTest test = ExtentTestManager.getTest();
        if (base64 == null) {
            if (test != null) test.info(mensaje);
            return;
        }

        int paso = STEP.get() + 1;
        STEP.set(paso);
        String archivo = String.format("%02d_%s.png", paso, mensaje.replaceAll("[^\\p{L}\\p{N}]+", "_"));
        Path destino = BASE_DIR.resolve(TEST_NAME.get() == null ? "sin_nombre" : TEST_NAME.get()).resolve(archivo);
        try {
            Files.createDirectories(destino.getParent());
            Files.write(destino, java.util.Base64.getDecoder().decode(base64));
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar la evidencia " + destino, e);
        }

        if (test != null) {
            test.info(mensaje, test.addScreenCaptureFromBase64String(base64).getModel().getMedia().get(0));
        }
    }

    /** Captura en base64, o null si no hay navegador abierto. */
    public static String capturaBase64() {
        if (!DriverFactory.hasDriver()) {
            return null;
        }
        return ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BASE64);
    }
}
