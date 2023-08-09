package com.wedoqa.test.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.wedoqa.test.pages.GooglePage;
import com.wedoqa.test.util.junit.TestBase;
import com.wedoqa.testrail.annotations.TestRail;

public class GoogleHomePage extends TestBase{
	
	
	@Test
	@TestRail(id=1, name="tests")
	public void testGooglePage() {
		GooglePage page = new GooglePage(getBaseInformations());
		
		Assertions.assertTrue(page.isSearchBarPresent());
		
	}

}
