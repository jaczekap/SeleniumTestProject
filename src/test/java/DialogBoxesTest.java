import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DialogBoxesTest {

    private WebDriver driver;


    @BeforeAll
    static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUpTest() {
        driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(2000, 0));
        driver.manage().window().maximize();
        ((RemoteWebDriver)driver).setLogLevel(Level.INFO);
    }

    @AfterEach
    void tearDownTest() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        driver.quit();
    }

    @AfterAll
    static void tearDownClass() {
    }

    @Test
    void testAlert() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.findElement(By.id("my-alert")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        assertThat(alert.getText(), is(equalTo("Hello world!")));
        alert.accept();
    }

    @Test
    void testConfirm() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.findElement(By.id("my-confirm")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert confirm = driver.switchTo().alert();
        assertThat(confirm.getText(), is(equalTo("Is this correct?")));
        confirm.dismiss();
    }

    @Test
    void testPrompt() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dialog-boxes.html");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        driver.findElement(By.id("my-prompt")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert prompt = driver.switchTo().alert();
        prompt.sendKeys("Some Text");
        assertThat(prompt.getText(), is(equalTo("Please enter your name")));
        prompt.accept();
    }

    @Test
    public void testLoginLogout(){
        driver.get("https://www.advantageonlineshopping.com/#");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement menuUserButton = driver.findElement(By.id("menuUser"));
        menuUserButton.click();

        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys("testUser");

        WebElement passwordField = driver.findElement(By.name("password"));
        passwordField.sendKeys("Password1");

        WebElement loginButton = driver.findElement(By.id("sign_in_btnundefined"));
        loginButton.click();

//driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
//wait.until(ExpectedConditions.elementToBeClickable(menuUserButton));
//menuUserButton.click();

        WebElement menuUserDiv = driver.findElement(By.id("menuUserLink"));
        wait.until(ExpectedConditions.attributeToBe(By.className("loader"), "style", "display: none; opacity: 0;"));


        menuUserDiv.click();

        WebElement logoutButton = driver.findElement(By.ByCssSelector.cssSelector("[ng-click='signOut($event)']"));
        logoutButton.click();
    }


}
