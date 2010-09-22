package com.sohlman.profiler;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ThreadProfilerTest {

	private void method(int level, int maxLevel, int loopCount) {
		method(level, maxLevel, loopCount, -1, 0);
	}

	private void method(int level, int maxLevel, int loopCount,
			int noStopLevel, int loopCounter) {
		sleep(10);
		Watch watch = ThreadLocalProfiler.start();
		sleep(10);
		try {
			if (level < maxLevel) {
				for (int i = 0; i < loopCount; i++) {
					sleep(10);
					method(level + 1, maxLevel, loopCount, -1, i);
				}
			}
		} finally {
			if (level != noStopLevel) {
				ThreadLocalProfiler.stop(watch, "com.sohlman.profiler.test."
						+ level + ".Doit", "method" + loopCounter);
			}
		}
	}
	
	public void sleep(long millis) {
		try {
			Thread.currentThread().sleep(millis);
		} catch (InterruptedException e) {
			// Ignore
		}
	}
	
	@Test
	public void testSimple() throws Exception {
		ThreadLocalProfiler.setup();
		Watch watch = ThreadLocalProfiler.start();
		Assert.assertTrue(watch.isRunning());
		ThreadLocalProfiler.stop(watch, "/root");
		Assert.assertFalse(watch.isRunning());
		System.out.println(ThreadLocalProfiler.printReport());
		ThreadLocalProfiler.tearDown();
	}

	@Test
	public void watch() throws Exception {
		ThreadLocalProfiler.setup();
		Watch watch = ThreadLocalProfiler.start();
		method(0, 1, 5);
		ThreadLocalProfiler.stop(watch, "root");
		Watch[] watches = ThreadLocalProfiler.report();
		
		System.out.println(ToStringUtil.writeReport(watches, 10, "THRESHOLD","ROWINDENTIFIER"));
		
		long totalMillis = watches[0].getDurationInMillis();
		long millisCounter=watches[0].getTimeToNextMillis();
		
		for(int i=1 ; i< watches.length ; i++) {
			millisCounter =+ watches[i].getDurationInMillis();
			millisCounter =+ watches[i].getDurationInMillis();
		}
		
		Assert.assertEquals(totalMillis, totalMillis);
		
		ThreadLocalProfiler.tearDown();
	}

	@Test
	public void testRecursiveError1() throws Exception {
		ThreadLocalProfiler.setup();
		Watch watch = ThreadLocalProfiler.start();
		System.out.println("hello");
		Assert.assertTrue(watch.isRunning());
	}	

	@After
	public void tearDown() throws Exception {
	}

}
