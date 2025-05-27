package com.akhbar24.tests;

import com.akhbar24.utils.BaseTest;
import com.akhbar24.utils.TestListener;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;


@Listeners(TestListener.class)
public class LoginTest extends BaseTest {

    public static WebElement waitForElement(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(60))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }


    private void verifyUserIsLoggedIn() throws InterruptedException {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        List<WebElement> views = driver.findElements(By.xpath("//android.view.View[@content-desc]"));
        boolean stillGuest = views.stream().anyMatch(v -> v.getAttribute("content-desc").contains("زائر"));
        Assert.assertFalse(stillGuest, "❌ ما زال المستخدم زائرًا، يبدو أن تسجيل الدخول لم ينجح.");
    }

    private void logoutIfLoggedIn() {
        try {
            waitForElement(AppiumBy.accessibilityId("القائمة")).click();
            if (driver.getPageSource().contains("تسجيل خروج")) {
                waitForElement(AppiumBy.accessibilityId("تسجيل خروج")).click();
                waitForElement(AppiumBy.accessibilityId("تأكيد"));
                driver.findElement(AppiumBy.accessibilityId("تأكيد")).click();
                System.out.println("🔄 تم تسجيل الخروج بنجاح.");
            } else {
                System.out.println("ℹ️ لم يكن المستخدم مسجلاً للدخول أو لم يظهر زر الخروج.");
            }
        } catch (Exception e) {
            System.out.println("⚠️ خطأ أثناء محاولة تسجيل الخروج: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    public void testLoginSuccess_Normal() throws InterruptedException {

        System.out.println(" بدء تسجيل الدخول العادي...");
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();

        waitForElement(By.className("android.widget.EditText"));
        List<WebElement> inputs = driver.findElements(By.className("android.widget.EditText"));

        Actions actions = new Actions(driver);
        actions.click(inputs.get(0)).perform();
        inputs.get(0).sendKeys("asilyacoub1@gmail.com");

        actions.click(inputs.get(1)).perform();
        inputs.get(1).sendKeys("123456789");

        waitForElement(AppiumBy.accessibilityId("تسجيل الدخول")).click();
        Thread.sleep(5000);

        verifyUserIsLoggedIn();

    }

    @Test(priority = 2)
    public void testLoginFail_WrongPassword() {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();

        waitForElement(By.className("android.widget.EditText"));
        List<WebElement> inputs = driver.findElements(By.className("android.widget.EditText"));

        Actions actions = new Actions(driver);
        actions.click(inputs.get(0)).perform();
        inputs.get(0).sendKeys("asilyacoub1@gmail.com");

        actions.click(inputs.get(1)).perform();
        inputs.get(1).sendKeys("wrongpassword");

        waitForElement(AppiumBy.accessibilityId("تسجيل الدخول")).click();
        try {
            WebElement closeButton = waitForElement(AppiumBy.accessibilityId("إغلاق"));
            Assert.assertTrue(closeButton.isDisplayed(), " لم تظهر رسالة الخطأ عند إدخال بيانات خاطئة.");
        } catch (Exception e) {
            Assert.fail(" لم تظهر نافذة الخطأ بعد إدخال كلمة مرور خاطئة.");
        }

    }

    @Test(priority = 3)
    public void testLoginFail_EmptyFields() {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();
        waitForElement(AppiumBy.accessibilityId("تسجيل الدخول")).click();

        boolean alertAppeared = driver.getPageSource().contains("الرجاء تعبئة جميع المعلومات")
                || driver.getPageSource().contains("المعلومات و المحاولة مرة أخرى");

        System.out.println(" تحقق من رسالة التنبيه: " + alertAppeared);
        Assert.assertTrue(alertAppeared, " لم تظهر رسالة التنبيه عند ترك الحقول فارغة.");
    }

    @Test(priority = 4)
    public void testLoginSuccess_Google() throws InterruptedException {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();

        waitForElement(By.xpath("//android.widget.ImageView[@content-desc='الدخول بحساب جوجل']")).click();
        Thread.sleep(3000);

        List<WebElement> accounts = driver.findElements(By.id("com.google.android.gms:id/account_picker_container"));
        if (!accounts.isEmpty()) {
            accounts.get(0).click();
            waitForElement(AppiumBy.accessibilityId("القائمة")); // انتظار حتى يظهر العنصر بعد الدخول
            verifyUserIsLoggedIn();
        } else {

            Assert.fail(" لم يتم العثور على نافذة اختيار الحساب.");
        }

    }

    @Test(priority = 5)
    public void testLoginCancel_Google() throws InterruptedException {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();
        waitForElement(AppiumBy.accessibilityId("الدخول بحساب جوجل")).click();

        Thread.sleep(3000);
        driver.navigate().back();
        Thread.sleep(3000);

        Assert.assertTrue(driver.getPageSource().contains("تسجيل الدخول"), " لم يرجع التطبيق لشاشة تسجيل الدخول بعد الإلغاء.");
    }

    @Test(priority = 6)
    public void testLoginWithFacebookButtonPresence() throws InterruptedException {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();
        WebElement fbLoginBtn = waitForElement(AppiumBy.accessibilityId("الدخول بحساب الفيسبوك"));
        Thread.sleep(3000);
        Assert.assertTrue(fbLoginBtn.isDisplayed(), " زر تسجيل الدخول بالفيسبوك غير ظاهر.");

    }

    @Test(priority = 7)
    public void successfulFacebookLogin() throws InterruptedException {

        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();
        waitForElement(AppiumBy.accessibilityId("الدخول بحساب الفيسبوك")).click();
        Thread.sleep(5000);

        try {
            int x = (42 + 1039) / 2;
            int y = (1913 + 2021) / 2;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, y));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Arrays.asList(tap));
            Thread.sleep(8000);
            System.out.println("✅ تم الضغط على زر 'متابعة باسم QA' بنجاح");
            Thread.sleep(3000);
            verifyUserIsLoggedIn();
        } catch (Exception e) {
            System.out.println("❌ فشل الضغط على زر المتابعة: " + e.getMessage());
        }
    }
}