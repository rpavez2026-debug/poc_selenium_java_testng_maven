package com.saucedemo.utils.reports;

import com.aventstack.extentreports.ExtentTest;

/**
 * Guarda el ExtentTest del test en curso por hilo. Lo inicia TestListener automáticamente,
 * así que los CP no necesitan llamar a startTest().
 */
public final class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> CURRENT = new ThreadLocal<>();

    private ExtentTestManager() {
    }

    public static synchronized ExtentTest startTest(String testName, String description) {
        ExtentTest test = ExtentManager.getExtentReports().createTest(testName, description);
        CURRENT.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return CURRENT.get();
    }

    public static void endTest() {
        CURRENT.remove();
    }
}
