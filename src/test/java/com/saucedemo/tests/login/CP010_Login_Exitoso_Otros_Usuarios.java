package com.saucedemo.tests.login;

import com.saucedemo.config.Config;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CP010_Login_Exitoso_Otros_Usuarios extends BaseTest {

    @DataProvider(name = "usuarios")
    public Object[][] usuarios() {
        return new Object[][]{
                {"problem_user"},
                {"performance_glitch_user"},
                {"error_user"},
                {"visual_user"},
        };
    }

    @Test(dataProvider = "usuarios", groups = {"login"},
            description = "Login exitoso con los demás usuarios válidos de SauceDemo")
    public void ejecutar(String username) {
        InventoryPage inventory = new LoginPage(driver()).open().loginAs(username, Config.get("password"));

        assertTrue(inventory.isLoaded(), "La página de inventario no cargó para " + username);
        assertEquals(inventory.getTitle(), "Products");
        registrarPaso("Inventario " + username);
    }
}
