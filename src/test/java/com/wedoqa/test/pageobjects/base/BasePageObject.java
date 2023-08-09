package com.wedoqa.test.pageobjects.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePageObject extends BasePageObjectInitializationAndWaits {
    
    protected String nameOfPageObject;
    protected String typeOfPageObject;
    
    public BasePageObject(BaseInformations baseInformations, boolean driverFactoryInitialize, Object... variables) {
        super(baseInformations, driverFactoryInitialize, variables);
    }
    
    /**
     * https://stackoverflow.com/a/13483496
     */
    protected final String xpathSafe(String origin) {
        return "concat('" + origin.replace("'", "', \"'\", '") + "', '')";
    }
    
    public Double getDoubleFromString(String number) {
        Double multCoef = 1.0;
        if(number.contains("k"))
            multCoef = 1000.0;
        return Double.parseDouble(number.replaceAll("[A-Za-z]", "").trim()) * multCoef;
    }
    
    public final void copySelectedText() {
        logger.debug("Copy selected text with the hotkey (Ctrl + C)");
        Actions actions = new Actions(getDriver());
        actions.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).build().perform();
    }
    
    public final void waitForPageLoaded() {
        if (DEBUG_LOGGING) {
            logger.trace("waitForPageLoaded");
        }
        int timeout = 0;
        
        while (timeout < 2*60*5) {
            boolean ajaxWorking = (boolean) ((JavascriptExecutor) getDriver())
                    .executeScript("try {\n" +
                            "  if (document.readyState !== 'complete') {\n" +
                            "    return false; // Page not loaded yet\n" +
                            "  }\n" +
                            "  if (window.jQuery) {\n" +
                            "    if (window.jQuery.active) {\n" +
                            "      return false;\n" +
                            "    } else if (window.jQuery.ajax && window.jQuery.ajax.active) {\n" +
                            "      return false;\n" +
                            "    }\n" +
                            "  }\n" +
                            "  if (window.angular) {\n" +
                            "    if (!window.qa) {\n" +
                            "      // Used to track the render cycle finish after loading is complete\n" +
                            "      window.qa = {\n" +
                            "        doneRendering: false\n" +
                            "      };\n" +
                            "    }\n" +
                            "    // Get the angular injector for this app (change element if necessary)\n" +
                            "    var injector = window.angular.element('body').injector();\n" +
                            "    // Store providers to use for these checks\n" +
                            "    var $rootScope = injector.get('$rootScope');\n" +
                            "    var $http = injector.get('$http');\n" +
                            "    var $timeout = injector.get('$timeout');\n" +
                            "    // Check if digest\n" +
                            "    if ($rootScope.$$phase === '$apply' || $rootScope.$$phase === '$digest' || $http.pendingRequests.length !== 0) {\n" +
                            "      window.qa.doneRendering = false;\n" +
                            "      return false; // Angular digesting or loading data\n" +
                            "    }\n" +
                            "    if (!window.qa.doneRendering) {\n" +
                            "      // Set timeout to mark angular rendering as finished\n" +
                            "      $timeout(function() {\n" +
                            "        window.qa.doneRendering = true;\n" +
                            "      }, 0);\n" +
                            "      return false;\n" +
                            "    }\n" +
                            "  }\n" +
                            "  return true;\n" +
                            "} catch (ex) {\n" +
                            "  return false;\n" +
                            "}");
            if (ajaxWorking)
                return;
            timeout++;
            if (DEBUG_LOGGING) {
                logger.trace("page loaded?");
            }
            waitFor(200);
        }
        
        throw new AssertionError("The page load not finished in 120 sec");
    }
    
    public final void hardRefresh() {
        if (DEBUG_LOGGING) {
            logger.trace("Hard refresh (Crtl + F5)");
        }
        ((JavascriptExecutor) getDriver()).executeScript("location.reload(true);");
        waitForPageLoaded();
    }
    
    public void enterValueToInputField(WebElement element, String value) {
        enterValueToInputField(element, value, true);
    }
    
    public void enterValueToInputField(WebElement element, String value, boolean checkExpected) {
        try {
            waitForElementToBeDisplayed(element);
            element.click();
            element.clear();
        } catch (Exception e) {
            
        }
        try {
            waitForValueEqual(element, "");
        } catch (Exception e) {
            element.clear();
            if (!element.getAttribute("value").isEmpty()) {
                element.sendKeys(Keys.CONTROL + "a");
                element.sendKeys(Keys.DELETE);
                if (!element.getAttribute("value").isEmpty()) {
                    throw new Error("The input field default value is not deleted");
                }
            }
        }
        element.sendKeys(value);
        if (checkExpected) {
            waitForPageLoaded();
            waitForElementToBeDisplayed(element);
            waitForValueEqual(element, value);
        }
    }
    
    public void waitForValueEqual(WebElement element, String text) {
        WebDriverWait myWait = new WebDriverWait(getDriver(), 5);
        myWait.until(ExpectedConditions.attributeToBe(element, "value", text));
    }
    
    private boolean isPageObjectNameAndPageObjectTypePresent() {
        return nameOfPageObject != null && typeOfPageObject != null;
    }
    
    private void baseLog(String fullLog, String shortLog, String traceFullLog, String traceShortLog) {
        String debugString;
        String traceString;
        if(isPageObjectNameAndPageObjectTypePresent()) {
            debugString = fullLog.trim();
            traceString = traceFullLog;
        } else {
            debugString = shortLog.trim();
            traceString = traceShortLog;
        }
        logger.debug(debugString);
        if (traceString != null) {
            logger.trace(traceString.trim());
        }
    }
    
    public void logClick(String nameOfElement, String elementType) {
        baseLog(
                String.format("Click on the \"%s\" %s on the \"%s\" %s", nameOfElement, elementType, nameOfPageObject, typeOfPageObject),
                String.format("Click on the \"%s\" %s", nameOfElement, elementType),
                null,
                null);
    }
    
    public void logClickButton(String nameOfButton) {
        logClick(nameOfButton, "button");
    }
    
    public void logSelectValue(String nameOfSelector, String value) {
        baseLog(
                String.format("Select \"%s\" on the \"%s\" %s", nameOfSelector, nameOfPageObject, typeOfPageObject),
                String.format("Select \"%s\"", nameOfSelector),
                String.format("Select \"%s\" from \"%s\" on the \"%s\" %s", value, nameOfSelector, nameOfPageObject, typeOfPageObject),
                String.format("Select \"%s\" from \"%s\"", value, nameOfSelector)
                );
    }
    
    public void logEnterValueToField(String nameOfField, String value) {
        baseLog(
                String.format("Enter value to \"%s\" field on the \"%s\" %s", nameOfField, nameOfPageObject, typeOfPageObject),
                String.format("Enter value to \"%s\" field", nameOfField),
                String.format("Enter value \"%s\" into \"%s\" on the \"%s\" %s", value, nameOfField, nameOfPageObject, typeOfPageObject),
                String.format("Enter value \"%s\" into \"%s\"", value, nameOfField)
                );
    }
    
    public void logEnterValueToTextArea(String nameOfTextArea, String value) {
        baseLog(
                String.format("Enter value to \"%s\" text area on the \"%s\" %s", nameOfTextArea, nameOfPageObject, typeOfPageObject),
                String.format("Enter value to \"%s\" text area", nameOfTextArea),
                String.format("Enter value \"%s\" into \"%s\" on the \"%s\" %s", value, nameOfTextArea, nameOfPageObject, typeOfPageObject),
                String.format("Enter value \"%s\" into \"%s\"", value, nameOfTextArea)
                );
    }
    
    public void logClickCheckbox(String nameOfCheckbox) {
        logClick(nameOfCheckbox, "checkbox");
    }
    
    public void logClickRadioButton(String nameOfRadioButton) {
        logClick(nameOfRadioButton, "radio button");
    }
    
    public void logClickSwitcher(String nameOfSwitcher) {
        logClick(nameOfSwitcher, "switcher");
    }
    
    public void logClickLink(String nameOfLink) {
        logClick(nameOfLink, "link");
    }
    
    public void logClickIcon(String nameOfLink) {
        logClick(nameOfLink, "icon");
    }
    
    public void logClickTab(String nameOfLink) {
        logClick(nameOfLink, "tab");
    }
    
    public void logClickField(String nameOfField) {
        logClick(nameOfField, "field");
    }
}
