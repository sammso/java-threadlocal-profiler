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

/**
 * Makes possible to change the profilers Timer implementatation.
 * 
 * @author Sampsa Sohlman
 */
public class Timer {
	private static Timer timer=new Timer();
		
	public static void setTimer(Timer timer) {
		Timer.timer = timer;
	}
	/**
	 * Measures time and returns long. Time should always increase
	 * 
	 * @return
	 */
	public long time(){
		return System.currentTimeMillis();
	}
	
	public final static Timer getInstance() {
		return timer;
	}
}
