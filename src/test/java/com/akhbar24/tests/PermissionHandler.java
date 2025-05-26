package com.akhbar24.tests;

import org.openqa.selenium.By;
import static com.akhbar24.utils.BaseTest.driver;
import static com.akhbar24.utils.BaseTest.waitForElement;

public class PermissionHandler {

    public void handleLocationPermissionIfVisible() {
        try {
            By locationDialog = By.id("com.android.permissioncontroller:id/grant_dialog");
            By whileUsingAppBtn = By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button");

            if (!driver.findElements(locationDialog).isEmpty()) {
                System.out.println("ظهرت نافذة صلاحية الموقع، سيتم اختيار 'While using the app'");
                waitForElement(whileUsingAppBtn).click();
            } else {
                System.out.println("لم تظهر نافذة صلاحية الموقع، المتابعة بشكل طبيعي");
            }
        } catch (Exception e) {
            System.out.println("خطأ أثناء التعامل مع نافذة صلاحية الموقع: " + e.getMessage());
        }
    }



}
