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
package com.sohlman.profiler.spring;

import java.lang.reflect.Method;

import org.aopalliance.intercept.Invocation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.sohlman.profiler.ThreadLocalProfiler;
import com.sohlman.profiler.Watch;
import com.sohlman.profiler.Reporter;

public class ProfilerInterceptor implements MethodInterceptor {
	/**
	 * Create a new PerformanceMonitorInterceptor with a static logger.
	 */
	public ProfilerInterceptor() {
	}
	
	public ProfilerInterceptor(Reporter reporter) {
		ThreadLocalProfiler.setReporter(reporter);	
	}

	private Class getClass(Method method, Invocation invocation) {
		Class declaringClass = method.getDeclaringClass();
		if (declaringClass.isInstance(invocation.getThis())) {
			declaringClass = invocation.getThis().getClass();
		}
		return declaringClass;
	}

	/**
	 * measures time
	 */

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		Class invocatedClass = getClass(method, invocation);
		Watch watch = null;
		try {
			watch = ThreadLocalProfiler.start();
			return invocation.proceed();

		} finally {
			ThreadLocalProfiler.stop(watch, invocatedClass, method);
		}
	}

}
