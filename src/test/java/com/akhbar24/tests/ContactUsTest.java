package com.akhbar24.tests;

import com.akhbar24.utils.BaseTest;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ContactUsTest extends BaseTest {

    @Test
    public void testContactUsSuccess() throws InterruptedException {
        System.out.println("🚀 بدء اختبار: testContactUsSuccess");
        openContactUsPage();
        fillContactForm("Asseel", "asilyacoub1@gmail.com", "Test", "رسالة تجريبية");

        hideKeyboardIfVisible();

        waitForElement(AppiumBy.accessibilityId("إرسال")).click();
        Thread.sleep(2000);

        Assert.assertTrue(
                handlePopupWithText("تم إرسال الرسالة بنجاح"),
                "❌ لم تظهر رسالة النجاح بعد الإرسال."
        );
    }

    @Test
    public void testContactUsMissingFields() throws InterruptedException {
        System.out.println("🚀 بدء اختبار: testContactUsMissingFields");
        openContactUsPage();
        fillContactForm("", "asilyacoub1@gmail.com", "Test", "رسالة بدون اسم");

        hideKeyboardIfVisible();

        waitForElement(AppiumBy.accessibilityId("إرسال")).click();
        Thread.sleep(2000);

        Assert.assertTrue(
                handlePopupWithText("الرجاء تعبئة جميع المعلومات"),
                "❌ لم تظهر رسالة تفيد بنقص البيانات."
        );
    }

    @Test
    public void testContactUsInvalidEmail() throws InterruptedException {
        System.out.println("🚀 بدء اختبار: testContactUsInvalidEmail");
        openContactUsPage();
        fillContactForm("Asseel", "asseel@gmail", "Test", "رسالة لبريد غير صحيح");

        hideKeyboardIfVisible();

        waitForElement(AppiumBy.accessibilityId("إرسال")).click();
        Thread.sleep(2000);

        Assert.assertTrue(
                handlePopupWithText("يرجى إدخال بريد إلكتروني صحيح"),
                "❌ لم تظهر رسالة خطأ للبريد الإلكتروني غير الصحيح."
        );
    }

    public void openContactUsPage() {
        System.out.println("📨 فتح القائمة...");
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();

        // ✅ ننتظر عنصر مؤكد داخل القائمة
        waitForElement(By.xpath("//android.view.View[contains(@content-desc, 'تسجيل دخول')]"));

        System.out.println("🔃 سحب لإظهار 'اتصل بنا'...");
        scrollToText("اتصل بنا");

        System.out.println("✅ النقر على 'اتصل بنا'...");
        waitForElement(AppiumBy.accessibilityId("اتصل بنا")).click();

        // ✅ تأكيد أن الفورم ظهر
        waitForElement(By.className("android.widget.EditText"));
    }

    public void fillContactForm(String name, String email, String subject, String message) {
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));

        if (fields.size() < 4) {
            throw new RuntimeException("❌ عدد الحقول أقل من 4. لم تظهر صفحة 'اتصل بنا' بشكل صحيح.");
        }

        fields.get(0).click();
        fields.get(0).sendKeys(name);

        fields.get(1).click();
        fields.get(1).sendKeys(email);

        fields.get(2).click();
        fields.get(2).sendKeys(subject);

        fields.get(3).click();
        fields.get(3).sendKeys(message);
    }

    public boolean handlePopupWithText(String expectedText) {
        List<WebElement> popup = driver.findElements(
                By.xpath("//android.view.View[contains(@content-desc, '" + expectedText + "')]")
        );
        if (!popup.isEmpty()) {
            System.out.println("📢 تم العثور على النافذة المنبثقة: " + expectedText);
            waitForElement(AppiumBy.accessibilityId("إغلاق")).click();
            return true;
        }
        System.out.println("⚠️ لم يتم العثور على النافذة المنبثقة: " + expectedText);
        return false;
    }

    public void hideKeyboardIfVisible() {
        try {
            if (driver instanceof AndroidDriver) {
                ((AndroidDriver) driver).hideKeyboard();
                System.out.println("⌨️ تم إخفاء الكيبورد");
            }
        } catch (Exception e) {
            System.out.println("⌨️ لا يوجد كيبورد ظاهر لإخفائه.");
        }
    }

    private void scrollToText(String visibleText) {
        System.out.println("🔍 محاولة السحب لإظهار العنصر: " + visibleText);
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                        "new UiSelector().descriptionContains(\"" + visibleText + "\"))"));
    }
}
