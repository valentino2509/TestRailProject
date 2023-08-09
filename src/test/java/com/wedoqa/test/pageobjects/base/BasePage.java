package com.wedoqa.test.pageobjects.base;

public class BasePage extends BasePageObject {

	public BasePage(BaseInformations baseInformations, Object... variables) {
		super(baseInformations, true, variables);
		logger.trace("Currently on page: " + this.getClass().getSimpleName());
	}
	
	public String getCurrentUrl() {
	    return getDriver().getCurrentUrl();
	}
	
	public void navigateBack() {
		getDriver().navigate().back();
	}

}