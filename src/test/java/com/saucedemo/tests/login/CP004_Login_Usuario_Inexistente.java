package com.saucedemo.tests.login;

import com.saucedemo.config.Config;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;

public class CP004_Login_Usuario_Inexistente extends BaseTest {

    @Test(groups = {"login", "negative"}, description = "Usuario que no existe muestra error")
    public void ejecutar() {
        LoginPage loginPage = new LoginPage(driver()).open()
                .loginExpectingError("usuario_inexistente", Config.get("password"));

        assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
        registrarPaso("Mensaje usuario inexistente");
    }
}
