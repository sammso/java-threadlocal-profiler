package com.sohlman.profiler.reporter;

import com.sohlman.profiler.Watch;

/**
 * Reporter interface. This only one implementatoin is shared for all classes
 * 
 * @author Sampsa Sohlman
 */
public interface Reporter {
	
	public void report(Watch[] watches);
}
