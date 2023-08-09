package com.wedoqa.test.util.junit.rules;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wedoqa.test.util.junit.TestBase;
import com.wedoqa.test.util.junit.TestBaseStaticConfig;

public class SkipTestsWithoutTestRailId implements BeforeEachCallback, BeforeAllCallback{
    
    protected static final Logger logger = LoggerFactory.getLogger(SkipTestsWithoutTestRailId.class);
    
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        if (TestBaseStaticConfig.isTestrail() && TestBaseStaticConfig.isTestsWithoutTestrailSkip()) {
            Integer id = TestBase.getTestRailId(context);
            if (id == null) {
                throw new Error("Skip test, no testrail id");
            } else {
                logger.trace("The test has testrail id: " + id);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (TestBaseStaticConfig.isTestrail() && TestBaseStaticConfig.isTestsWithoutTestrailSkip()) {
            Class<TestBase> testClasss = (Class<TestBase>) context.getTestClass().get();
            for (Method method: testClasss.getMethods()) {
                boolean isTest = method.getAnnotation(Test.class) != null;
                boolean isDisabled = method.getAnnotation(Disabled.class) != null;
                boolean isTestId = TestBase.getTestRailId(context) != null;
                if (isTest && !isDisabled && isTestId) {
                    logger.trace("We have a test to test: " + TestBase.getTestRailId(context));
                    return;
                }
            }
            logger.trace("No tests to test");
            throw new Error("Skip test class");
        }
    }
    
}