import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;

import java.time.Duration;
import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNot.*;

public class JavaScriptTest {

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
    void testScroll() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/long-page.html");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        //String script = "window.scrollBy(0, 1000);";
        //js.executeScript(script);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement lastElememt = driver.findElement(By.cssSelector("p:last-child"));
        String script = "arguments[0].scrollIntoView();";
        js.executeScript(script, lastElememt);
    }

    @Test
    void testColorPicker() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement colorPicker = driver.findElement(By.name("my-colors"));
        String initColor = colorPicker.getAttribute("value");
        Color red = new Color(255, 0, 0, 1);

        String script = String.format("arguments[0].setAttribute('value', '%s');", red.asHex());
        js.executeScript(script, colorPicker);

        String finalColor = colorPicker.getAttribute("value");
        assertThat(finalColor, not(initColor));
        assertThat(Color.fromString(finalColor), is(red));

    }

}
