package com.wedoqa.test.util.junit;

import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;

public class CustomParallelizationStrategy implements ParallelExecutionConfigurationStrategy {

	@Override
	public ParallelExecutionConfiguration createConfiguration(ConfigurationParameters configurationParameters) {
		int parallelism = Integer.parseInt(System.getProperty("threadNumber", "3"));
		return new CustomParallelExecutionConfiguration(parallelism, parallelism, 256 + parallelism, parallelism,
				30);
	}

	public class CustomParallelExecutionConfiguration implements ParallelExecutionConfiguration {

		private final int parallelism;
		private final int minimumRunnable;
		private final int maxPoolSize;
		private final int corePoolSize;
		private final int keepAliveSeconds;

		CustomParallelExecutionConfiguration(int parallelism, int minimumRunnable, int maxPoolSize, int corePoolSize,
				int keepAliveSeconds) {
			this.parallelism = parallelism;
			this.minimumRunnable = minimumRunnable;
			this.maxPoolSize = maxPoolSize;
			this.corePoolSize = corePoolSize;
			this.keepAliveSeconds = keepAliveSeconds;
		}

		@Override
		public int getParallelism() {
			return parallelism;
		}

		@Override
		public int getMinimumRunnable() {
			return minimumRunnable;
		}

		@Override
		public int getMaxPoolSize() {
			return maxPoolSize;
		}

		@Override
		public int getCorePoolSize() {
			return corePoolSize;
		}

		@Override
		public int getKeepAliveSeconds() {
			return keepAliveSeconds;
		}

	}
}