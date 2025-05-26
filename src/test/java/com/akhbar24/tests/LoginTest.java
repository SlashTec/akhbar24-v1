package com.akhbar24.tests;

import com.akhbar24.utils.BaseTest;
import com.akhbar24.utils.TestListener;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

@Listeners(TestListener.class)
public class LoginTest extends BaseTest {

    private void verifyUserIsLoggedIn() throws InterruptedException {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        List<WebElement> views = driver.findElements(By.xpath("//android.view.View[@content-desc]"));
        boolean stillGuest = false;
        for (int i = 0; i < views.size(); i++) {
            try {
                WebElement view = driver.findElements(By.xpath("//android.view.View[@content-desc]"))
                        .get(i);
                String desc = view.getAttribute("content-desc");
                if (desc != null && desc.contains("زائر")) {
                    stillGuest = true;
                    System.out.println("المستخدم ما زال زائرًا: " + desc);
                    break;
                }
            } catch (Exception e) {
                System.out.println("عنصر محدث، تخطي العنصر رقم " + i);
            }
        }
        Assert.assertFalse(stillGuest, "❌ ما زال المستخدم زائرًا، يبدو أن تسجيل الدخول لم ينجح.");
    }

    @Test(priority = 1)
    public void testLoginSuccess_Normal() throws InterruptedException {
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
        waitForElement(AppiumBy.accessibilityId("القائمة"));
        verifyUserIsLoggedIn();
    }

    @Test(priority = 2)
    public void testLoginFail_WrongPassword() throws InterruptedException {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();

        waitForElement(By.className("android.widget.EditText"));
        List<WebElement> inputs = driver.findElements(By.className("android.widget.EditText"));

        Actions actions = new Actions(driver);
        actions.click(inputs.get(0)).perform();
        inputs.get(0).sendKeys("asilyacoub@gmail.com");

        actions.click(inputs.get(1)).perform();
        inputs.get(1).sendKeys("wrongpassword");

        waitForElement(AppiumBy.accessibilityId("تسجيل الدخول")).click();
        WebElement closeButton = waitForElement(AppiumBy.accessibilityId("إغلاق"));
        Assert.assertTrue(closeButton.isDisplayed(), "❌ لم تظهر رسالة الخطأ عند إدخال بيانات خاطئة.");
    }

    @Test(priority = 3)
    public void testLoginFail_EmptyFields() {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();
        waitForElement(AppiumBy.accessibilityId("تسجيل الدخول")).click();

        boolean alertAppeared = driver.getPageSource().contains("الرجاء تعبئة جميع المعلومات")
                || driver.getPageSource().contains("المعلومات و المحاولة مرة أخرى");

        Assert.assertTrue(alertAppeared, "❌ لم تظهر رسالة التنبيه عند ترك الحقول فارغة.");
    }

    @Test(priority = 4)
    public void testLoginSuccess_Google() throws InterruptedException {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();

        waitForElement(By.xpath("//android.widget.ImageView[@content-desc='الدخول بحساب جوجل']")).click();
        waitForElement(AppiumBy.accessibilityId("القائمة"));

        List<WebElement> accounts = driver.findElements(By.id("com.google.android.gms:id/account_picker_container"));
        if (!accounts.isEmpty()) {
            accounts.get(0).click();
            waitForElement(AppiumBy.accessibilityId("القائمة"));
            verifyUserIsLoggedIn();
        } else {
            Assert.fail("❌ لم يتم العثور على نافذة اختيار الحساب.");
        }
    }

    @Test(priority = 5)
    public void testLoginCancel_Google() throws InterruptedException {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();
        waitForElement(AppiumBy.accessibilityId("الدخول بحساب جوجل")).click();

        waitForElement(AppiumBy.accessibilityId("القائمة"));
        driver.navigate().back();
        waitForElement(AppiumBy.accessibilityId("القائمة"));

        Assert.assertTrue(driver.getPageSource().contains("تسجيل الدخول"), "❌ لم يرجع التطبيق لشاشة تسجيل الدخول بعد الإلغاء.");
    }

    @Test(priority = 6)
    public void testLoginWithFacebookButtonPresence() throws InterruptedException {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();
        WebElement fbLoginBtn = waitForElement(AppiumBy.accessibilityId("الدخول بحساب الفيسبوك"));
        waitForElement(AppiumBy.accessibilityId("القائمة"));
        Assert.assertTrue(fbLoginBtn.isDisplayed(), "❌ زر تسجيل الدخول بالفيسبوك غير ظاهر.");
    }
}