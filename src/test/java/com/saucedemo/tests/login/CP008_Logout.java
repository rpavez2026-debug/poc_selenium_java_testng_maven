package com.saucedemo.tests.login;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import com.saucedemo.utils.data.TestData;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertTrue;

public class CP008_Logout extends BaseTest {

    @Test(groups = {"smoke", "login"}, description = "Logout desde el menú vuelve a la página de login")
    public void ejecutar() {
        InventoryPage inventory = new LoginPage(driver()).open()
                .loginAs(TestData.get("usuario"), TestData.get("password"));
        assertTrue(inventory.isLoaded(), "La página de inventario no cargó");
        registrarPaso("Sesion iniciada");

        LoginPage loginPage = inventory.logout();

        assertTrue(loginPage.isLoginButtonDisplayed(), "No volvió a la página de login tras el logout");
        registrarPaso("Login tras logout");
    }
}
