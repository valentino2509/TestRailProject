package com.wedoqa.testrail.entities;

import java.io.Serializable;

import com.wedoqa.testrail.service.TestRailService;

/**
 * @author wedoqa
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -3770248751816746138L;

    private TestRailService testRailService;
    protected TestRailService getTestRailService() { return testRailService; }
    public void setTestRailService(TestRailService testRailService) { this.testRailService = testRailService; }
}
