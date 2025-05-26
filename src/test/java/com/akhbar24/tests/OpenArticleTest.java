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
public class OpenArticleTest extends BaseTest {

    private void verifyUserIsLoggedIn() {
        boolean isHomeVisible = driver.getPageSource().contains("الرئيسية");
        Assert.assertTrue(isHomeVisible, "❌ لم يتم عرض صفحة الرئيسية بعد تسجيل الدخول.");

        waitForElement(AppiumBy.accessibilityId("القائمة")).click();
        String userStatus = waitForElement(By.xpath("//android.view.View[@content-desc]"))
                .getAttribute("content-desc");
        Assert.assertFalse(userStatus.contains("زائر"), "❌ ما زال المستخدم زائرًا، يبدو أن تسجيل الدخول لم ينجح.");
    }

    @Test(priority = 1)
    public void testOpenFirstArticleFromHome() {
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

        try {
            waitForElement(AppiumBy.accessibilityId("الرئيسية")).click();
        } catch (Exception e) {
            System.out.println("⚠️ لم يتم العثور على زر الرئيسية، قد تكون فعلاً على الصفحة الرئيسية.");
        }

        WebElement firstArticle = waitForElement(
                By.xpath("//android.widget.ScrollView/android.view.View/android.view.View/android.view.View[2]/android.view.View/android.view.View/android.view.View[1]"));

        firstArticle.click();
        WebElement shareIcon = waitForElement(
                By.xpath("//android.widget.ScrollView/android.widget.ImageView[4]"));

        Assert.assertTrue(shareIcon.isDisplayed(), "❌ لم يتم العثور على أيقونة المشاركة - ربما لم يتم فتح المقال بشكل صحيح");
    }

    @Test(priority = 2)
    public void testShareFunctionality() {
        WebElement shareIcon = waitForElement(By.xpath("//android.widget.ScrollView/android.widget.ImageView[4]"));
        shareIcon.click();

        WebElement sharingPopupTitle = waitForElement(By.id("com.android.intentresolver:id/headline"));
        String popupText = sharingPopupTitle.getText();
        Assert.assertTrue(popupText.contains("Sharing link"), "❌ نافذة المشاركة لم تظهر بالشكل المتوقع");
    }
}
