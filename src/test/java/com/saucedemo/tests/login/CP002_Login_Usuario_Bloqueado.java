package com.saucedemo.tests.login;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import com.saucedemo.utils.data.TestData;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;

public class CP002_Login_Usuario_Bloqueado extends BaseTest {

    @Test(groups = {"login", "negative"}, description = "Usuario bloqueado (locked_out_user) no puede ingresar")
    public void ejecutar() {
        LoginPage loginPage = new LoginPage(driver()).open()
                .loginExpectingError(TestData.get("usuario"), TestData.get("password"));

        assertEquals(loginPage.getErrorMessage(), TestData.get("mensajeEsperado"));
        registrarPaso("Mensaje usuario bloqueado");
    }
}
