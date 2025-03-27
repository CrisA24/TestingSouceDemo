package tests;

import org.example.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.Constants;
import java.time.Duration;

public class LoginTests extends tests.BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(LoginTests.class);

    @Test
    public void testLoginWithEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        logger.info("Starting test: Login with empty credentials");

        loginPage.login("", "");
        String actualError = loginPage.getErrorMessage();

        Assert.assertTrue(actualError.contains(Constants.ERROR_USERNAME_REQUIRED), "Error message validation failed");
        logger.warn("Login failed with message: {}", actualError);
    }

    @Test
    public void testLoginWithOnlyUsername() {
        LoginPage loginPage = new LoginPage(driver);
        logger.info("Starting test: Login with username only");

        loginPage.login("standard_user", "");
        String actualError = loginPage.getErrorMessage();

        Assert.assertTrue(actualError.contains(Constants.ERROR_PASSWORD_REQUIRED), "Error message validation failed");
        logger.warn("Login failed with message: {}", actualError);
    }

    @Test
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        logger.info("Starting test: Valid login");

        loginPage.login("standard_user", "secret_sauce");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.titleIs("Swag Labs"));

        Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title validation failed");
        logger.info("Login successful, redirected to Swag Labs");
    }
}
