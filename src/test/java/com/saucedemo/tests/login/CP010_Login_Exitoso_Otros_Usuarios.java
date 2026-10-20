package com.saucedemo.tests.login;

import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.tests.BaseTest;
import com.saucedemo.utils.data.TestData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.saucedemo.utils.evidence.Evidencia.registrarPaso;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CP010_Login_Exitoso_Otros_Usuarios extends BaseTest {

    @DataProvider(name = "usuarios")
    public Object[][] usuarios() {
        // El DataProvider corre antes del @BeforeMethod, por eso el CP va explícito
        return TestData.getList(getClass().getSimpleName(), "usuarios").stream()
                .map(usuario -> new Object[]{usuario})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "usuarios", groups = {"login"},
            description = "Login exitoso con los demás usuarios válidos de SauceDemo")
    public void ejecutar(String usuario) {
        InventoryPage inventory = new LoginPage(driver()).open().loginAs(usuario, TestData.get("password"));

        assertTrue(inventory.isLoaded(), "La página de inventario no cargó para " + usuario);
        assertEquals(inventory.getTitle(), TestData.get("tituloEsperado"));
        registrarPaso("Inventario " + usuario);
    }
}
