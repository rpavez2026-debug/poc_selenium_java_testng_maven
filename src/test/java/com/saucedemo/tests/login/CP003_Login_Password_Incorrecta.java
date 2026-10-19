package com.saucedemo.tests.login;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;

public class CP003_Login_Password_Incorrecta extends BaseTest {

    @Test(groups = {"login", "negative"}, description = "Usuario válido con contraseña incorrecta muestra error")
    public void ejecutar() {
        LoginPage loginPage = new LoginPage(driver()).open()
                .loginExpectingError("standard_user", "clave_incorrecta");

        assertEquals(loginPage.getErrorMessage(),
                "Epic sadface: Username and password do not match any user in this service");
        registrarPaso("Mensaje credenciales invalidas");
    }
}
