package com.sohlman.profiler;

public class ToStringUtil {
	private static void addPrefixes(char c, int length,
			StringBuilder stringBuilder) {
		for (int i = 0; i < length; i++) {
			stringBuilder.append(c);
		}
	}

	

	public static String writeReport(Watch[] watches, long alarmThresHoldMillis, String thresholdReached, String rowIdentifier) {
		StringBuilder stringBuilder = new StringBuilder(4096);
		rowIdentifier = rowIdentifier==null?"":rowIdentifier;
		
		long rootMillis=0;
		long rootDurationMillis=0;
		if(watches.length>0) {
			rootMillis=watches[0].getStartTimeMillis();
			rootDurationMillis=watches[0].getElapsedInMillis();
		}
		
		stringBuilder.append(String.format("\n%23s %8s %8s %8s %s ","Timestamp:","Start:", "Elapsed:",
				"ToNext:","Action:"));
		
		if(alarmThresHoldMillis<=rootDurationMillis) {
			stringBuilder.append(thresholdReached);
			stringBuilder.append(" ");
		}		
		stringBuilder.append(rowIdentifier);
		stringBuilder.append('\n');

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
			stringBuilder.append("\n");
		}
		
		
		return stringBuilder.toString();
	}
}
