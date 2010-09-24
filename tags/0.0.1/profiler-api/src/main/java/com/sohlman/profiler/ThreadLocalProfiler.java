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

import java.lang.reflect.Method;
import java.util.Formatter;

/**
 * Thread local profiler is little library for profiling measuring performance
 * of applicatons.
 * 
 * It generates tree form report where it is easy to point out system
 * bottlenecks. It collects data to ThreadLocal variable, so with this is easy
 * find out what is taking time.
 * 
 * Design is not intrusive so no additional dependencies are required.
 * 
 * Usage measuring:
 * 
 * Watch watch = ThreadLocalProfiler.start(); ThreadLocalProfiler.stop(watch,
 * getClass(), "doIt","integration time");
 * 
 * Usage: report
 * 
 * Watch[] watches = ThreadLocalProfiler.report();
 * 
 * @author Sampsa Sohlman http://sampsa.sohlman.com
 * 
 */
public class ThreadLocalProfiler {

	private static ThreadLocal<ThreadLocalProfiler> threadProfiler = new ThreadLocal<ThreadLocalProfiler>();

	private Watch root;
	private Watch current;
	private int activeWatchCounter;
	private int watchCounter;

	private ThreadLocalProfiler() {
		// Not allowed to create
		activeWatchCounter = 0;
		watchCounter = 0;
	}

	/**
	 * @param text
	 * @return
	 */
	public static Watch start() {
		ThreadLocalProfiler profiler = threadProfiler.get();
		if (!isRunning(profiler)) {
			profiler = new ThreadLocalProfiler();
			threadProfiler.set(profiler);
		}
		return profiler.doStart();
	}

	private Watch doStart() {
		Watch watch;
		if (current == null) {
			watch = new Watch(null);
			root = watch;
		} else {
			watch = new Watch(current);
			current.addChild(watch);
		}
		activeWatchCounter++;
		watchCounter++;
		current = watch;
		return watch;
	}

	public static boolean isRunning() {
		return isRunning(threadProfiler.get());
	}

	private static boolean isRunning(ThreadLocalProfiler profiler) {
		if (profiler == null) {
			return false;
		}
		else if (profiler.getRoot() == null) {
			return false;
		} else {
			return profiler.current!=null;
		}
	}
	
	
	private void doStop(Watch watch, String className, String methodName,
			String text) {
		activeWatchCounter--;
		watch.stop(className, methodName, text);
		current = watch.getParent();
	}

	Watch getRoot() {
		return root;
	}

	public static void stop(Watch watch, String text) {
		stop(watch, (String) null, (String) null, text);
	}

	public static void stop(Watch watch, Class clazz, String methodName,
			String text) {
		stop(watch, clazz != null ? clazz.getName() : "<no class>", methodName,
				text);
	}

	public static void stop(Watch watch, Class clazz, String methodName) {
		stop(watch, clazz != null ? clazz.getName() : "<no class>", methodName,
				null);
	}

	public static void stop(Watch watch, Class clazz, Method method) {
		stop(watch, clazz != null ? clazz.getName() : "<no class>",
				method != null ? method.getName() : "<no method>", null);
	}

	public static void stop(Watch watch, String className, String methodName) {
		stop(watch, className, methodName, null);
	}

	public static void stop(Watch watch, String className, String methodName,
			String text) {
		if (watch == null) {
			return;
		}
		ThreadLocalProfiler profiler = threadProfiler.get();
		if (profiler == null) {
			return;
		}
		profiler.doStop(watch, className, methodName, text);
	}

	/**
	 * Removes profiler from current thread
	 */
	public static void tearDown() {
		threadProfiler.remove();
	}

	private static Watch[] EMTPY_REPORT = new Watch[0];

	public static Watch[] report() {
		ThreadLocalProfiler profiler = threadProfiler.get();
		if (profiler == null) {
			return EMTPY_REPORT;
		} else {
			return profiler.doReport();
		}
	}

	private Watch[] doReport() {
		if (watchCounter == 0) {
			return EMTPY_REPORT;
		} else {
			Watch[] watches = new Watch[watchCounter];
			int counter = 0;
			doReport(this.root, watches, counter);
			return watches;
		}
	}

	private void doReport(Watch watch, Watch[] watches, int counter) {
		do {
			watches[counter] = watch;
			counter++;
			if (watch.hasChilds()) {
				doReport(watch.getFirstChild(), watches, counter);
			}
			watch = watch.getNext();
		} while (watch != null);
	}
}