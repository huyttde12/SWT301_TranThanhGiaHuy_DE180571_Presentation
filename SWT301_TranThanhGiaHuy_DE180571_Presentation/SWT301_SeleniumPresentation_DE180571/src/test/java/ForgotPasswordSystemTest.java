import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ForgotPasswordSystemTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        //
        driver.get("file:///C:/Users/Dell/Downloads/TrangQuenMatKhau.html");
    }

    private void requestReset(String email) {
        driver.findElement(By.id("reset_email")).clear();
        driver.findElement(By.id("reset_email")).sendKeys(email);
        driver.findElement(By.id("reset_btn")).click();
    }

    @Test
    public void testTC01_SuccessResetRequest() {
        requestReset("test@gmail.com"); // Email đúng có trong hệ thống
        Alert alert = driver.switchTo().alert();
        assertEquals("Đường link khôi phục đã được gửi vào hòm thư của bạn!", alert.getText());
        alert.accept();
    }

    @Test
    public void testTC02_EmptyEmail() {
        requestReset("");
        assertEquals("Vui lòng nhập email", driver.findElement(By.id("reset_email_error")).getText());
    }

    @Test
    public void testTC03_InvalidEmailFormat() {
        requestReset("testgmail.com");
        assertEquals("Email không hợp lệ", driver.findElement(By.id("reset_email_error")).getText());
    }

    @Test
    public void testTC04_UnregisteredEmail() {
        requestReset("chuadangky@gmail.com");
        assertEquals("Không tìm thấy tài khoản với email này", driver.findElement(By.id("reset_email_error")).getText());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) { driver.quit(); }
    }
}