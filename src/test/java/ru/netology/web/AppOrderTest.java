package ru.netology.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
         WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }


    @Test
    void shouldSendIfCorrect() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(By.cssSelector(".form"));
        form.findElement(By.cssSelector("[name='name']")).sendKeys("Василий");
        form.findElement(By.cssSelector("[name='phone']")).sendKeys("+79270000000");
        form.findElement(By.cssSelector(".checkbox")).click();
        form.findElement(By.cssSelector(".button")).click();
        String text = driver.findElement(By.cssSelector(".paragraph")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }


    @Test
    void shouldShowErrorMsgIfNameIsIncorrect() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(By.cssSelector(".form"));
        form.findElement(By.cssSelector("[name='name']")).sendKeys("Name");
        form.findElement(By.cssSelector("[name='phone']")).sendKeys("+79270000000");
        form.findElement(By.cssSelector(".checkbox")).click();
        form.findElement(By.cssSelector(".button")).click();
        String text = form.findElement(By.cssSelector("[data-test-id='name'] .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", text.trim());
    }


    @Test
    void shouldShowErrorMsgIfTelWithoutPlus() {
        driver.get("http://localhost:9999/");
        WebElement form = driver.findElement(By.cssSelector(".form"));
        form.findElement(By.cssSelector("[name='name']")).sendKeys("Василий");
        form.findElement(By.cssSelector("[name='phone']")).sendKeys("792700000000");
        form.findElement(By.cssSelector(".checkbox")).click();
        form.findElement(By.cssSelector(".button")).click();
        String text = form.findElement(By.cssSelector("[data-test-id='phone'] .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", text.trim());
    }

}
