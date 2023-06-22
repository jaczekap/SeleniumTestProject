import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class HelloSeleniumTest {

    static final Logger log = LoggerFactory.getLogger(HelloSeleniumTest.class);
    private WebDriver driver;


    @BeforeAll
    static void setUpClass() {
        WebDriverManager.chromedriver().setup();
        //System.out.println("This is run before all test");
        log.info("This is run before all test");
    }

    @BeforeEach
    void setUpTest() {
        driver = new ChromeDriver();
        driver.manage().window().setPosition(new Point(2000, 0));
        driver.manage().window().maximize();
        ((RemoteWebDriver)driver).setLogLevel(Level.INFO);
        System.out.println("This is run before each test");
    }

    @AfterEach
    void tearDownTest() {

        driver.quit();
        System.out.println("This is run after each test");
    }

    @AfterAll
    static void tearDownClass() {
        System.out.println("This is run after all tests");
    }

    /*
     * this is hello test
     * */
    @DisplayName("This is most simple test")
    @Test
    public void googlePageOpens() {
        String url = "https://www.google.com/";
        driver.get(url);
        //driver.switchTo().newWindow(WindowType.WINDOW);
        System.out.println("Test is currently running...");

        assertThat("Seems that we are not on right page", driver.getTitle(), allOf(startsWith("G"),
                endsWith("e"),
                hasLength(6),
                containsString("oo")));
        assertThat(driver.getCurrentUrl(), is(equalTo(url)));
    }
}
