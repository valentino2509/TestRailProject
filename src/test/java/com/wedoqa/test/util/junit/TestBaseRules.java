package com.wedoqa.test.util.junit;

import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

import com.wedoqa.test.util.junit.rules.BrowserConsoleLogRule;
import com.wedoqa.test.util.junit.rules.CloseDriverRule;
import com.wedoqa.test.util.junit.rules.LoggingRule;
import com.wedoqa.test.util.junit.rules.LongStepAlertRule;
import com.wedoqa.test.util.junit.rules.ScreenshotRule;
import com.wedoqa.test.util.junit.rules.SkipPassedTest;
import com.wedoqa.test.util.junit.rules.SkipTestsWithoutTestRailId;
import com.wedoqa.test.util.junit.rules.TestRailDocumentationRule;
import com.wedoqa.test.util.junit.rules.TestRailRule;

@ExtendWith(LongStepAlertRule.class)
@ExtendWith(LoggingRule.class)
@ExtendWith(SkipTestsWithoutTestRailId.class)
@ExtendWith(SkipPassedTest.class)
@ExtendWith(SetTestRailCaseId.class)
@ExtendWith(CloseDriverRule.class)
@ExtendWith(BrowserConsoleLogRule.class)
@ExtendWith(ScreenshotRule.class)
@ExtendWith(TestRailRule.class)
@ExtendWith(TestRailDocumentationRule.class)
@Timeout(2400)//40 min timeout

public class TestBaseRules extends TestBaseDriverSetupAndKill {
	
	public TestBaseRules() {
		super();
	}

}