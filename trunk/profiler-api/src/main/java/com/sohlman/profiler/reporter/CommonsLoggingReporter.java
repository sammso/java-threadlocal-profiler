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

	public CommonsLoggingReporter(long thresHoldMillis, String rowIdentifier,
			String thresholdReachedIdentifier, boolean isThresholdReportEnabled, String loggerName) {		
		this(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier, LogFactory.getLog(loggerName));
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
