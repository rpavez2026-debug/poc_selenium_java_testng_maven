# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

UI test automation POC against https://www.saucedemo.com/ using Selenium 4, TestNG and Maven (Java 21). The user communicates in Spanish; test names, descriptions and comments are in Spanish.

## Commands

```bash
mvn test                                  # run suite in src/test/resources/testng.xml (visible Chrome)
mvn test -Dheadless=true                  # headless
mvn test -Dbrowser=firefox                # chrome | firefox | edge
mvn test -Dtest=CP008_Logout              # single test case (bypasses testng.xml, so listeners don't run)
mvn test -Dgroups=smoke                   # by TestNG group (smoke, login, negative)
mvn test -Dsuite=other.xml                # alternate suite file under src/test/resources/
```

Reports: `target/surefire-reports/`.

## Architecture

All code lives under `src/test/java/com/saucedemo/` (no `src/main`); dependencies are `test` scope.

- `config/Config` — loads `src/test/resources/config.properties`; every key can be overridden with `-Dkey=value` (system properties win). Add new settings there rather than hardcoding.
- `driver/DriverFactory` — one `WebDriver` per thread via `ThreadLocal`. Drivers are resolved by Selenium Manager (no WebDriverManager / no driver binaries in repo). Chrome options disable the password-manager / leak-detection popup, which otherwise appears after logging in with `secret_sauce` and blocks the UI — keep these when touching Chrome options.
- `pages/` — Page Object Model. `BasePage` provides explicit-wait helpers (`waitVisible`, `click`, `type`, `isDisplayed`); never use `Thread.sleep` or implicit waits. `isDisplayed` waits up to the timeout and returns false instead of throwing. Page methods return the next page object (e.g. `LoginPage.loginAs` → `InventoryPage`, `InventoryPage.logout` → `LoginPage`); `loginExpectingError` stays on `LoginPage`.
- `tests/BaseTest` — opens a fresh browser in `@BeforeMethod` and quits it in `@AfterMethod` (`alwaysRun = true`), so each test is fully isolated. Tests extend it and get the driver via `driver()`.
- **One test case per class** (user's convention from previous projects): `tests/<flujo>/CPnnn_Descripcion_En_Snake_Case` with a single `@Test public void ejecutar()` whose `description` explains the case. Each CP gets its own `<test>` line in `testng.xml` (one-liner format); new CPs must be added there to run with plain `mvn test`.
- `utils/listeners/` — registered in `testng.xml`. `TestListener` creates the ExtentReports entry per CP (named after the class) and attaches a screenshot on pass/fail; it runs before `@AfterMethod`, so the browser is still open. `AnnotationTransformer` applies `Retry` to every `@Test`; max retries come from `retry.max` (`-Dretry.max=0` disables).
- `utils/evidence/Evidencia.registrarPaso("mensaje")` — call at key steps inside a CP; saves `target/evidencias/<CP>/NN_mensaje.png` and adds the screenshot to the report.
- Report: `target/extent-reports/extent-report.html`.
- **Test data lives in `src/test/resources/utils.json`**, one block per CP keyed by the exact class name (`usuario`, `password`, `mensajeEsperado`, …); never hardcode it in the CP. Read it with `TestData.get("clave")` — `BaseTest.setUp` sets the current CP from `getClass().getSimpleName()`, so it also works with `-Dtest`. DataProviders run before `@BeforeMethod`, so they must use `TestData.getList(getClass().getSimpleName(), "clave")`. Missing blocks/keys throw with a descriptive message. Renaming a CP class means renaming its JSON block too.
- `config.properties` holds environment settings only (URL, browser, waits, retries), not test data.
- `utils/logs/Log` — static log4j2 facade (`Log.info(...)`), configured in `src/test/resources/log4j2.properties`: console + `logs/test-automation.log` (project root, gitignored, survives `mvn clean`), rolled daily / at 10MB into `logs/archivo/`. Pages log each user action; `registrarPaso` and `TestListener` also log. Use `Log`, not `System.out`.
- `slf4j-api` 2.x is pinned in the pom because TestNG pulls 1.7, which breaks the `log4j-slf4j2-impl` bridge. Selenium's version must support the installed Chrome's CDP version (4.51.0 → Chrome 155), otherwise "Unable to find CDP implementation" warnings appear.

Locators prefer `id` or `data-test` attributes. Expected error messages are asserted as exact strings ("Epic sadface: ...").
