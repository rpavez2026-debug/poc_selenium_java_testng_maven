package com.saucedemo.tests.login;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;

public class CP007_Login_Campos_Vacios extends BaseTest {

    @Test(groups = {"login", "negative"}, description = "Ambos campos vacíos: se valida primero el usuario")
    public void ejecutar() {
        LoginPage loginPage = new LoginPage(driver()).open()
                .loginExpectingError("", "");

        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Username is required");
        registrarPaso("Mensaje campos vacios");
    }
}
