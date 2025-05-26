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
    public void testSearchWithValidKeyword() {
        waitForElement(AppiumBy.accessibilityId("بحث")).click();

        WebElement fakeInput = waitForElement(By.xpath("//android.widget.ImageView[@bounds='[53,538][1028,664]']"));
        fakeInput.click();

        driver.executeScript("mobile: type", ImmutableMap.of("text", "السعودية"));
        driver.executeScript("mobile: performEditorAction", ImmutableMap.of("action", "search"));

        boolean hasResults = driver.getPageSource().contains("السعودية");
        Assert.assertTrue(hasResults, "❌ لم تظهر نتائج تحتوي على 'السعودية'");
    }
}
