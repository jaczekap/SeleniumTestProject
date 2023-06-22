import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.SystemUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ActionTest {

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
    void testContextAndDoubleClick() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/dropdown-menu.html");
        Actions actions = new Actions(driver);
        WebElement dropdown2 = driver.findElement(By.id("my-dropdown-2"));
        actions.contextClick(dropdown2).build().perform();
        WebElement contextMenu2 = driver.findElement(By.id("context-menu-2"));
        assertThat(contextMenu2.isDisplayed(), is(true));
        WebElement dropdown3 = driver.findElement(By.id("my-dropdown-3"));
        actions.doubleClick(dropdown3).build().perform();
        WebElement contextMenu3 = driver.findElement(By.id("context-menu-3"));
        assertThat(contextMenu3.isDisplayed(), is(true));
    }

    @Test
    void testDragAndDrop() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/drag-and-drop.html");
        Actions actions = new Actions(driver);
        WebElement draggable = driver.findElement(By.id("draggable"));
        int offset = 100;
        Point initLocation = draggable.getLocation();
        actions.dragAndDropBy(draggable, offset, 0)
                .dragAndDropBy(draggable, 0, offset)
                .dragAndDropBy(draggable, -offset, 0)
                .dragAndDropBy(draggable, 0, -offset).build().perform();
        assertThat(initLocation, is(equalTo(draggable.getLocation())));
        WebElement target = driver.findElement(By.id("target"));
        actions.dragAndDrop(draggable, target).build().perform();
        assertThat(target.getLocation(), is(equalTo(draggable.getLocation())));
    }

    @Test
    void testCopyAndPaste() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        Actions actions = new Actions(driver);
        WebElement inputText = driver.findElement(By.name("my-text"));
        WebElement textarea = driver.findElement(By.name("my-textarea"));
        actions.sendKeys(inputText, "hello world").keyDown(Keys.CONTROL)
                .sendKeys(inputText, "a").sendKeys(inputText, "c")
                .sendKeys(textarea, "v").build().perform();
        Assertions.assertEquals(inputText.getAttribute("value"), textarea.getAttribute("value"));
    }


}
