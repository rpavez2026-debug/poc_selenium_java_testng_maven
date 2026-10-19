package com.saucedemo.utils.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.saucedemo.config.Config;

public final class ExtentManager {

    public static final String REPORT_PATH = "target/extent-reports/extent-report.html";

    private static final ExtentReports EXTENT_REPORTS = create();

    private ExtentManager() {
    }

    private static ExtentReports create() {
        ExtentSparkReporter reporter = new ExtentSparkReporter(REPORT_PATH);
        reporter.config().setReportName("Informe de pruebas SauceDemo");
        reporter.config().setDocumentTitle("SauceDemo");

        ExtentReports extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("URL", Config.baseUrl());
        extent.setSystemInfo("Navegador", Config.get("browser"));
        extent.setSystemInfo("Headless", Config.get("headless"));
        return extent;
    }

    public static ExtentReports getExtentReports() {
        return EXTENT_REPORTS;
    }
}
