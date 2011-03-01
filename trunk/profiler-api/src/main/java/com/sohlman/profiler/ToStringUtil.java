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

public class ToStringUtil {
	private static void addPrefixes(char c, int length,
			StringBuilder stringBuilder) {
		for (int i = 0; i < length; i++) {
			stringBuilder.append(c);
		}
	}

	

	public static String writeReport(Watch[] watches, long alarmThresHoldMillis, String thresholdReached, String rowIdentifier, String lineSeparator) {
		StringBuilder stringBuilder = new StringBuilder(4096);
		rowIdentifier = rowIdentifier==null?"":rowIdentifier;
		long rootMillis=0;
		long rootDurationMillis=0;
		if(watches.length>0) {
			rootMillis=watches[0].getStartTimeMillis();
			rootDurationMillis=watches[0].getElapsedInMillis();
		}
		stringBuilder.append(lineSeparator);
		stringBuilder.append(String.format("%23s %8s %8s %8s %s ","Timestamp:","Start:", "Elapsed:",
				"ToNext:","Action:"));
		
		if(alarmThresHoldMillis<=rootDurationMillis) {
			stringBuilder.append(thresholdReached);
			stringBuilder.append(" ");
		}		
		stringBuilder.append(rowIdentifier);
		stringBuilder.append(lineSeparator);

		for(Watch watch : watches ) {
			stringBuilder.append(String.format("%1$tY-%1$tm-%1$te %1$tH:%1$tM:%1$tS.%1$tL %2$8d %3$8d %4$8d", watch.getStartTimeMillis(), (watch.getStartTimeMillis() - rootMillis), watch.getElapsedInMillis(), watch.getTimeToNextMillis()));
			
			stringBuilder.append(' ');
			addPrefixes('-', watch.getLevel(), stringBuilder);
			boolean hasClassInfo=false;
			if (watch.getClassName() != null && watch.getMethodName() != null) {
				stringBuilder.append(watch.getClassName()).append('.')
						.append(watch.getMethodName()).append("(..)");
				hasClassInfo = true;
			} 
			if (watch.getText() != null) {
				if (hasClassInfo) {
					stringBuilder.append(" - ");
				}
				stringBuilder.append(watch.getText());
			}			
			stringBuilder.append(' ');
			if(alarmThresHoldMillis<=rootDurationMillis) {
				stringBuilder.append(thresholdReached);
				stringBuilder.append(" ");
			}
			stringBuilder.append(rowIdentifier);
			stringBuilder.append(lineSeparator);
		}
		
		
		return stringBuilder.toString();
	}
}
