import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UpdateProfileSystemTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.manage().window().maximize();
        driver.get("file:///C:/Users/Dell/Downloads/TrangHoSo.html");
    }

    // Hàm gộp siêu tối ưu: Tự động nhập form
    private void fillProfile(String name, String phone, String genderId, String cityValue) {
        // Nhập Tên
        driver.findElement(By.id("fullname")).clear();
        driver.findElement(By.id("fullname")).sendKeys(name);

        // Nhập SĐT
        driver.findElement(By.id("phone")).clear();
        driver.findElement(By.id("phone")).sendKeys(phone);

        // Chọn Giới tính (Nếu truyền vào ID thì mới click)
        if (genderId != null && !genderId.isEmpty()) {
            driver.findElement(By.id(genderId)).click();
        }

        // Chọn Thành phố (Dropdown)
        if (cityValue != null) {
            Select cityDropdown = new Select(driver.findElement(By.id("city")));
            cityDropdown.selectByValue(cityValue);
        }

        // Bấm nút Lưu
        driver.findElement(By.id("update_btn")).click();
    }


    @Test
    public void testTC01_SuccessUpdate() {
        // Điền full dữ liệu đúng (Tên, SĐT, ID Giới tính, Value Thành phố)
        fillProfile("Nguyen Van A", "0901234567", "gender_male", "DN");
        Alert alert = driver.switchTo().alert();
        assertEquals("Cập nhật hồ sơ thành công!", alert.getText());
        alert.accept(); // Bấm OK đóng Popup
    }

    @Test
    public void testTC02_EmptyName() {
        fillProfile("", "0901234567", "gender_male", "HN");
        assertEquals("Họ tên không được để trống", driver.findElement(By.id("name_error")).getText());
    }

    @Test
    public void testTC03_NameContainsNumbers() {
        fillProfile("Nguyen Van 123", "0901234567", "gender_male", "HN");
        assertEquals("Họ tên không được chứa số hoặc ký tự đặc biệt", driver.findElement(By.id("name_error")).getText());
    }

    @Test
    public void testTC04_EmptyPhone() {
        fillProfile("Nguyen Van A", "", "gender_female", "HCM");
        assertEquals("Số điện thoại không được để trống", driver.findElement(By.id("phone_error")).getText());
    }

    @Test
    public void testTC05_PhoneContainsLetters() {
        fillProfile("Nguyen Van A", "0901234abc", "gender_female", "HCM");
        assertEquals("Số điện thoại chỉ được chứa chữ số", driver.findElement(By.id("phone_error")).getText());
    }

    @Test
    public void testTC06_PhoneNotStartingWithZero() {
        fillProfile("Nguyen Van A", "8490123456", "gender_female", "HCM");
        assertEquals("Số điện thoại phải bắt đầu bằng số 0", driver.findElement(By.id("phone_error")).getText());
    }

    @Test
    public void testTC07_PhoneTooShort() {
        fillProfile("Nguyen Van A", "090123456", "gender_female", "HCM"); // 9 số
        assertEquals("Số điện thoại phải có đúng 10 số", driver.findElement(By.id("phone_error")).getText());
    }

    @Test
    public void testTC08_PhoneTooLong() {
        fillProfile("Nguyen Van A", "09012345678", "gender_female", "HCM"); // 11 số
        assertEquals("Số điện thoại phải có đúng 10 số", driver.findElement(By.id("phone_error")).getText());
    }

    @Test
    public void testTC09_NoGenderSelected() {
        // Truyền null vào chỗ giới tính để bỏ qua thao tác click
        fillProfile("Nguyen Van A", "0901234567", null, "HN");
        assertEquals("Vui lòng chọn giới tính", driver.findElement(By.id("gender_error")).getText());
    }

    @Test
    public void testTC10_NoCitySelected() {
        // Truyền "" vào chỗ Thành phố để chọn dòng mặc định (-- Chọn thành phố --)
        fillProfile("Nguyen Van A", "0901234567", "gender_male", "");
        assertEquals("Vui lòng chọn một thành phố", driver.findElement(By.id("city_error")).getText());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}