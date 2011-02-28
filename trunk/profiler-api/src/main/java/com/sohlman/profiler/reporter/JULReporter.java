package com.sohlman.profiler.reporter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JULReporter extends AbstractReporter {
	private Logger logger;
	
	public JULReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, Logger logger) {
		super(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier);
		this.logger = logger;
	}

	public JULReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, boolean isThresholdReportEnabled) {		
		this(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier, Logger.getLogger(JULReporter.class.getName()));
	}

	@Override
	public boolean isThresholdReportEnabled() {
		return logger.isLoggable(Level.WARNING);
	}

	@Override
	public void writeThresholdReport(String message) {
		logger.warning(message);
	}

	@Override
	public boolean isReportEnabled() {
		return logger.isLoggable(Level.FINE);
	}

	@Override
	public void writeReport(String message) {
		logger.fine(message);
	}

}
