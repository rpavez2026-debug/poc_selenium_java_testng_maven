package com.saucedemo.driver;

import com.saucedemo.config.Config;
import com.saucedemo.utils.logs.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Map;

/**
 * Crea y guarda un WebDriver por hilo (ThreadLocal) para soportar ejecución en paralelo.
 * Los drivers los descarga automáticamente Selenium Manager (incluido en Selenium 4).
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initDriver() {
        String browser = Config.get("browser").toLowerCase();
        boolean headless = Config.getBoolean("headless");

        WebDriver driver = switch (browser) {
            case "chrome" -> new ChromeDriver(chromeOptions(headless));
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                yield new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (headless) options.addArguments("--headless=new");
                yield new EdgeDriver(options);
            }
            default -> throw new IllegalArgumentException("Navegador no soportado: " + browser);
        };

        if (!headless) {
            driver.manage().window().maximize();
        }
        DRIVER.set(driver);
        Log.info("Navegador iniciado: " + browser + (headless ? " (headless)" : ""));
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        // Evita el popup "Cambia tu contraseña" de Chrome, que bloquea la UI tras loguearse
        // con secret_sauce (contraseña marcada como filtrada).
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        options.addArguments("--disable-features=PasswordLeakDetection");
        return options;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver no inicializado. ¿Se ejecutó initDriver()?");
        }
        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
            Log.info("Navegador cerrado");
        }
    }
}
