package com.saucedemo.tests.login;

import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import com.saucedemo.utils.data.TestData;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;

public class CP003_Login_Password_Incorrecta extends BaseTest {

    @Test(groups = {"login", "negative"}, description = "Usuario válido con contraseña incorrecta muestra error")
    public void ejecutar() {
        LoginPage loginPage = new LoginPage(driver()).open()
                .loginExpectingError(TestData.get("usuario"), TestData.get("password"));

        assertEquals(loginPage.getErrorMessage(), TestData.get("mensajeEsperado"));
        registrarPaso("Mensaje credenciales invalidas");
    }
}
