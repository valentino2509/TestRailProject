package com.wedoqa.test.util.junit.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.wedoqa.test.util.junit.TestBase;
import com.wedoqa.test.util.junit.TestBaseStaticConfig;
import com.wedoqa.testrail.datacontainers.TestRunInfo;
import com.wedoqa.testrail.entities.TestInstance;
import com.wedoqa.testrail.entities.TestResult;
import com.wedoqa.testrail.entities.TestResults;
import com.wedoqa.testrail.entities.TestRun;

public class TestRailRule implements AfterEachCallback, AfterAllCallback {
    
    //private static final int testResultForAttachmentsId = 495351;
    
    private List<ExtensionContext> results;
    protected static final Logger logger = LoggerFactory.getLogger(TestRailRule.class);
    
    public TestRailRule() {
        super();
        this.results = Collections.synchronizedList(new ArrayList<ExtensionContext>());
    }
    
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        logger.warn("off");
        results.add(context);
    }
    
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (!TestBaseStaticConfig.isTestrail()) {
            return;
        }
        List<TestInstance> testInstances;
        TestRun testRun = TestRunInfo.getTestRun();
        if (!TestBaseStaticConfig.isPreparedrun()) {
            //put test cases to the test run
            addTestCasesToTheTestRun(results);
            //get test instances for the run
            testInstances = TestBaseStaticConfig.getTestRailService().getTests(testRun.getId());
        } else {
            testInstances = TestBaseStaticConfig.getTestInstances();
        }
        TestResults testResults = new TestResults();
        
        for (ExtensionContext result: results) {
            TestBase testClass = (TestBase) result.getTestInstance().get();
            Integer id = TestBase.getTestRailId(result);
            
            if(id == null) {
                logger.error("No testrail tag for test: " + result.getTestMethod().get().getName());
                continue;
            } else if (id <= 0)  {
                logger.error("No testrail id for test: " + result.getTestMethod().get().getName());
                continue;
            }
            TestInstance testInstance = testInstances.stream().filter(instance -> instance.getCaseId().equals(id)).findFirst().get();
            if (result.getExecutionException().isPresent()) {
                
                //test failed
                if (!TestBaseStaticConfig.isIgnoreFailures() && (result.getExecutionException().get().getMessage() == null || !result.getExecutionException().get().getMessage().equals("Skip test"))) {
                    logger.trace("set test result to fail");
                    logger.trace("TestInstance id: " + testInstance.getId());
                    //Create a new test result
                    TestResult testResult = new TestResult();
                    //testResult.setAssignedtoId(TestRunInfo.getAssignedToId());
                    testResult.setVerdict("Failed");
                    testResult.setTestId(testInstance.getId());
                    
                    String comment = null;
                    File screenshot = ScreenshotRule.getScreenshotForTest(result);
                    if (TestBaseStaticConfig.isThumbnail() && screenshot != null) {
                        String attachmentId = TestBaseStaticConfig.getTestRailService()
                                .addAttachment(testRun.getId(), screenshot);
                        comment = getCustomErrorMessage(result) + "\n![](index.php?/attachments/get/" + attachmentId + ")";
                    } else {
                        comment = getCustomErrorMessage(result);
                    }
                    testResult.setComment(comment);
                    testResults.addResult(testResult);
                }
            } else {
                //test passed
                logger.trace("set test result to success");
                
                try {
                //Create a new test result
                TestResult testResult = new TestResult();
                testResult.setVerdict("Passed");
                testResult.setTestId(testInstance.getId());
                
                String comment = "PASSED";
                
                if (TestBaseStaticConfig.isThumbnail()) {
                    String attachmentId = TestBaseStaticConfig.getTestRailService()
                            .addAttachment(testRun.getId(), ScreenshotRule.getScreenshotForTest(result));
                    comment += "\n![](index.php?/attachments/get/" + attachmentId + ")";
                }
                testResult.setComment(comment);
                testResults.addResult(testResult);
                } catch (NullPointerException npe) {
                    logger.trace("NPE: " + npe.getMessage());
                }
            }
        }
        //push test results
        if (!testResults.getResults().isEmpty()) {
            TestBaseStaticConfig.getTestRailService().addTestResults(testRun.getId(), testResults);
        }
        if (TestBaseStaticConfig.isAttachment() && !TestBaseStaticConfig.isThumbnail()) {
            //push screenshots to results
            for (ExtensionContext result: results) {
                int finalId = TestBase.getTestRailId(result);
                TestInstance testInstance = testInstances.stream().filter(instance -> instance.getCaseId().equals(finalId)).findFirst().get();
                TestResult testResult = TestBaseStaticConfig.getTestRailService().getTestResult(testInstance.getId());
                int testResultId = testResult.getId();
                TestBaseStaticConfig.getTestRailService().addAttachment(testResultId, ScreenshotRule.getScreenshotForTest(result));
            }
        }
    }
    
    public String getCustomErrorMessage(ExtensionContext context) throws Exception {
        logger.trace("getCustomErrorMessage");
        
        Integer id = TestBase.getTestRailId(context);        
        String path = "logs" + File.separator + DateTime.now().toString("yyyy-MM-dd") + File.separator + id + ".log";
        if (!new File(path).exists()) {
            path = "logs" + File.separator + DateTime.now().minusDays(1).toString("yyyy-MM-dd") + File.separator + id + ".log";
        }
        
        String lastStep = "Before the first step";
        int stepCount = 0;
        String lastVerification = null;
        boolean verified = false;
        int offCount = 0;
        try {
            for (String line: FileUtils.readLines(new File(path), Charset.defaultCharset())) {
                line = line.replaceFirst("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3} ", "");
                line = line.replaceFirst(" c\\.s\\.t\\.(.)+\\] - ", "-~~-");
                String[] splitedLine = line.split("-~~-");
                if(splitedLine.length == 2) {
                    String level = splitedLine[0];
                    String message = splitedLine[1];
                    
                    if (level.equals("WARN ")) {
                        if (message.equals("off")) {
                            offCount++;
                        } else if (message.equals("on")) {
                            offCount--;
                        }
                    } else if (offCount <= 0) { 
                        if (level.equals("DEBUG")) {
                            lastStep = message;
                            verified = false;
                            stepCount++;
                        } else if(level.equals("INFO ")){
                            lastVerification = message;
                            
                            if (!verified) {
                                verified = true;
                            }
                            
                        } else {
                            //System.out.println("Ignoring line: " + line);
                        }
                    }
                } else {
                    logger.warn("There should be exactly two part: " + line + " count: " + splitedLine.length);
                }
            }
            
            String message = "The test failed within the " + new RuleBasedNumberFormat(RuleBasedNumberFormat.ORDINAL).format(stepCount) + " step:\n\t " + lastStep.toString();
            if (verified) {
                message += "\n Verification step: \n\t" + lastVerification;
            }
            logger.trace(message);
            return message;
            
        } catch (FileNotFoundException e) {
            logger.warn("The log file is missing");
            return "The log file was not found for test case C" + id;
        }
        
    }
    
    private static synchronized void addTestCasesToTheTestRun(List<ExtensionContext> contexts) {
        TestRun testRun = TestRunInfo.getTestRun();
        List<Integer> originalTestCaseIds = new ArrayList<Integer>();
        List<Integer> newTestCaseIds = new ArrayList<Integer>();
        
        originalTestCaseIds.addAll(
                TestBaseStaticConfig.getTestRailService().getTests(testRun.getId()).stream()
                .map(testInstance -> testInstance.getCaseId())
                .collect(Collectors.toList())
                );
        newTestCaseIds.addAll(originalTestCaseIds);
        
        contexts.stream().forEach(context -> {
            Optional<Method> testMethod = context.getTestMethod();
            if (!testMethod.isPresent()) {
                return;
            }
            Integer id = TestBase.getTestRailId(context);
            String methodName = testMethod.get().getName();
            if (id == null) {
                logger.error("No testrail tag for test: " + methodName);
                return;
            }
            if (id <= 0) {
                logger.error("Ttestrail id is not a positive number for test: " + methodName);
                return;
            }
            if (!newTestCaseIds.contains(id)) {
                newTestCaseIds.add(id);
            }
        });
        
        testRun.setCaseIds(newTestCaseIds);
        
        if (!originalTestCaseIds.equals(newTestCaseIds)) {
            TestBaseStaticConfig.getTestRailService().updateTestRunTestCases(testRun);
        }
    }
    
}
