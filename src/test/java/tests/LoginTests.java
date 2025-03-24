package tests;

import Base.BaseTest;
import org.example.LoginPage;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.example.LoginPage;
import Utils.Constants;

import java.time.Duration;

public class LoginTests extends BaseTest {
    private static final Logger logger = LoggerFactory.getLogger(LoginTests.class);

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"", "", Constants.ERROR_USERNAME_REQUIRED},     // UC-1: Empty credentials
                {"standard_user", "", Constants.ERROR_PASSWORD_REQUIRED}, // UC-2: Username only
                {"standard_user", "secret_sauce", "Swag Labs"}  // UC-3: Valid login
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, String expectedMessage) {
        LoginPage loginPage = new LoginPage(driver);
        logger.info("Starting login test with username: '{}' and password: '{}'", username, password.isEmpty() ? "(empty)" : "******");

        // Perform login
        loginPage.login(username, password);

        if (expectedMessage.equals("Swag Labs")) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.titleIs("Swag Labs"));
            Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title validation failed");
            logger.info("Login successful, redirected to Swag Labs");
        } else {
            String actualError = loginPage.getErrorMessage();
            Assert.assertTrue(actualError.contains(expectedMessage), "Error message validation failed");
            logger.warn("Login failed with message: {}", actualError);
        }
    }
}
