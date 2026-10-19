package com.saucedemo.tests.login;

import com.saucedemo.config.Config;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;

public class CP002_Login_Usuario_Bloqueado extends BaseTest {

    @Test(groups = {"login", "negative"}, description = "Usuario bloqueado (locked_out_user) no puede ingresar")
    public void ejecutar() {
        LoginPage loginPage = new LoginPage(driver()).open()
                .loginExpectingError("locked_out_user", Config.get("password"));

        assertEquals(loginPage.getErrorMessage(), "Epic sadface: Sorry, this user has been locked out.");
        registrarPaso("Mensaje usuario bloqueado");
    }
}
