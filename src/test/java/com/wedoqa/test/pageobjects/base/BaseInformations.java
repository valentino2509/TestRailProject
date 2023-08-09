package com.wedoqa.test.pageobjects.base;

import org.openqa.selenium.WebDriver;

/**
 * Class where you can get base informations for your app
 * e.g. WebDriver, Operation System, Type of Server...
 * @author wedoqa
 *
 */
public class BaseInformations {
    final WebDriver driver;
    
    public BaseInformations(WebDriver driver) {
        super();
        this.driver = driver;
    }
    
    public WebDriver getDriver() {
        return driver;
    }
}