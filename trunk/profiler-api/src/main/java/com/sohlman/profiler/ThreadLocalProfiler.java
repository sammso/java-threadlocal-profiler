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

public class ThreadLocalProfiler {

	private static ThreadLocal<ThreadLocalProfiler> threadProfiler = new ThreadLocal<ThreadLocalProfiler>();

	private Watch root;
	private Watch current;
	private long watchCounter;
	private long lastEndTime;

	private Formatter formatter;

	private ThreadLocalProfiler() {
		// Not allowed to create
		watchCounter = 0;
	}

	public static void setup() {
		ThreadLocalProfiler profiler = threadProfiler.get();
		if (profiler == null) {
			profiler = new ThreadLocalProfiler();
			threadProfiler.set(profiler);
		}
	}

	/**
	 * @param text
	 * @return
	 */
	public static Watch start() {
		ThreadLocalProfiler profiler = threadProfiler.get();
		if (profiler != null) {
			return profiler.doStart();
		} else {
			return null;
		}
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
		watchCounter++;
		current = watch;
		return watch;
	}

	private void doStop(Watch watch, String className, String methodName,
			String text) {
		watchCounter--;
		watch.stop(className, methodName, text);
		lastEndTime = watch.getEndTimeMillis();
		current = watch.getParent();
	}

	private Watch doGetRoot() {
		return root;
	}

	public static void stop(Watch watch, String text) {
		stop(watch, (String)null, (String)null, text);
	}
	
	public static void stop(Watch watch, Class clazz, String methodName, String text) {
		stop(watch, clazz!=null?clazz.getName():"<no class>",methodName, text);
	}
	
	public static void stop(Watch watch, Class clazz, String methodName) {
		stop(watch, clazz!=null?clazz.getName():"<no class>",methodName, null);
	}	
	
	public static void stop(Watch watch, Class clazz, Method method) {
		stop(watch, clazz!=null?clazz.getName():"<no class>", method!=null?method.getName():"<no method>", null);
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

	public static Watch getRoot() {
		ThreadLocalProfiler profiler = threadProfiler.get();
		if (profiler != null) {
			return profiler.doGetRoot();
		} else {
			return null;
		}
	}
	
	public static boolean isSetupDone(){
		 return threadProfiler.get()!=null;
	}

	public static boolean isRunning() {
		ThreadLocalProfiler profiler = threadProfiler.get();
		if (profiler == null) {
			return false;
		} else {
			return profiler.isRunning();
		}
	}

	public static void tearDown() {
		threadProfiler.remove();
	}

	public static String printReport() {
		ThreadLocalProfiler profiler = threadProfiler.get();
		if (profiler == null) {
			return "";
		} else {
			return profiler.doPrintReport();
		}
	}

	private String doPrintReport() {
		StringBuilder stringBuilder = new StringBuilder(4096);
		stringBuilder
				.append("\n")
				.append(String.format("%10s %10s %10s ", "Start", "Elapsed",
						"ToNext")).append("\n");
		doPrintReport(stringBuilder, this.root, 0);
		return stringBuilder.toString();
	}

	private void doPrintReport(StringBuilder stringBuilder, Watch watch,
			int level) {
		do {
			write(watch, stringBuilder, level);
			if (watch.hasChilds()) {
				doPrintReport(stringBuilder, watch.getFirstChild(), level + 1);
			}
			watch = watch.getNext();
		} while (watch != null);
	}

	private void write(Watch watch, StringBuilder stringBuilder, int level) {
		long start = watch.getStartTimeMillis();
		long end = watch.getEndTimeMillis();
		long fromStart = watch.getStartTimeMillis() - root.getStartTimeMillis();
		long timeSpend = watch.getEndTimeMillis() - watch.getStartTimeMillis();
		long toNext = watch.getTimeMillisToNext();
		stringBuilder.append(String.format("%10d %10d %10d ", fromStart, timeSpend,
				toNext));
		addPrefixes('-', level, stringBuilder);
		boolean hasClassInfo = false;
		if (watch.getClassName() != null && watch.getMethodName() != null) {
			stringBuilder.append(watch.getClassName()).append('.')
					.append(watch.getMethodName()).append("(..)");
			hasClassInfo = true;
		} else if (watch.getText() != null) {
			if (hasClassInfo) {
				stringBuilder.append(" - ");
			}
			stringBuilder.append(watch.getText());
		}
		stringBuilder.append('\n');
	}

	private void addPrefixes(char c, int length, StringBuilder stringBuilder) {
		for (int i = 0; i < length; i++) {
			stringBuilder.append(c);
		}
	}
}
