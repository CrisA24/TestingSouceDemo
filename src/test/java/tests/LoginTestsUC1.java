package tests;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginTestsUC1 {
    private WebDriver driver;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void testLoginWithEmptyCredentials() throws InterruptedException {
        WebElement usernameInput = driver.findElement(By.cssSelector("#user-name"));
        WebElement passwordInput = driver.findElement(By.cssSelector("#password"));
        WebElement loginButton = driver.findElement(By.cssSelector("#login-button"));

        // Introduce data tests (user and password) and delete them
        usernameInput.sendKeys("testuser");
        passwordInput.sendKeys("testpass");
        usernameInput.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        passwordInput.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        Thread.sleep(500); //check if the password is deleted

        // Both data tests are empty
        assertThat(usernameInput.getAttribute("value")).isEmpty();
        assertThat(passwordInput.getAttribute("value")).isEmpty();

        // Click on Login
        loginButton.click();
        Thread.sleep(2000);

        // Wait the error message
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-container h3")));

        // Check the error message
        assertThat(errorMessage.getText()).contains("Username is required");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
