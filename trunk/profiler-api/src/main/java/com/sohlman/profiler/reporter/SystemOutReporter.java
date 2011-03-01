/*
   Copyright 2010-2011 Sampsa Sohlman http://sampsa.sohlman.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
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
