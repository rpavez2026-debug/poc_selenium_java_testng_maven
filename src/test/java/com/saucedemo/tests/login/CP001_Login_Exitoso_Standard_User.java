package com.saucedemo.tests.login;

import com.saucedemo.config.Config;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CP001_Login_Exitoso_Standard_User extends BaseTest {

    @Test(groups = {"smoke", "login"}, description = "Login exitoso con standard_user redirige al inventario")
    public void ejecutar() {
        LoginPage loginPage = new LoginPage(driver()).open();
        registrarPaso("Pagina de login");

        InventoryPage inventory = loginPage.loginAs("standard_user", Config.get("password"));

        assertTrue(inventory.isLoaded(), "La página de inventario no cargó");
        assertEquals(inventory.getTitle(), "Products");
        registrarPaso("Inventario cargado");
    }
}
