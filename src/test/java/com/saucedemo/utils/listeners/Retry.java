package com.saucedemo.utils.listeners;

import com.saucedemo.config.Config;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Reintenta un test fallido hasta retry.max veces (config.properties o -Dretry.max=N).
 * TestNG crea una instancia por método, así que el contador es por test.
 */
public class Retry implements IRetryAnalyzer {

    private static final int MAX_TRY = Config.getInt("retry.max");
    private int count = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess() && count < MAX_TRY) {
            count++;
            return true;
        }
        return false;
    }
}
