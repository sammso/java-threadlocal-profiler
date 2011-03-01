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
package com.sohlman.profiler;

import com.sohlman.profiler.reporter.Reporter;

/**
 * This is only internal test case use.
 * 
 * NOTE: this is not thread safe at all.
 * 
 * @author Sampsa Sohlman
 *
 */
public class HelperReporter implements Reporter {	
	private Watch[] watches;
	
	@Override
	public void report(Watch[] watches) {
		this.watches = watches;
	}
	
	public Watch[] getWatches() {
		return this.watches;
	}

}
