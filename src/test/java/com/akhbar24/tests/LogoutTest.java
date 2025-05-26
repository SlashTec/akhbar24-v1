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
public class LogoutTest extends BaseTest {

    private void verifyUserIsLoggedIn() {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        String userStatus = waitForElement(By.xpath("//android.view.View[@content-desc]"))
                .getAttribute("content-desc");
        Assert.assertFalse(userStatus.contains("زائر"), "❌ ما زال المستخدم زائرًا، يبدو أن تسجيل الدخول لم ينجح.");
    }

    private void verifyUserIsLoggedOut() {
        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        List<WebElement> elements = driver.findElements(By.xpath("//android.view.View[@content-desc]"));
        boolean isVisitor = false;
        for (WebElement el : elements) {
            String desc = el.getAttribute("content-desc");
            if (desc != null && desc.contains("زائر")) {
                isVisitor = true;
                break;
            }
        }
        Assert.assertTrue(isVisitor, "❌ المستخدم لا يزال مسجل دخول بعد محاولة تسجيل الخروج.");
    }

    @Test
    public void testLoginAndLogoutFlow() {
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

        waitForElement(AppiumBy.accessibilityId("تسجيل خروج")).click();
        waitForElement(AppiumBy.accessibilityId("تسجيل الخروج")).click();
        waitForElement(AppiumBy.accessibilityId("القائمة"));
        verifyUserIsLoggedOut();
    }

    @Test
    public void testLogin_ThenCancelLogout() {
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

        waitForElement(AppiumBy.accessibilityId("تسجيل خروج")).click();
        waitForElement(AppiumBy.accessibilityId("إلغاء")).click();
        waitForElement(AppiumBy.accessibilityId("القائمة"));
        verifyUserIsLoggedIn();
    }
}