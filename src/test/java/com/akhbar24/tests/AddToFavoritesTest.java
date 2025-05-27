package com.akhbar24.tests;


import com.akhbar24.utils.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class AddToFavoritesTest extends BaseTest {

    public static  WebElement waitForElement(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(60))
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }


    private void verifyUserIsLoggedIn() {
        boolean isHomeVisible = driver.getPageSource().contains("الرئيسية");
        Assert.assertTrue(isHomeVisible, "❌ لم يتم عرض صفحة الرئيسية بعد تسجيل الدخول.");

        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        String userStatus = waitForElement(By.xpath("//android.view.View[@content-desc]"))
                .getAttribute("content-desc");
        System.out.println("👤 الحالة الحالية للمستخدم: " + userStatus);
        Assert.assertFalse(userStatus.contains("زائر"), "❌ ما زال المستخدم زائرًا، يبدو أن تسجيل الدخول لم ينجح.");
    }

    @Test
    public void testAddNewsToFavorites() throws InterruptedException {
        System.out.println("🚀 تسجيل الدخول عبر جوجل...");

        // تسجيل الدخول بجوجل
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();
        waitForElement(By.xpath("//android.widget.ImageView[@content-desc='الدخول بحساب جوجل']")).click();
        Thread.sleep(3000);

        List<WebElement> accounts = driver.findElements(By.id("com.google.android.gms:id/account_picker_container"));
        if (!accounts.isEmpty()) {
            accounts.get(0).click();
            waitForElement(AppiumBy.accessibilityId("القائمة")); // بعد تسجيل الدخول
        } else {
            Assert.fail("❌ لم يتم العثور على نافذة اختيار الحساب.");
        }

        // الانتقال للرئيسية
        waitForElement(AppiumBy.accessibilityId("الرئيسية")).click();
        Thread.sleep(3000);

        // العثور على أول زر حفظ خبر
        List<WebElement> bookmarkIcons = driver.findElements(By.xpath("//android.widget.ImageView[@clickable='true']"));
        Assert.assertTrue(bookmarkIcons.size() > 0, "❌ لم يتم العثور على زر الحفظ.");
        bookmarkIcons.get(0).click();
        System.out.println("✅ تم النقر على زر الحفظ بنجاح (تم حفظ خبر)");

        // الانتقال إلى تبويب "أخباري"
        waitForElement(AppiumBy.accessibilityId("أخباري")).click();
        Thread.sleep(3000);

        // التأكد من وجود أي عنصر محفوظ في "أخباري"
        List<WebElement> savedItems = driver.findElements(
                By.xpath("//android.view.View[contains(@content-desc, '') and string-length(@content-desc) > 15]")
        );
        Assert.assertTrue(savedItems.size() > 0, "❌ لا يوجد محتوى محفوظ في قسم أخباري.");
        System.out.println("✅ تم التأكد من وجود محتوى في قسم أخباري.");
    }
}
