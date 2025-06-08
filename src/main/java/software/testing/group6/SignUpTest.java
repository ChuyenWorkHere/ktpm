package software.testing.group6;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class SignUpTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

    }

    //Testcase đăng ký tài khoản thành công
    @DataProvider(name = "validSignUpData")
    public Object[][] validSignUpData() {
        return new Object[][] {
                {"user123ntabcde", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "user@example.com",
                        "0123456789", "123 Đường ABC"}
        };
    }

    //Testcase thiếu trường dữ liệu bắt buộc
    @DataProvider(name = "missingFieldData")
    public Object[][] missingFieldData() {
        return new Object[][] {
                // TC2: Thiếu tên tài khoản
                {"", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "user2@example.com", "0123456789",
                        "123 Đường ABC", "Nhập tài khoản"},
                // TC3: Thiếu mật khẩu
                {"user456", "", "Nguyen Van A", "01/01/1990", "Nam", "user3@example.com", "0123456789",
                        "123 Đường ABC", "Nhập mật khẩu"},
                // TC4: Thiếu họ và tên
                {"user789", "Pass1234", "", "01/01/1990", "Nam", "user4@example.com", "0123456789",
                        "123 Đường ABC", "Nhập họ tên"},
                // TC5: Thiếu ngày sinh
                {"user101", "Pass1234", "Nguyen Van A", "", "Nam", "user5@example.com", "0123456789",
                        "123 Đường ABC", "Nhập ngày sinh"},
                // TC6: Thiếu email
                {"user102", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "", "0123456789",
                        "123 Đường ABC", "Nhập email"},
                // TC7: Thiếu số điện thoại
                {"user103", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "user6@example.com",
                        "", "123 Đường ABC", "Nhập số điện thoại"},
                // TC8: Thiếu địa chỉ
                {"user104", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "user7@example.com",
                        "0123456789", "", "Nhập địa chỉ"}
        };
    }

    //Testcase các trường dữ liệu không hợp lệ
    @DataProvider(name = "invalidInputData")
    public Object[][] invalidInputData() {
        return new Object[][] {
                // TC9: Email không hợp lệ
                {"user105ntnnt", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "chickaakaka.com",
                        "0123456789", "123 Đường ABC", "Email không hợp lệ"},
                // TC10: Tên tài khoản không hợp lệ
                {"bc", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "user8@example.com",
                        "0123456789", "123 Đường ABC", "Tên tài khoản phải ít nhất 6 ký tự"},
                // TC11: Họ tên không hợp lệ
                {"user106nt", "Pass1234", "vcc@", "01/01/1990", "Nam", "user9@example.com",
                        "0123456789", "123 Đường ABC", "Họ và tên không hợp lệ"},
                // TC12: Ngày sinh không hợp lệ
                {"user107nt", "Pass1234", "Nguyen Van A", "01/01/2026", "Nam", "user10@example.com",
                        "0123456789", "123 Đường ABC", "Ngày sinh không hợp lệ"},
                // TC13: SĐT không hợp lệ
                {"user108nt", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "user11@example.com",
                        "abc123", "123 Đường ABC", "SĐT không hợp lệ"},
                // TC14: Mật khẩu không hợp lệ
                {"user09812", "a", "Nguyen Van A", "01/01/1990", "Nam", "user12@example.com",
                        "0123456789", "123 Đường ABC", "Mật khẩu phải đủ 6 đến 20 ký tự"}
        };
    }

    //Testcase với trường dữ liệu đã tồn tại
    @DataProvider(name = "existingData")
    public Object[][] existingData() {
        return new Object[][] {
                // TC15: Tài khoản đã tồn tại
                {"existingUser", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "user13@example.com",
                        "0123456789", "123 Đường ABC", "Tài khoản đã có người sử dụng"},
                // TC16: SĐT đã tồn tại
                {"user110", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "user14@example.com",
                        "existingPhone", "123 Đường ABC", "Số điện thoại đã có người sử dụng"},
                // TC17: Email đã tồn tại
                {"user111", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam", "existingEmail@example.com",
                        "0123456789", "123 Đường ABC", "Email đã có người sử dụng"}
        };
    }

    @Test(dataProvider = "validSignUpData", description = "TC1: Kiểm thử đăng ký tài khoản thành công")
    public void testSignUpSuccess(String username, String password, String fullName, String birthYear,
                                  String gender, String email, String phone, String address) {
        // Mở trang chủ
        driver.get("http://hauiproj.somee.com/Default.aspx");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Nhấn nút đăng ký
        WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("LinkDK")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerButton);
        registerButton.click();

        fillForm(username, password, fullName, birthYear, gender, email, phone, address);
        WebElement submitButton = driver.findElement(By.id("ContentPlaceHolder1_btDangky"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        submitButton.click();

        // Kiểm tra nút đăng ký không hiển thị
        List<WebElement> signUpButtons = driver.findElements(By.id("LinkDK"));
        boolean isSignUpButtonDisplayed = !signUpButtons.isEmpty() && signUpButtons.get(0).isDisplayed();
        assertFalse(isSignUpButtonDisplayed, "Nút đăng ký hiển thị trên trang chủ khi đăng ký thành công!");
    }

    @Test(dataProvider = "missingFieldData", description = "TC2-TC8: Kiểm thử đăng ký với thiếu trường bắt buộc")
    public void testMissingFields(String username, String password, String fullName, String birthYear,
                                  String gender, String email, String phone, String address, String expectedError) {
        // Mở trang chủ
        driver.get("http://hauiproj.somee.com/Default.aspx");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Nhấn nút đăng ký
        WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("LinkDK")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerButton);
        registerButton.click();

        fillForm(username, password, fullName, birthYear, gender, email, phone, address);
        WebElement submitButton = driver.findElement(By.id("ContentPlaceHolder1_btDangky"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        submitButton.click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ContentPlaceHolder1_lblThongBao")));
        assertTrue(errorMessage.isDisplayed(), "Không hiển thị thông báo lỗi: " + expectedError);
    }

    @Test(dataProvider = "invalidInputData", description = "TC9-TC14: Kiểm thử đăng ký với dữ liệu không hợp lệ")
    public void testInvalidInputs(String username, String password, String fullName, String birthYear,
                                  String gender, String email, String phone, String address, String expectedError) {
        // Mở trang chủ
        driver.get("http://hauiproj.somee.com/Default.aspx");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Nhấn nút đăng ký
        WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("LinkDK")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerButton);
        registerButton.click();

        fillForm(username, password, fullName, birthYear, gender, email, phone, address);
        WebElement submitButton = driver.findElement(By.id("ContentPlaceHolder1_btDangky"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        submitButton.click();

        // Kiểm tra nút thoát hiển thị
        List<WebElement> signUpButtons = driver.findElements(By.id("LinkDX"));
        boolean isSignUpButtonDisplayed = !signUpButtons.isEmpty() && signUpButtons.get(0).isDisplayed();
        assertFalse(isSignUpButtonDisplayed, "Nút đăng ký hiển thị trên trang chủ khi thông tin nhập không hợp lệ!");

        //        WebElement errorMessage = driver.findElement(By.id("ContentPlaceHolder1_lblThongBao"));
//        String actualError = errorMessage.getText();
//        assertTrue(actualError.equalsIgnoreCase(expectedError), "Thông báo lỗi không đúng. Kỳ vọng: " +
//                expectedError + ", Thực tế: " + actualError);
    }

    @Test(dataProvider = "existingData", description = "TC15-TC17: Kiểm thử đăng ký với thông tin đã tồn tại")
    public void testExistingData(String username, String password, String fullName, String birthYear,
                                 String gender, String email, String phone, String address, String expectedError) {
        // Mở trang chủ
        driver.get("http://hauiproj.somee.com/Default.aspx");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Nhấn nút đăng ký
        WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("LinkDK")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerButton);
        registerButton.click();

        fillForm(username, password, fullName, birthYear, gender, email, phone, address);
        WebElement submitButton = driver.findElement(By.id("ContentPlaceHolder1_btDangky"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", submitButton);
        submitButton.click();
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ContentPlaceHolder1_lblThongBao")));
        assertTrue(errorMessage.getText().equalsIgnoreCase(expectedError), "Không hiển thị thông báo lỗi: " + expectedError);
    }

    @Test(description = "TC18: Kiểm thử hủy bỏ đăng ký")
    public void testCancelSignUp() throws InterruptedException {
        // Mở trang đăng ký
//        driver.get("http://hauiproj.somee.com/Default.aspx");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        // Chờ nút "Quay lại" có thể nhấp
//        WebElement registerButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("LinkDK")));
//        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", registerButton);
//        registerButton.click();

        fillForm("user112", "Pass1234", "Nguyen Van A", "01/01/1990", "Nam",
                "user15@example.com", "0123456789", "123 Đường ABC");

        // Ẩn footer Somee.com để tránh che nút
        ((JavascriptExecutor) driver).executeScript("document.querySelector('a[href=\"http://somee.com\"]').style.display='none';");

        // Chờ nút "Quay lại" có thể nhấp
        WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Quay lại')]")));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", backButton);
        backButton.click();
        // Chờ URL chuyển hướng về trang chủ
        wait.until(ExpectedConditions.urlContains("Default.aspx"));

        // Kiểm tra nút đăng ký trên trang chủ
        WebElement signUpButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("LinkDK")));
        assertTrue(signUpButton.isDisplayed(), "Nút đăng ký không hiển thị trên trang chủ!");
    }

    private void fillForm(String username, String password, String fullName, String birthYear,
                          String gender, String email, String phone, String address) {
        if (!username.isEmpty()) driver.findElement(By.id("ContentPlaceHolder1_txtTaiKhoan")).sendKeys(username);
        if (!password.isEmpty()) driver.findElement(By.id("ContentPlaceHolder1_txtMatKhau")).sendKeys(password);
        if (!fullName.isEmpty()) driver.findElement(By.id("ContentPlaceHolder1_txtHoTen")).sendKeys(fullName);
        if (!birthYear.isEmpty()) driver.findElement(By.id("ContentPlaceHolder1_txtNamSinh")).sendKeys(birthYear);
        if (!gender.isEmpty()) {
            Select genderSelect = new Select(driver.findElement(By.id("ContentPlaceHolder1_dllGioiTinh")));
            genderSelect.selectByVisibleText(gender);
        }
        if (!email.isEmpty()) driver.findElement(By.id("ContentPlaceHolder1_txtEmail")).sendKeys(email);
        if (!phone.isEmpty()) driver.findElement(By.id("ContentPlaceHolder1_txtSdt")).sendKeys(phone);
        if (!address.isEmpty()) driver.findElement(By.id("ContentPlaceHolder1_txtDiaChi")).sendKeys(address);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
