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

    private void verifyUserIsLoggedIn() {
        boolean isHomeVisible = driver.getPageSource().contains("الرئيسية");
        Assert.assertTrue(isHomeVisible, "❌ لم يتم عرض صفحة الرئيسية بعد تسجيل الدخول.");

        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        String userStatus = waitForElement(By.xpath("//android.view.View[@content-desc]"))
                .getAttribute("content-desc");
        Assert.assertFalse(userStatus.contains("زائر"), "❌ ما زال المستخدم زائرًا، يبدو أن تسجيل الدخول لم ينجح.");
    }

    @Test
    public void testAddNewsToFavorites() {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        waitForElement(By.xpath("//android.view.View[@content-desc='تسجيل دخول']")).click();

        List<WebElement> inputs = driver.findElements(By.className("android.widget.EditText"));
        Actions actions = new Actions(driver);
        actions.click(inputs.get(0)).perform();
        inputs.get(0).sendKeys("asilyacoub1@gmail.com");
        actions.click(inputs.get(1)).perform();
        inputs.get(1).sendKeys("123456789");

        waitForElement(AppiumBy.accessibilityId("تسجيل الدخول")).click();
        waitForElement(AppiumBy.accessibilityId("القائمة"));
        verifyUserIsLoggedIn();

        WebElement firstBookmark = waitForElement(By.xpath("(//android.widget.ImageView[@clickable='true'])[1]"));
        firstBookmark.click();
        Assert.assertTrue(firstBookmark.isDisplayed(), "❌ لم يتم العثور على أيقونة الحفظ.");

        waitForElement(AppiumBy.accessibilityId("أخباري")).click();
        List<WebElement> savedItems = driver.findElements(By.xpath("//android.view.View[contains(@content-desc, '‫')]"));
        Assert.assertTrue(savedItems.size() > 0, "❌ لا يوجد محتوى محفوظ في قسم أخباري.");
    }
}