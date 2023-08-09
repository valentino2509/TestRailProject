package com.wedoqa.test.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.wedoqa.test.constants.ServerURL;
import com.wedoqa.test.pageobjects.base.BaseInformations;
import com.wedoqa.test.pageobjects.base.BasePage;
import com.wedoqa.test.pageobjects.base.BasePageObject;

public class GooglePage extends BasePage{


	@FindBy(css = "textarea[type='search']")
	private WebElement searchInput;

	public GooglePage(BaseInformations baseInformations) {
		super(baseInformations);
		nameOfPageObject = "Search";
		typeOfPageObject = "page";
	}
	
	@Override
	protected void isLoaded() {
		String currentUrl = getDriver().getCurrentUrl();
        if (currentUrl == null 
                || !(currentUrl.equals(ServerURL.QA_URL) 
                        )) {
            throw new Error("The page url is incorrect: " + currentUrl);
        }
        waitForPageLoaded();
        waitForElementToBeDisplayed(searchInput);
        
	}
	
	@Override
    protected void load() {
        if (!getDriver().getCurrentUrl().equals(ServerURL.QA_URL)) {
            logger.debug("Go to the login page of your server");
            logger.trace("Curent url: " + getDriver().getCurrentUrl());
            logger.trace("Server url: " + ServerURL.QA_URL);
            getDriver().get("http://www.google.com");
            getDriver().get(ServerURL.QA_URL);
        }
        initializeWithoutGet();
    }
	
	public boolean isSearchBarPresent() {
		return isElementDisplayed(searchInput);
	}

}
