package com.wedoqa.test.util.junit;

import java.util.Set;

import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import com.wedoqa.testrail.annotations.TestRail;
import com.wedoqa.testrail.datacontainers.TestRunInfo;

@Execution(ExecutionMode.CONCURRENT)
//@ResourceLock(value = "allTests", mode = ResourceAccessMode.READ)
public class TestBase extends TestBaseRules {
  
  private TestRunInfo testRunInfo = new TestRunInfo();
  
  public TestRunInfo getTestRunInfo() {
      return testRunInfo;
  }
  
  protected RandomStringGenerator randomNumericStringGenerator = new RandomStringGenerator.Builder()
          .withinRange('0', '9')
          .build();
  
  public TestBase() {
      super();
  }
  
  protected String winHandleBefore;
  protected Set<String> winHandlesBefore;
  
  public static final String SPECIAL_CHARACTERS = "!@#$%^&*?_~";
  
  public static String getFileName(ExtensionContext context) {
      String fileName;
      Integer id = TestBase.getTestRailId(context);
      if (id != null) {
          fileName = "" + id;
      } else {
          fileName = context.getTestClass().get().getCanonicalName() + "-"
                  + context.getTestMethod().get().getName();
      }
      return fileName;
  }
  
  public static Integer getTestRailId(ExtensionContext context) {
      if (context.getTestMethod().get().isAnnotationPresent(TestRail.class)) {
          TestRail annotation = context.getTestMethod().get().getAnnotation(TestRail.class);
          return annotation.id();
      } else {
          return -1;
      }
  }
}
