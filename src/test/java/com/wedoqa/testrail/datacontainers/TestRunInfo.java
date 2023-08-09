package com.wedoqa.testrail.datacontainers;

import com.wedoqa.testrail.entities.*;

/**
 * Created by wedoqa
 *
 * Data Container to hold all of important information about currently running
 * test run and test case. 
 */
public class TestRunInfo {

    private static Project project;
    private static TestPlan testPlan;
    private static TestRun testRun;
    private TestInstance testInstance;
    private static TestSuite testSuite;
    private TestCase testCase;
    private static Section section;
    private static Integer assignedToId;

    public TestRunInfo() {
    	
    }

    public static Project getProject() {
        return project;
    }

    public static TestPlan getTestPlan() {
        return testPlan;
    }

    public static TestRun getTestRun() {
        return testRun;
    }

    public TestInstance getTestInstance() {
        return testInstance;
    }

    public static TestSuite getTestSuite() {
        return testSuite;
    }

    public TestCase getTestCase() {
        return testCase;
    }

    public static Section getSection() {
        return section;
    }

    public static Integer getAssignedToId() {
        return assignedToId;
    }

    public static void setProject(Project project) {
        TestRunInfo.project = project;
    }

    public static void setTestPlan(TestPlan testPlan) {
        TestRunInfo.testPlan = testPlan;
    }

    public static void setTestRun(TestRun testRun) {
        TestRunInfo.testRun = testRun;
    }

    public void setTestInstance(TestInstance testInstance) {
        this.testInstance = testInstance;
    }

    public static void setTestSuite(TestSuite testSuite) {
        TestRunInfo.testSuite = testSuite;
    }

    public void setTestCase(TestCase testCase) {
        this.testCase = testCase;
    }

    public static void setSection(Section section) {
        TestRunInfo.section = section;
    }

    public static void setAssignedToId(Integer assignedToId) {
        TestRunInfo.assignedToId = assignedToId;
    }
}