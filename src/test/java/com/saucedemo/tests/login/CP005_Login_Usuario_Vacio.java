package com.saucedemo.tests.login;

import com.saucedemo.config.Config;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;

public class CP005_Login_Usuario_Vacio extends BaseTest {

    @Test(groups = {"login", "negative"}, description = "Usuario vacío muestra 'Username is required'")
    public void ejecutar() {
        LoginPage loginPage = new LoginPage(driver()).open()
                .loginExpectingError("", Config.get("password"));

        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
        registrarPaso("Mensaje usuario requerido");
    }
}
