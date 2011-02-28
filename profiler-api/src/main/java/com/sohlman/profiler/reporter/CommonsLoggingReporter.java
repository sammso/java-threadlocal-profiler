package com.sohlman.profiler.reporter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class CommonsLoggingReporter extends AbstractReporter {
	private Log log;
	
	public CommonsLoggingReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, Log log) {
		super(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier);
		this.log = log;
	}

	public CommonsLoggingReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, boolean isThresholdReportEnabled) {		
		this(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier, LogFactory.getLog(CommonsLoggingReporter.class));
	}

	@Override
	public boolean isThresholdReportEnabled() {
		return log.isWarnEnabled(); //logger.isLoggable(Level.WARNING);
	}

	@Override
	public void writeThresholdReport(String message) {
		log.warn(message);
	}

	@Override
	public boolean isReportEnabled() {
		return log.isDebugEnabled(); // logger.isLoggable(Level.FINE);
	}

	@Override
	public void writeReport(String message) {
		log.debug(message);
	}

}
