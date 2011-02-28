package com.sohlman.profiler.reporter;

public class SystemOutReporter extends AbstractReporter {

	private boolean isReportEnabled;
	private boolean isThresholdReportEnabled;
	
	public SystemOutReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, boolean isThresholdReportEnabled, boolean isReportEnabled) {
		super(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier);
		// TODO Auto-generated constructor stub
		this.isThresholdReportEnabled = isThresholdReportEnabled;
		this.isReportEnabled = isReportEnabled;
	}

	@Override
	public boolean isThresholdReportEnabled() {
		return isThresholdReportEnabled;
	}

	@Override
	public void writeThresholdReport(String message) {
		System.out.println(message);
	}

	@Override
	public boolean isReportEnabled() {
		// TODO Auto-generated method stub
		return isReportEnabled;
	}

	@Override
	public void writeReport(String message) {
		System.out.println(message);
	}

}
