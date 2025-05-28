package com.akhbar24.utils;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.net.URL;
import java.time.Duration;

@Listeners({AllureTestNg.class, TestListener.class})
public class BaseTest {

    public static AppiumDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setDeviceName("Pixel 7a");
        options.setAutomationName("UiAutomator2");
        options.setApp("C:\\Users\\user\\Downloads\\app-release (6).apk");
        options.setAppWaitDuration(Duration.ofSeconds(60));
        options.setAutoGrantPermissions(true);
        options.setCapability("uiautomator2ServerLaunchTimeout", 60000); // 60 ثانية
        options.setCapability("adbExecTimeout", 60000);
        options.setCapability("appWaitDuration", 60000);



      URL serverURL = new URL(" https://86d5-82-212-126-176.ngrok-free.app/wd/hub");
       // URL serverURL = new URL("http://127.0.0.1:4723/wd/hub");
        System.out.println(" جاري إنشاء الجلسة...");
        driver = new AndroidDriver(serverURL, options);
        System.out.println(" تم إنشاء الجلسة بنجاح: " + driver.getSessionId());
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }


    public static WebElement waitForElement(By locator) {
        return new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (driver != null) {
            driver.quit();
            System.out.println("🛑 تم إنهاء جلسة Appium بعد هذا الاختبار.");
        }}



}