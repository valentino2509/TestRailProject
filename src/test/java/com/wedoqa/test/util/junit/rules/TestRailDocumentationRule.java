package com.wedoqa.test.util.junit.rules;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.wedoqa.test.util.junit.TestBase;
import com.wedoqa.test.util.junit.TestBaseStaticConfig;
import com.wedoqa.testrail.apibinding.APIClient;

public class TestRailDocumentationRule implements AfterEachCallback {
    
    public static PrintStream printer;
    public static APIClient client;
    
    static {
        if (TestBaseStaticConfig.isDocumentation()) {
            try {
                printer = new PrintStream("logs/TestWithoutTestRailAnnotation" + System.currentTimeMillis() + ".log");
                
                client = new APIClient("https://sureclinical.testrail.net");
                client.setUser("valentino@wedoqa.co");
                client.setPassword("02NS-17.r02.S");
                
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (!TestBaseStaticConfig.isDocumentation()) {
            return;
        }
        Integer id = TestBase.getTestRailId(context);
        if (!context.getExecutionException().isPresent() && id != null) {
            // only apply log on passed tests
            File logFile = LoggingRule.getLogFile(context);
            
            List<StringBuilder> steps = new ArrayList<StringBuilder>();
            List<String> expected = new ArrayList<String>();
            StringBuilder lastStep = null;
            int verificationCount = 0;
            boolean alreadyVerified = false;
            int offCount = 0;
            for (String line: FileUtils.readLines(logFile, Charset.defaultCharset())) {
                line = line.replaceFirst("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3} ", "");
                line = line.replaceFirst(" c\\.s\\.t\\.(.)+\\] - ", "-~~-");
                String[] splitedLine = line.split("-~~-");
                if (splitedLine.length != 2) {
                    System.err.println("There should be exactly two part: " + line + " count: " + splitedLine.length);
                    continue;
                }
                String level = splitedLine[0];
                String message = splitedLine[1];
                
                if (level.equals("WARN ")) {
                    if (message.equals("off")) {
                        offCount++;
                    } else if (message.equals("on")) {
                        offCount--;
                    } else {
                        //unexpected warn message
                    }
                } else if (offCount <= 0) { 
                    if (level.equals("DEBUG")) {
                        steps.add(new StringBuilder(message));
                        lastStep = steps.get(steps.size() - 1);
                        alreadyVerified = false;
                    } else if(level.equals("INFO ")){
                        int verificationNumber;
                        
                        if (expected.contains(message)) {
                            verificationNumber = expected.indexOf(message) + 1;
                        } else {
                            verificationNumber = ++verificationCount;
                            expected.add(message);
                        }
                        
                        if (!alreadyVerified) {
                            lastStep.append(" - " + verificationNumber);
                            alreadyVerified = true;
                        } else {
                            lastStep.append(", " + verificationNumber);
                        }
                        
                    } else {
                        //System.out.println("Ignoring line: " + line);
                    }
                }
            }
            StringBuilder stepsBuilder = new StringBuilder();
            for(StringBuilder step: steps) {
                stepsBuilder.append(step.toString().replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\"") + "\\n");
            }
            
            StringBuilder verificationBuilder = new StringBuilder();
            int verificationNumber = 1;
            for (String verification:expected) {
                verificationBuilder.append(verificationNumber + " - " + verification.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\\"", "\\\\\\\"") + "\\n");
                verificationNumber++;
            }
            String json = "{ \"custom_steps\":\"" + stepsBuilder.toString() + "\", \"custom_expected\":\"" +  verificationBuilder.toString() + "\"}";
            client.sendPost("update_case/" + id, json);
            Thread.sleep(100);
        }
    }
    
}