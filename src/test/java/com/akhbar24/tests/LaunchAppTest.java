package com.akhbar24.tests;

import com.akhbar24.utils.BaseTest;
import com.akhbar24.utils.TestListener;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;
@Listeners(TestListener.class)
public class LaunchAppTest extends BaseTest {

    @Test
    public void testAppLaunchesSuccessfully() throws InterruptedException {
        Thread.sleep(5000);
        new PermissionHandler().handleLocationPermissionIfVisible();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement mainLogo = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//android.widget.FrameLayout[@resource-id='android:id/content']/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.widget.ImageView[1]")));

        Assert.assertTrue(mainLogo.isDisplayed(), "الشعار غير ظاهر بعد التشغيل.");
    }
}