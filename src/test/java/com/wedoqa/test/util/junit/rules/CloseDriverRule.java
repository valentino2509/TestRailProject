package com.wedoqa.test.util.junit.rules;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import com.wedoqa.test.util.FilePathUtils;
import com.wedoqa.test.util.junit.TestBaseDriverSetupAndKill;

public class CloseDriverRule implements AfterEachCallback, BeforeEachCallback {

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		TestBaseDriverSetupAndKill testBaseDriverSetupAndKill = (TestBaseDriverSetupAndKill) context.getTestInstance().get();

		String downloadFolderPath = FilePathUtils.getInstance().getDownloadFilePath(testBaseDriverSetupAndKill);

        if (Boolean.parseBoolean(System.getProperty("debugLogging"))) {
            TestBaseDriverSetupAndKill.logger.trace("close driver");
        }
		testBaseDriverSetupAndKill.getDrivers().forEach(driver -> {
			try {
				if (!driver.toString().contains("(null)"))
					driver.quit();

			} catch (Throwable e) {
				TestBaseDriverSetupAndKill.logger.error("driver close failed " + e);
			}

		});		
		testBaseDriverSetupAndKill.deleteDownloadFolder(downloadFolderPath);
	}

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		TestBaseDriverSetupAndKill testBaseDriverSetupAndKill = (TestBaseDriverSetupAndKill) context.getTestInstance().get();
        if (Boolean.parseBoolean(System.getProperty("debugLogging"))) {
            TestBaseDriverSetupAndKill.logger.trace("Creating a requiest for a driver");
        }
		testBaseDriverSetupAndKill.createDriverForTest(context.getTestMethod().get().getName());
		
	}

}