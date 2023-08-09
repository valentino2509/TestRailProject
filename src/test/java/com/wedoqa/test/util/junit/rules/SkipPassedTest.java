package com.wedoqa.test.util.junit.rules;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wedoqa.test.util.junit.TestBase;
import com.wedoqa.test.util.junit.TestBaseStaticConfig;
import com.wedoqa.testrail.annotations.TestRail;
import com.wedoqa.testrail.entities.TestInstance;
import com.wedoqa.testrail.entities.TestResult;
import com.wedoqa.testrail.entities.TestRun;

public class SkipPassedTest implements BeforeEachCallback, BeforeAllCallback{
    
    protected static final Logger logger = LoggerFactory.getLogger(SkipPassedTest.class);
    protected static final List<Integer> skipTests = new ArrayList<>();
    
    static {
        if (TestBaseStaticConfig.getTestRunId() == 0 || !TestBaseStaticConfig.isPassedSkip()) {
            logger.trace("no test to skip");
        } else {
            TestRun testRun = TestBaseStaticConfig.getTestRailService().getTestRun(TestBaseStaticConfig.getTestRunId());
            for (TestInstance testInstance: TestBaseStaticConfig.getTestRailService().getTests(testRun.getId())) {
                try {
                    int caseId = testInstance.getCaseId();
                    logger.trace("caseid: " + caseId);
                    List<TestResult> results = testInstance.getResults(5);
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        
                    }
                    if (!results.isEmpty() && results.get(0).getStatusId() == 1) {
                        skipTests.add(caseId);
                    }
                } catch (Exception e) {
                    logger.error("Exception appeared while collecting test result for case " +  testInstance.getCaseId());
                }
            }
            logger.trace("tests to skip: " + skipTests);
        }
    }
    
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (TestBaseStaticConfig.isTestrail() && TestBaseStaticConfig.isPassedSkip()) {
            Integer id = TestBase.getTestRailId(context);
            if(id == null) {
                logger.error("No testrail tag for test: " + context.getTestMethod().get().getName());
                throw new Error("Skip test");
            }
            if (skipTests.contains(id)) {
                throw new Error("Skip test");
            } else {
                logger.trace("The test case is not in the ignore list: " + id);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (TestBaseStaticConfig.isTestrail() && TestBaseStaticConfig.isPassedSkip()) {
            Class<TestBase> testClasss = (Class<TestBase>) context.getTestClass().get();
            for (Method method: testClasss.getMethods()) {
                boolean isTest = method.getAnnotation(Test.class) != null;
                boolean isDisabled = method.getAnnotation(Disabled.class) != null;
                boolean isTestRail = method.getAnnotation(TestRail.class) != null;
                if (isTest && !isDisabled && isTestRail) {
                    int testRailId = method.getAnnotation(TestRail.class).id();
                    if (!skipTests.contains(testRailId)) {
                        logger.trace("We have a test to test: " + testRailId);
                        return;
                    }
                }
            }
            logger.trace("No tests to test");
            throw new Error("Skip test class");
        }
    }
    
}