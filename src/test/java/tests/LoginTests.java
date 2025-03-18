package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class LoginTests {
    private WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(LoginTests.class);

    @BeforeClass
    @Parameters("browser")
    public void setup(@Optional("chrome") String browser) {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } else if (browser.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }

        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        logger.info("Navigated to SauceDemo");
    }

    @DataProvider(name = "loginData")
    public Object[][] loginDataProvider() {
        return new Object[][]{
                {"", "", "Username is required"},                  // UC-1: Empty credentials
                {"standard_user", "", "Password is required"},     // UC-2: Only username
                {"standard_user", "secret_sauce", "Swag Labs"}     // UC-3: Valid login
        };
    }

    @Test(dataProvider = "loginData")
    public void testLogin(String username, String password, String expectedMessage) {
        WebElement usernameInput = driver.findElement(By.cssSelector("#user-name"));
        WebElement passwordInput = driver.findElement(By.cssSelector("#password"));
        WebElement loginButton = driver.findElement(By.cssSelector("#login-button"));

        usernameInput.clear();
        passwordInput.clear();

        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);

        logger.info("Entered username: {} and password: {}", username, password.isEmpty() ? "(empty)" : "******");

        loginButton.click();

        if (expectedMessage.equals("Swag Labs")) {
            // UC-3: Check of the title page is "Swag Labs"
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.titleIs("Swag Labs"));
            Assert.assertEquals(driver.getTitle(), "Swag Labs", "Title validation failed");
            logger.info("Login successful, redirected to Swag Labs");
        } else {
            // UC-1, UC-2: Check the error message
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));
            Assert.assertTrue(errorMessage.getText().contains(expectedMessage), "Error message validation failed");
            logger.warn("Login failed with message: {}", errorMessage.getText());
        }
    }

    @AfterClass //close the browser
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            logger.info("Browser closed");
        }
    }
}

