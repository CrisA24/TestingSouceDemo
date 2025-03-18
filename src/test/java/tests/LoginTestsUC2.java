package tests;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.annotations.*;
import java.time.Duration;
import static org.assertj.core.api.Assertions.assertThat;


public class LoginTestsUC2 {

    private WebDriver driver;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup(); //Set Chromedriver
        driver = new ChromeDriver();//Initiate the browser
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
    }

    @Test
    public void testLoginWithUsernameOnly() throws InterruptedException {
        WebElement usernameInput = driver.findElement(By.id("user-name"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"login-button\"]"));


        // Introduce Username
        usernameInput.sendKeys("standard_user");

        // Introduce Password and delete it
        passwordInput.sendKeys("secret_sauce");
        passwordInput.sendKeys(Keys.CONTROL + "a", Keys.BACK_SPACE);
        Thread.sleep(500);

        // Delete the Password
        assertThat(passwordInput.getAttribute("value")).isEmpty();

        // Click on Login
        loginButton.click();
        Thread.sleep(2000);

        // Wait for the error message
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".error-message-container h3")));

        // Check that the message is displayed
        assertThat(errorMessage.getText()).contains("Password is required");

    }

    @AfterClass
    //Close the browser
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
