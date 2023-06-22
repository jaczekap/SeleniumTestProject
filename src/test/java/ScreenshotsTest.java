import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ScreenshotsTest {

    private WebDriver driver;
    private TakesScreenshot ts;


    @BeforeAll
    static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUpTest() {
        driver = new ChromeDriver();
        ts = (TakesScreenshot)driver;
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
    void testScreenshotPng() throws IOException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        TakesScreenshot ts = (TakesScreenshot) driver;
        File screenshot = ts.getScreenshotAs(OutputType.FILE);
        Path destination = Paths.get("screenshot.png");
        Files.move(screenshot.toPath(), destination, REPLACE_EXISTING);

        Desktop.getDesktop().open(new File(destination.toUri()));

        assertTrue(Files.exists(destination));
    }

    @Test
    void testWebElementScreenshot() throws IOException {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        WebElement form = driver.findElement(By.tagName("form"));
        File screenshot = form.getScreenshotAs(OutputType.FILE);
        Path destination = Paths.get("webelement-screenshot.png");
        Files.move(screenshot.toPath(), destination, REPLACE_EXISTING);

        Desktop.getDesktop().open(new File(destination.toUri()));

        assertTrue(Files.exists(destination));
    }


}
