import io.github.bonigarcia.wdm.WebDriverManager;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.assertThat;

public class AuthTest {

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

    @Test
    void testBasicAuth() {
        ((HasAuthentication) driver)
                .register(() -> new UsernameAndPassword("guest", "guest"));
        driver.get("https://jigsaw.w3.org/HTTP/Basic/");
        WebElement body = driver.findElement(By.tagName("body"));
        assertThat(body.getText(), Matchers.containsStringIgnoringCase("Your browser made it!"));
    }


}
