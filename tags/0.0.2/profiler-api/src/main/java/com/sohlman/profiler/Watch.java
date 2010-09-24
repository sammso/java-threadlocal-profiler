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

public class Watch {
	private static String PROFILER_NOT_STOPPED_CORRECTLY = "PROFILER NOT STOPPED CORRECTLY";
	private String methodName;
	private String className;
	private String text;
	private long start = 0;
	private long end = 0;
	private long duration = -1;
	private int level;

	Watch(Watch parent) {
		if (parent == null) {
			this.level = 0;
		} else {
			this.level = parent.getLevel() + 1;
		}
		this.parent = parent;
		start = System.currentTimeMillis();
	}

	private Watch next = null;
	private Watch firstChild = null;
	private Watch lastChild = null;
	private Watch parent = null;

	void setNext(Watch watch) {
		next = watch;
	}

	public int getLevel() {
		return this.level;
	}

	void addChild(Watch watch) {
		if (firstChild == null) {
			firstChild = watch;
			lastChild = watch;
		} else {
			lastChild.setNext(watch);
			lastChild = watch;
		}
		watch.parent = this;
	}

	Watch getNext() {
		return next;
	}

	Watch getParent() {
		return parent;
	}

	Watch getFirstChild() {
		return firstChild;
	}
	
	Watch getLastChild() {
		return lastChild;
	}

	public long getTimeToNextMillis() {
		if (getFirstChild() != null)
			return getFirstChild().getStartTimeMillis() - getStartTimeMillis();
		if (getNext() != null)
			return getNext().getStartTimeMillis() - getEndTimeMillis();
		if (getParent() != null) {
			return getMillisFromParent(getParent());
		}
		return 0;
	}

	private long getMillisFromParent(Watch watch) {
		if (watch.getNext() != null) {
			return getParent().getNext().getStartTimeMillis()
					- getStartTimeMillis();
		} else if (watch.getParent() != null) {
			return getMillisFromParent(watch.getParent());
		} else {
			return watch.getEndTimeMillis() - getEndTimeMillis();
		}
	}

	public boolean isRoot() {
		return parent == null;
	}

	public boolean hasChilds() {
		return firstChild != null;
	}

	public boolean hasNext() {
		return next != null;
	}

	public String getText() {
		return this.text;
	}

	public String getClassName() {
		return this.className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	@Override
	public boolean equals(Object object) {
		return this == object;
	}

	void stop(String className, String methodName, String text) {
		if (this.end == 0) {
			this.end = System.currentTimeMillis();
			this.duration = this.end - this.start;
			
			this.className = className;
			this.methodName = methodName;
			this.text = text;
			if( lastChild!=null) {
				lastChild.stop(null, null,PROFILER_NOT_STOPPED_CORRECTLY);
			}
		}
	}

	public long getElapsedInMillis() {
		return this.duration;
	}

	public long getStartTimeMillis() {
		return this.start;
	}

	public long getEndTimeMillis() {
		return this.end;
	}

	public boolean isRunning() {
		return this.duration == -1;
	}
}
