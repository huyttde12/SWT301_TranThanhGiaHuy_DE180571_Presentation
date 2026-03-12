import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationSystemTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();

        //
        driver.get("file:///C:/Users/Dell/Downloads/!TrangDangKy.html");
    }

    private void fillFormAndSubmit(String email, String password, String confirmPassword) {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(email);

        driver.findElement(By.id("password")).clear();
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("confirm_password")).clear();
        driver.findElement(By.id("confirm_password")).sendKeys(confirmPassword);

        driver.findElement(By.id("submit_btn")).click();
    }


    @Test
    public void testTC01_SuccessfulRegistration() {
        fillFormAndSubmit("test@gmail.com", "ValidPass123!", "ValidPass123!");
        Alert alert = driver.switchTo().alert();
        assertEquals("Đăng ký thành công!", alert.getText());
        alert.accept(); // Bấm OK để đóng Alert
    }

    @Test
    public void testTC02_EmptyEmail() {
        fillFormAndSubmit("", "ValidPass123!", "ValidPass123!");
        String errorMsg = driver.findElement(By.id("email_error")).getText();
        assertEquals("Vui lòng nhập email", errorMsg);
    }

    @Test
    public void testTC03_EmailMissingAtSymbol() {
        fillFormAndSubmit("testgmail.com", "ValidPass123!", "ValidPass123!");
        String errorMsg = driver.findElement(By.id("email_error")).getText();
        assertEquals("Email không hợp lệ (thiếu @ hoặc dấu chấm)", errorMsg);
    }

    @Test
    public void testTC04_EmailMissingDot() {
        fillFormAndSubmit("test@gmailcom", "ValidPass123!", "ValidPass123!");
        String errorMsg = driver.findElement(By.id("email_error")).getText();
        assertEquals("Email không hợp lệ (thiếu @ hoặc dấu chấm)", errorMsg);
    }

    @Test
    public void testTC05_EmailAlreadyExists() {
        fillFormAndSubmit("existing@example.com", "ValidPass123!", "ValidPass123!");
        String errorMsg = driver.findElement(By.id("general_error")).getText();
        assertEquals("Email này đã được sử dụng", errorMsg);
    }

    @Test
    public void testTC06_EmptyPassword() {
        fillFormAndSubmit("test@gmail.com", "", "");
        String errorMsg = driver.findElement(By.id("password_error")).getText();
        assertEquals("Vui lòng nhập mật khẩu", errorMsg);
    }

    @Test
    public void testTC07_ShortPassword_7Chars() {
        fillFormAndSubmit("test@gmail.com", "1234567", "1234567");
        String errorMsg = driver.findElement(By.id("password_error")).getText();
        assertEquals("Mật khẩu phải có ít nhất 8 ký tự", errorMsg);
    }

    @Test
    public void testTC08_ValidPassword_Exactly8Chars() {
        fillFormAndSubmit("test@gmail.com", "12345678", "12345678");
        Alert alert = driver.switchTo().alert();
        assertEquals("Đăng ký thành công!", alert.getText());
        alert.accept();
    }

    @Test
    public void testTC09_EmptyConfirmPassword() {
        fillFormAndSubmit("test@gmail.com", "ValidPass123!", "");
        String errorMsg = driver.findElement(By.id("confirm_password_error")).getText();
        assertEquals("Vui lòng xác nhận mật khẩu", errorMsg);
    }

    @Test
    public void testTC10_PasswordMismatch() {
        fillFormAndSubmit("test@gmail.com", "ValidPass123!", "WrongPass123!");
        String errorMsg = driver.findElement(By.id("confirm_password_error")).getText();
        assertEquals("Mật khẩu xác nhận không khớp", errorMsg);
    }

    @Test
    public void testTC11_PasswordCaseSensitiveMismatch() {
        // Sai chữ hoa chữ thường
        fillFormAndSubmit("test@gmail.com", "ValidPass123!", "validpass123!");
        String errorMsg = driver.findElement(By.id("confirm_password_error")).getText();
        assertEquals("Mật khẩu xác nhận không khớp", errorMsg);
    }

    @Test
    public void testTC12_WhitespaceOnlyEmail() {
        fillFormAndSubmit("   ", "ValidPass123!", "ValidPass123!");
        String errorMsg = driver.findElement(By.id("email_error")).getText();
        assertEquals("Vui lòng nhập email", errorMsg);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}