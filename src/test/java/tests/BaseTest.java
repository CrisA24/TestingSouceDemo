package tests;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.WebDriverFactory;

public class BaseTest {
    protected WebDriver driver;
    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeMethod
    @Parameters("browser")
    public void setup(@Optional("chrome") String browser) {
        logger.info("Starting test on browser: {}", browser);
        driver = WebDriverFactory.getDriver(browser);
        driver.get(ConfigReader.getProperty("baseUrl"));
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            logger.info("Closing browser...");
            driver.quit();
        }
    }
}



