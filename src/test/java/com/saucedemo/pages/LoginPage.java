package com.saucedemo.pages;

import com.saucedemo.config.Config;
import com.saucedemo.utils.logs.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("h3[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        Log.info("Abriendo página de login: " + Config.baseUrl());
        driver.get(Config.baseUrl());
        waitVisible(LOGIN_BUTTON);
        return this;
    }

    /** Login esperado como exitoso: devuelve la página de inventario. */
    public InventoryPage loginAs(String username, String password) {
        submit(username, password);
        return new InventoryPage(driver);
    }

    /** Login esperado como fallido: se queda en la página de login. */
    public LoginPage loginExpectingError(String username, String password) {
        submit(username, password);
        return this;
    }

    private void submit(String username, String password) {
        Log.info("Login con usuario '" + username + "'");
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }

    public String getErrorMessage() {
        String error = getText(ERROR_MESSAGE);
        Log.info("Mensaje de error mostrado: " + error);
        return error;
    }

    public boolean isLoginButtonDisplayed() {
        return isDisplayed(LOGIN_BUTTON);
    }
}
