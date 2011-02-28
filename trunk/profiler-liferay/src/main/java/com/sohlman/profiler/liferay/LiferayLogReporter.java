package com.sohlman.profiler.liferay;
import com.liferay.portal.kernel.log.Log;
import com.sohlman.profiler.AbstractReporter;

public class LiferayLogReporter extends AbstractReporter {
	
	private Log log;
	
	public LiferayLogReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, Log logger) {
		super(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier);
		this.log = logger;
	}

	@Override
	public boolean isThresholdReportEnabled() {
		return log.isWarnEnabled();
	}

	@Override
	public void writeThresholdReport(String message) {
		log.warn(message);
	}

	@Override
	public boolean isReportEnabled() {
		return log.isDebugEnabled();
	}

	@Override
	public void writeReport(String message) {
		log.debug(message);
	}

}
