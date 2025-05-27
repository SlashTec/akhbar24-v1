package com.akhbar24.tests;

import com.akhbar24.utils.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class ContactUsTest extends BaseTest {

    private void scrollToText(String visibleText) {
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                        "new UiSelector().descriptionContains(\"" + visibleText + "\"))"));
    }

    // ✅ 1. حالة نجاح الإرسال
    @Test
    public void testContactUsSuccess() throws InterruptedException {
        openContactUsPage();
        fillContactForm("Asseel", "asilyacoub1@gmail.com", "Test", "رسالة تجريبية");

        waitForElement(AppiumBy.accessibilityId("إرسال")).click();
        Thread.sleep(2000);

        Assert.assertTrue(
                handlePopupWithText("تم إرسال الرسالة بنجاح"),
                "❌ لم تظهر رسالة النجاح بعد الإرسال."
        );
    }

    // ❌ 2. حالة نقص البيانات
    @Test
    public void testContactUsMissingFields() throws InterruptedException {
        openContactUsPage();
        fillContactForm("", "asilyacoub1@gmail.com", "Test", "رسالة لحالات الاختبار ");

        waitForElement(AppiumBy.accessibilityId("إرسال")).click();
        Thread.sleep(2000);

        Assert.assertTrue(
                handlePopupWithText("الرجاء تعبئة جميع المعلومات"),
                "❌ لم تظهر رسالة تفيد بنقص البيانات."
        );
    }

    // ❌ 3. حالة بريد إلكتروني غير صحيح
    @Test
    public void testContactUsInvalidEmail() throws InterruptedException {
        openContactUsPage();
        fillContactForm("Asseel", "asseel@gmail", "Test", "رسالة تجريبية لحالات الاختبار ");

        waitForElement(AppiumBy.accessibilityId("إرسال")).click();
        Thread.sleep(2000);

        Assert.assertTrue(
                handlePopupWithText("يرجى إدخال بريد إلكتروني صحيح"),
                "❌ لم تظهر رسالة خطأ للبريد الإلكتروني غير الصحيح."
        );
    }

    // ✅ دالة فتح صفحة اتصل بنا
    public void openContactUsPage() {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        scrollToText("اتصل بنا");
        waitForElement(AppiumBy.accessibilityId("اتصل بنا")).click();
    }

    // ✅ دالة تعبئة النموذج مع النقر أولاً
    public void fillContactForm(String name, String email, String subject, String message) {
        List<WebElement> fields = driver.findElements(By.className("android.widget.EditText"));

        fields.get(0).click();
        fields.get(0).sendKeys(name);

        fields.get(1).click();
        fields.get(1).sendKeys(email);

        fields.get(2).click();
        fields.get(2).sendKeys(subject);

        fields.get(3).click();
        fields.get(3).sendKeys(message);
    }

    // ✅ دالة التحقق من النافذة المنبثقة حسب النص
    public boolean handlePopupWithText(String expectedText) {
        List<WebElement> popup = driver.findElements(
                By.xpath("//android.view.View[contains(@content-desc, '" + expectedText + "')]")
        );
        if (!popup.isEmpty()) {
            waitForElement(AppiumBy.accessibilityId("إغلاق")).click();
            return true;
        }
        return false;
    }
}