package com.akhbar24.tests;

import com.akhbar24.utils.BaseTest;
import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
public class SearchTest extends BaseTest {


    @Test
    public void testSearchWithValidKeyword() throws InterruptedException {
        System.out.println(" بدء اختبار البحث بكلمة مفتاحية 'السعودية'");

        waitForElement(AppiumBy.accessibilityId("بحث")).click();

        //    اضغط على العنصر الوهمي لفتح الكيبورد
        WebElement fakeInput = waitForElement(By.xpath("//android.widget.ImageView[@bounds='[53,538][1028,664]']"));
        fakeInput.click();

        // ⏱ انتظر لحين تفعيل الكيبورد (حل المشاكل المتقطعة)
        Thread.sleep(1000);

        //  استخدم mobile: type بشكل آمن
        try {
            driver.executeScript("mobile: type", ImmutableMap.of("text", "السعودية"));
        } catch (Exception e) {
            System.out.println(" فشل في mobile: type، محاولة إعادة المحاولة...");
            Thread.sleep(1000);
            driver.executeScript("mobile: type", ImmutableMap.of("text", "السعودية"));
        }

        //  تنفيذ البحث
        driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "search"));

        //  تحقق من ظهور النتائج
        boolean hasResults = driver.getPageSource().contains("السعودية");
        Assert.assertTrue(hasResults, " لم تظهر نتائج تحتوي على 'السعودية'");
    }


}
