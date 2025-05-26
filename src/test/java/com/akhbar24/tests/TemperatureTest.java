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
        try {
            waitForElement(AppiumBy.accessibilityId("القائمة")).click();
            By tempLocator = By.xpath("//android.view.View[contains(@content-desc, '°C')]");
            WebElement tempElement = waitForElement(tempLocator);

            String content = tempElement.getAttribute("content-desc");
            boolean hasValidTemp = content.matches(".*\\d+\\s?°C.*");


            Assert.assertTrue(hasValidTemp, "❌ لم يتم العثور على درجة حرارة صحيحة (رقم + °C).");
        } catch (Exception e) {
            Assert.fail("❌ فشل في التحقق من درجة الحرارة: " + e.getMessage());
        }
    }
}