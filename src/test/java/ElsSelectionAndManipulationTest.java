import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.ui.Select;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ElsSelectionAndManipulationTest {


    private WebDriver driver;


    @BeforeAll
    static void setUpClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUpTest() {
        // Instantiate a new ChromeDriver object
        driver = new ChromeDriver();
        // Set the position of the browser window to (2000, 0)
        driver.manage().window().setPosition(new Point(2000, 0));
        // Maximize the browser window
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
    void rangeBarIsResponsive() {
        String url = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
        driver.get(url);

        JavascriptExecutor js = (JavascriptExecutor)driver;
        WebElement rangeBar = driver.findElement(By.xpath("//input[@name='my-range']"));

        StringBuilder valueToSend = new StringBuilder("arguments[0].value=0");

//        for (int i = 0; i < 11; i++) {
//            js.executeScript(valueToSend.deleteCharAt(valueToSend.indexOf("=") + 1).append(i).toString(), driver.findElement(By.xpath("//input[@name='my-range']")));
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }

        int minValue = Integer.parseInt(rangeBar.getAttribute("min"));
        int maxValue = Integer.parseInt(rangeBar.getAttribute("max"));

        for (int i = minValue; i <= maxValue; i++) {
            js.executeScript(valueToSend.deleteCharAt(valueToSend.indexOf("=") + 1).append(i).toString(), driver.findElement(By.xpath("//input[@name='my-range']")));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        assertThat(rangeBar.getAttribute("value"), equalTo("10"));
        assertTrue(rangeBar.getAttribute("value").contentEquals("10"));
        assertEquals(rangeBar.getAttribute("value"), "10");
        assertEquals(rangeBar.getAttribute("value"), "10", "Bar is not on max value!!!");
    }

    @Test
    void testSlider() {
        driver.get(
                "https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        WebElement slider = driver.findElement(By.name("my-range"));
        String initValue = slider.getAttribute("value");
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            slider.sendKeys(Keys.ARROW_RIGHT);
        }
        String endValue = slider.getAttribute("value");
        assertThat(initValue, not(endValue));
    }

    @Test
    void testByChained() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        List<WebElement> rowsInForm = driver.findElements(
                new ByChained(By.tagName("form"), By.className("row")));
        assertThat(rowsInForm.size(), is(1));
    }

    @Test
    void testByAll() {
        driver.get(
                "https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        List<WebElement> rowsInForm = driver.findElements(
                new ByAll(By.tagName("form"), By.className("row")));
        assertThat(rowsInForm.size(), is(5));
    }

    @Test
    void testRelativeLocators() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        WebElement link = driver.findElement(By.linkText("Return to index"));
        RelativeLocator.RelativeBy relativeBy = RelativeLocator.with(By.tagName("input"));
        WebElement readOnly = driver.findElement(relativeBy.above(link));
        assertThat(readOnly.getAttribute("name"), is(equalTo("my-readonly")));
    }

    @Test
    void testDatePicker() {
        driver.get(
                "https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        // Get the current date from the system clock
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentDay = today.getDayOfMonth();
        // Click on the date picker to open the calendar
        WebElement datePicker = driver.findElement(By.name("my-date"));
        datePicker.click();
        // Click on the current month by searching by text
        WebElement monthElement = driver.findElement(By.xpath(
                String.format("//th[contains(text(),'%d')]", currentYear)));
        monthElement.click();
        // Click on the left arrow using relative locators
        WebElement arrowLeft = driver.findElement(
                RelativeLocator.with(By.tagName("th")).toRightOf(monthElement));
        arrowLeft.click();
        // Click on the current month of that year
        WebElement monthPastYear = driver.findElement(RelativeLocator
                .with(By.cssSelector("span[class$=focused]")).below(arrowLeft));
        monthPastYear.click();
        // Click on the present day in that month
        WebElement dayElement = driver.findElement(By.xpath(String.format(
                "//td[@class='day' and contains(text(),'%d')]", currentDay)));
        dayElement.click();
        // Get the final date on the input text
        String oneYearBack = datePicker.getAttribute("value");
        // Assert that the expected date is equal to the one selected in the
        // date picker
        LocalDate previousYear = today.minusYears(1);
        DateTimeFormatter dateFormat = DateTimeFormatter
                .ofPattern("MM/dd/yyyy");
        String expectedDate = previousYear.format(dateFormat);
        assertThat(oneYearBack, is(equalTo(expectedDate)));
    }

    /**
    * Test for uploading a file.
    * @throws IOException if there is an error in the IO operation
    */
    @Test
    void testUploadFile() throws IOException {
        String initUrl = "https://bonigarcia.dev/selenium-webdriver-java/web-form.html";
        driver.get(initUrl);
        WebElement inputFile = driver.findElement(By.name("my-file"));
        Path tempFile = Files.createTempFile("tempfiles", ".tmp");
        String filename = tempFile.toAbsolutePath().toString();
        inputFile.sendKeys(filename);
        driver.findElement(By.tagName("form")).submit();
        assertThat(driver.getCurrentUrl(), not(initUrl));
    }

    @Test
    void testCheckBoxAndRadioButtons() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        WebElement checkbox2 = driver.findElement(By.id("my-check-2"));
        checkbox2.click();
        assertTrue(checkbox2.isSelected());
        WebElement radio2 = driver.findElement(By.id("my-radio-2"));
        radio2.click();
        assertTrue(radio2.isSelected());
    }

    @Test
    void test() {
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/web-form.html");
        Select select = new Select(driver.findElement(By.name("my-select")));
        String optionLabel = "Three";
        select.selectByVisibleText(optionLabel);
        assertThat(select.getFirstSelectedOption().getText(), is(equalTo(optionLabel)));
    }

    @Test
    void test2() {
        driver.get("https://demowebshop.tricentis.com/books");
        driver.findElement(By.xpath("//img[@alt='Tricentis Demo Web Shop']")).click();
    }

}
