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
			String thresholdReachedIdentifier, boolean isThresholdReportEnabled, String loggerName) {		
		this(thresHoldMillis, rowIdentifier, thresholdReachedIdentifier, LoggerFactory.getLogger(loggerName));
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
