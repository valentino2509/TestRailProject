package com.wedoqa.testsuites.before;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeforeSuiteTest {

	protected static final Logger logger = LoggerFactory.getLogger(BeforeSuiteTest.class);
	
	@Test
	public void testBeforeSuite() {
		logger.trace("Before All");
	}
}