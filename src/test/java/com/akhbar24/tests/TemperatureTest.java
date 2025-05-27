package com.akhbar24.tests;

import com.akhbar24.utils.BaseTest;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
public class TemperatureTest extends BaseTest {

    @Test
    public void testTemperatureValueIsDisplayedCorrectly() {
        System.out.println("🌡 بدء اختبار عرض درجة الحرارة في القائمة");

        waitForElement(AppiumBy.accessibilityId("القائمة")).click();

        boolean temperatureVisible = driver.findElements(
                By.xpath("//android.view.View[contains(@content-desc, '°')]")
        ).size() > 0;

        Assert.assertTrue(temperatureVisible, "❌ لم يتم العثور على عنصر يحتوي على الرمز ° الذي يمثل درجة الحرارة.");
    }


}