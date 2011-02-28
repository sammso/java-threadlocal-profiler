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
