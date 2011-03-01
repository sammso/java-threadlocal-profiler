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
