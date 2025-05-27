package com.akhbar24.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TestListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("Starting Test: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("Test Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        try {
            File srcFile = ((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.FILE);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File screenshotsDir = new File("screenshots/" + timestamp);
            if (!screenshotsDir.exists()) {
                screenshotsDir.mkdirs();
            }

            File destFile = new File(screenshotsDir, "FAILED_" + result.getName() + ".png");
            FileUtils.copyFile(srcFile, destFile);

            // ✅ إضافة الصورة إلى Allure
            Allure.addAttachment("📸 Screenshot - " + result.getName(), new FileInputStream(destFile));

            // ✅ Stack Trace كمرفق نصي
            saveStackTrace(result.getThrowable());

            System.out.println("📸 Screenshot saved at: " + destFile.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("⚠️ Error while taking screenshot: " + e.getMessage());
        }
    }


    @Attachment(value = "📄 Stack Trace", type = "text/plain")
    public String saveStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        throwable.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }








/*
    @Override
    public void onTestFailure(ITestResult result) {
        try {
            if (BaseTest.driver == null) {
                System.err.println("Driver is null. Cannot capture screenshot.");
                return;
            }

            // حفظ الصورة في مجلد screenshots (اختياري للرجوع يدويًا)
            File srcFile = ((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File screenshotsDir = new File("screenshots/" + timestamp);
            screenshotsDir.mkdirs();
            File destFile = new File(screenshotsDir, "FAILED_" + result.getName() + ".png");
            FileUtils.copyFile(srcFile, destFile);

            System.out.println("📸 Screenshot saved at: " + destFile.getAbsolutePath());

            // إرسال الصورة إلى Allure
            attachScreenshot(((TakesScreenshot) BaseTest.driver).getScreenshotAs(OutputType.BYTES));

        } catch (Exception e) {
            System.err.println("❌ Error while taking screenshot: " + e.getMessage());
        }
    }*/



    @Attachment(value = "📸 Screenshot on Failure", type = "image/png")
    public byte[] saveScreenshot(byte[] screenshot) {
        return screenshot;
    }

    @Attachment(value = "📸 Screenshot on Failure", type = "image/png")
    public byte[] attachScreenshot(byte[] screenshot) {
        return screenshot;
    }



    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] attachScreenshot(File file) throws IOException {
        return FileUtils.readFileToByteArray(file);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("Test Skipped: " + result.getName());
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("Suite finished: " + context.getName());
    }


}