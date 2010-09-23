/*
   Copyright 2010 Sampsa Sohlman http://sampsa.sohlman.com

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
					method(level + 1, maxLevel, loopCount, noStopLevel, i);
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
	public void testDefaults() {
		// This test should be first. If the default values are
		// changed then modify also teardown()
		Assert.assertFalse(ThreadLocalProfiler.isSetupRequired());
		Assert.assertFalse(ThreadLocalProfiler.isDisabled());
	}
	
	@Test
	public void testSimple() throws Exception {
		Watch watch = ThreadLocalProfiler.start();
		Assert.assertTrue(watch.isRunning());
		ThreadLocalProfiler.stop(watch, "/root");
		Assert.assertFalse(watch.isRunning());
		Watch[] watches = ThreadLocalProfiler.report();
		Assert.assertEquals(1, watches.length);
		ThreadLocalProfiler.tearDown();
	}
	
	private String THRESHOLD = "THRESHOLD";
	private String ROWINDENTIFIER = "ROWINDENTIFIER";
	
	@Test
	public void testRecursiveWithThreshold() throws Exception {
		method(0, 2, 5);
		Watch[] watches = ThreadLocalProfiler.report();
		
		String reportString = ToStringUtil.writeReport(watches, 10, THRESHOLD,ROWINDENTIFIER);
		
		System.out.println(reportString);
		
		verifyTree(watches);
		Assert.assertEquals(31, watches.length);
		Assert.assertTrue("Verify report threshold from text report", reportString.indexOf(THRESHOLD)>=0);
		Assert.assertTrue("Verify report row identifier from text report",reportString.indexOf(ROWINDENTIFIER)>=0);
		
		ThreadLocalProfiler.tearDown();
	}
	
	@Test
	public void testRecursiveWithWithoutThreshold() throws Exception {
		method(0, 1, 5);
		Watch[] watches = ThreadLocalProfiler.report();
		
		// use big threshold
		String reportString = ToStringUtil.writeReport(watches, 10000, THRESHOLD,ROWINDENTIFIER);
		
		System.out.println(reportString);
		
		verifyTree(watches);
		Assert.assertEquals(6, watches.length);
		
		Assert.assertFalse("Verify report threshold from text report", reportString.indexOf(THRESHOLD)>=0);
		Assert.assertTrue("Verify report row identifier from text report",reportString.indexOf(ROWINDENTIFIER)>=0);
		
		ThreadLocalProfiler.tearDown();
	}	

	private void verifyTree(Watch[] watches) throws Exception {
		long totalMillis = watches[0].getElapsedInMillis();
		long millisCounter=watches[0].getTimeToNextMillis();
		
		for(int i=1 ; i< watches.length ; i++) {
			millisCounter =+ watches[i].getElapsedInMillis();
			millisCounter =+ watches[i].getElapsedInMillis();
		}
		
		Assert.assertEquals(totalMillis, totalMillis);		
	}
	
	@Test
	public void testNotAllStopped() throws Exception {
		Watch w1 = ThreadLocalProfiler.start();
		sleep(1);
		Watch w2 = ThreadLocalProfiler.start();
		sleep(1);
		Watch w3 = ThreadLocalProfiler.start();
		sleep(1);
		ThreadLocalProfiler.stop(w3, "w3");		
		ThreadLocalProfiler.stop(w1, "w1");
		
		Assert.assertEquals(w1.getEndTimeMillis(), w2.getEndTimeMillis());
		Assert.assertNotSame(w1.getEndTimeMillis(), w3.getEndTimeMillis());
	}	
	
	@Test
	public void testThreadPoolWithoutTearDown() throws Exception {
		Watch w1 = ThreadLocalProfiler.start();
		sleep(1);
		Watch w2 = ThreadLocalProfiler.start();
		sleep(1);
		ThreadLocalProfiler.stop(w2, "w2");		
		ThreadLocalProfiler.stop(w1, "w1");
		
		Watch w3 = ThreadLocalProfiler.start();
		sleep(1);
		Watch w4 = ThreadLocalProfiler.start();
		sleep(1);
		ThreadLocalProfiler.stop(w3, "w3");		
		ThreadLocalProfiler.stop(w4, "w4");
		
		Assert.assertEquals(2,ThreadLocalProfiler.report().length);		
	}
	
	@Test 
	public void testDisabled() {
		ThreadLocalProfiler.setDisabled(true);
		Assert.assertNull(ThreadLocalProfiler.start());
		ThreadLocalProfiler.setDisabled(false);
		Assert.assertNotNull(ThreadLocalProfiler.start());
	}
	
	@Test 
	public void testSetupRequiredTrue_Start_Setup_Start() {
		ThreadLocalProfiler.setSetupRequired(true);
		Assert.assertNull(ThreadLocalProfiler.start());
		ThreadLocalProfiler.setUp();
		Assert.assertNotNull(ThreadLocalProfiler.start());
	}	
	
	@Test 
	public void testSetupFalse_Start_SetupTrue_Stop() {
		ThreadLocalProfiler.setSetupRequired(false);
		Watch watch = ThreadLocalProfiler.start();
		ThreadLocalProfiler.setSetupRequired(true);
		ThreadLocalProfiler.stop(watch,"");
		Assert.assertEquals(1, ThreadLocalProfiler.report().length);
		Assert.assertNull(ThreadLocalProfiler.start());
	}		
	
	@Test 
	public void testSetupTrue_Start_SetupFalse_Stop_Start_Stop() {
		ThreadLocalProfiler.setSetupRequired(true);
		ThreadLocalProfiler.setUp();
		Watch watch = ThreadLocalProfiler.start();
		Assert.assertNotNull(watch);
		ThreadLocalProfiler.setSetupRequired(false);
		ThreadLocalProfiler.stop(watch,"");
		Assert.assertEquals(1, ThreadLocalProfiler.report().length);
		
		watch = ThreadLocalProfiler.start();
		Assert.assertNotNull(watch);
		ThreadLocalProfiler.setSetupRequired(false);
		ThreadLocalProfiler.stop(watch,"");
		Assert.assertEquals(1, ThreadLocalProfiler.report().length);
	}
	
	@Test 
	public void testSetupTrue_Start_SetupFalse_Stop_Setup_Start_Stop() {
		ThreadLocalProfiler.setSetupRequired(true);
		ThreadLocalProfiler.setUp();
		Watch watch = ThreadLocalProfiler.start();
		Assert.assertNotNull(watch);
		ThreadLocalProfiler.setSetupRequired(false);
		ThreadLocalProfiler.stop(watch,"");
		Assert.assertEquals(1, ThreadLocalProfiler.report().length);
		ThreadLocalProfiler.setUp();
		watch = ThreadLocalProfiler.start();
		Assert.assertNotNull(watch);
		ThreadLocalProfiler.setSetupRequired(false);
		ThreadLocalProfiler.stop(watch,"");
		Assert.assertEquals(1, ThreadLocalProfiler.report().length);
	}	
	
	@Test 
	public void testSetupTrue_Start_Disabled_Stop_Setup_Start() {
		ThreadLocalProfiler.setSetupRequired(true);
		ThreadLocalProfiler.setUp();
		Watch watch = ThreadLocalProfiler.start();
		Assert.assertNotNull(watch);
		ThreadLocalProfiler.setDisabled(true);
		ThreadLocalProfiler.stop(watch,"");
		Assert.assertEquals(1, ThreadLocalProfiler.report().length);
		
		watch = ThreadLocalProfiler.start();
		Assert.assertNull(watch);
	}	
	
	@After 
	public void teardDown() {
		ThreadLocalProfiler.tearDown();
		ThreadLocalProfiler.setSetupRequired(false);
		ThreadLocalProfiler.setDisabled(false);
	}
}
