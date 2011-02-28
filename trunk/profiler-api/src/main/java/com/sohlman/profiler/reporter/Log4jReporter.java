package com.sohlman.profiler.reporter;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4jReporter extends AbstractReporter {
	
	private Logger logger;
	
	public Log4jReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, Logger logger) {
		super(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier);
		this.logger = logger;
	}

	public Log4jReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, boolean isThresholdReportEnabled) {		
		this(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier, Logger.getLogger(Log4jReporter.class));
	}

	@Override
	public boolean isThresholdReportEnabled() {
		return true;
	}

	@Override
	public void writeThresholdReport(String message) {
		logger.warn(message);
	}

	@Override
	public boolean isReportEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public void writeReport(String message) {
		logger.debug(message);
	}

}
