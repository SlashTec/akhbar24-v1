package com.akhbar24.tests;

import com.akhbar24.utils.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
public class VideoSectionTest extends BaseTest {

    private void verifyUserIsLoggedIn() {
        boolean isHomeVisible = driver.getPageSource().contains("الرئيسية");
        Assert.assertTrue(isHomeVisible, "❌ لم يتم عرض صفحة الرئيسية بعد تسجيل الدخول.");

        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        String userStatus = waitForElement(By.xpath("//android.view.View[@content-desc]"))
                .getAttribute("content-desc");
        Assert.assertFalse(userStatus.contains("زائر"), "❌ ما زال المستخدم زائرًا، يبدو أن تسجيل الدخول لم ينجح.");
    }

    @Test
    public void testVideoPlaybackFromVideosSection() {
        try {
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

            waitForElement(AppiumBy.accessibilityId("مرئيات")).click();
            WebElement firstVideo = waitForElement(By.xpath("//android.widget.ScrollView/android.view.View[1]/android.widget.ImageView[1]"));
            firstVideo.click();

            By videoIndicatorLocator = By.xpath("//android.view.View[contains(@content-desc, 'فيديو أخبار 24')]");
            WebElement freshVideoIndicator = waitForElement(videoIndicatorLocator);
            Assert.assertTrue(freshVideoIndicator.isDisplayed(), "❌ الفيديو لم يظهر أو لم يبدأ التشغيل.");

        } catch (Exception e) {
            Assert.fail("❌ حدث خطأ أثناء اختبار المرئيات / الفيديو: " + e.getMessage());
        }
    }
}