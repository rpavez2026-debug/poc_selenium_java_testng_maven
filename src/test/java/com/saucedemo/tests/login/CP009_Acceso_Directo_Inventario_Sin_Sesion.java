package com.saucedemo.tests.login;

import com.saucedemo.config.Config;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;

public class CP009_Acceso_Directo_Inventario_Sin_Sesion extends BaseTest {

    @Test(groups = {"login", "negative"},
            description = "Sin sesión, acceder directo a /inventory.html redirige al login con error")
    public void ejecutar() {
        driver().get(Config.baseUrl() + "inventory.html");
        LoginPage loginPage = new LoginPage(driver());

        assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: You can only access '/inventory.html' when you are logged in.");
        registrarPaso("Mensaje acceso sin sesion");
    }
}
