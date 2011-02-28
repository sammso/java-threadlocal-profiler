package com.sohlman.profiler.reporter;

import com.sohlman.profiler.ToStringUtil;
import com.sohlman.profiler.Watch;

public abstract class AbstractReporter implements Reporter {
	private String lineSeparator;
	private long thresHoldMillis;
	private String rowIdentifier;
	private String thresholdReachedIdentifier;

	public abstract boolean isThresholdReportEnabled();
	
	public abstract void writeThresholdReport(String message);
	
	public abstract boolean isReportEnabled();

	public abstract void writeReport(String message);
	
	public AbstractReporter(long thresHoldMillis, String rowIdentifier, String thresholdReachedIdentifier) {
		this.thresHoldMillis = thresHoldMillis;
		this.rowIdentifier = rowIdentifier;
		this.thresholdReachedIdentifier = thresholdReachedIdentifier;
		this.lineSeparator = System.getProperty("line.separator");
	}
	
	@Override
	public void report(Watch[] watches) {
		if(isReportEnabled() || isThresholdReportEnabled()) {
			String message = ToStringUtil.writeReport(watches, thresHoldMillis, thresholdReachedIdentifier, rowIdentifier, lineSeparator);
		
			if(isThresholdAchieved(thresHoldMillis, watches) && isThresholdReportEnabled()) {
				writeThresholdReport(message);
			}
			else if(isReportEnabled()){
				writeReport(message);
			}
		}
	}

	private boolean isThresholdAchieved(long thresholdMillis, Watch[] watches) {
		if (watches == null || watches.length <= 0) {
			return false;
		} else {
			return watches[0].getElapsedInMillis() > thresholdMillis;
		}
	}
}
