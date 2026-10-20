package com.saucedemo.pages;

import com.saucedemo.utils.logs.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class InventoryPage extends BasePage {

    private static final By TITLE = By.cssSelector("[data-test='title']");
    private static final By INVENTORY_LIST = By.cssSelector("[data-test='inventory-list']");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        wait.until(ExpectedConditions.urlContains("inventory.html"));
        return waitVisible(INVENTORY_LIST).isDisplayed();
    }

    public String getTitle() {
        return getText(TITLE);
    }

    public LoginPage logout() {
        Log.info("Cerrando sesión desde el menú");
        click(MENU_BUTTON);
        click(LOGOUT_LINK);
        return new LoginPage(driver);
    }
}
