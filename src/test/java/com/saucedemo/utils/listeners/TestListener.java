package com.saucedemo.utils.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.saucedemo.utils.evidence.Evidencia;
import com.saucedemo.utils.logs.Log;
import com.saucedemo.utils.reports.ExtentManager;
import com.saucedemo.utils.reports.ExtentTestManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Crea la entrada de ExtentReports de cada CP y adjunta una captura al terminar.
 * Corre antes del @AfterMethod de BaseTest, así que el navegador sigue abierto.
 */
public class TestListener implements ITestListener {

    private static String testName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String name = testName(result);
        Log.info("===== " + name + " comenzando =====");
        ExtentTestManager.startTest(name, result.getMethod().getDescription());
        Evidencia.iniciar(name);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        Log.info("===== " + testName(result) + " OK =====");
        log(Status.PASS, "Test exitoso");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Log.error("===== " + testName(result) + " FALLÓ =====", result.getThrowable());
        ExtentTest test = ExtentTestManager.getTest();
        if (test != null) {
            test.fail(result.getThrowable());
        }
        log(Status.FAIL, "Test fallido");
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        if (result.wasRetried()) {
            Log.warn("===== " + testName(result) + " falló, se reintenta =====");
            log(Status.WARNING, "Falló, se reintenta");
        } else {
            Log.warn("===== " + testName(result) + " omitido =====");
            log(Status.SKIP, "Test omitido");
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentTestManager.endTest();
        ExtentManager.getExtentReports().flush();
    }

    private static void log(Status status, String mensaje) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test == null) {
            return;
        }
        String base64 = Evidencia.capturaBase64();
        if (base64 == null) {
            test.log(status, mensaje);
        } else {
            test.log(status, mensaje, test.addScreenCaptureFromBase64String(base64).getModel().getMedia().get(0));
        }
    }
}
