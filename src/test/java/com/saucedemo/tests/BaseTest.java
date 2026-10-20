package com.saucedemo.tests;

import com.saucedemo.driver.DriverFactory;
import com.saucedemo.utils.data.TestData;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Un navegador nuevo por cada test: aislamiento total entre casos.
 */
public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        // Aquí y no en el listener, para que funcione también con -Dtest (sin testng.xml)
        TestData.iniciar(getClass().getSimpleName());
        DriverFactory.initDriver();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected WebDriver driver() {
        return DriverFactory.getDriver();
    }
}
