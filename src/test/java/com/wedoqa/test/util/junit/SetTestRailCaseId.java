package com.wedoqa.test.util.junit;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class SetTestRailCaseId implements BeforeEachCallback {
    
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ((TestBase) (context.getTestInstance().get())).setTestCaseId(TestBase.getTestRailId(context));
    }
    
}