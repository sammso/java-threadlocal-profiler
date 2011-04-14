package com.sohlman.profiler.spring.beans;

import com.sohlman.profiler.TestTimer;
import com.sohlman.profiler.Timer;

public class ServiceImpl implements Service {
	private Service testInterface;
	private long sleep;
	
	public ServiceImpl(long sleep) {
		this.testInterface = testInterface;
		this.sleep = sleep;
	}	
	
	public ServiceImpl(Service testInterface, long sleep) {
		this.testInterface = testInterface;
		this.sleep = sleep;
	}
	
	@Override
	public void one() {
		if(this.testInterface!=null) {
			testInterface.one();
		}
		((TestTimer)Timer.getInstance()).sleep(this.sleep);
	}

	@Override
	public void two() {
		if(this.testInterface!=null) {
			testInterface.one();
		}
		((TestTimer)Timer.getInstance()).sleep(this.sleep);
		if(this.testInterface!=null) {
			testInterface.two();
		}
		((TestTimer)Timer.getInstance()).sleep(this.sleep);
	}

	@Override
	public void three() {
		if(this.testInterface!=null) {
			testInterface.one();
		}
		((TestTimer)Timer.getInstance()).sleep(this.sleep);
		if(this.testInterface!=null) {
			testInterface.two();
		}
		((TestTimer)Timer.getInstance()).sleep(this.sleep);
		if(this.testInterface!=null) {
			testInterface.three();
		}
		((TestTimer)Timer.getInstance()).sleep(this.sleep);		
	}

}
