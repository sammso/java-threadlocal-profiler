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
