package com.sohlman.profiler.reporter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Slf4jReporter extends AbstractReporter {
	
	private Logger logger;
	
	public Slf4jReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, Logger logger) {
		super(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier);
		this.logger = logger;
	}

	public Slf4jReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, boolean isThresholdReportEnabled) {		
		this(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier, LoggerFactory.getLogger(Slf4jReporter.class));
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
