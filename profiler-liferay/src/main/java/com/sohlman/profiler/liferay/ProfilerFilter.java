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
package com.sohlman.profiler.liferay;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.sohlman.profiler.ThreadLocalProfiler;
import com.sohlman.profiler.ToStringUtil;
import com.sohlman.profiler.Watch;

public class ProfilerFilter extends BasePortalFilter {
	public final String SKIP_FILTER = getClass().getName() + "SKIP_FILTER";

	private final String PROFILE_THRESHOLDMILLIS 				= "threadlocalprofiler.thresholdmillis";
	private final String PROFILE_ROWIDENTIFIER 					= "threadlocalprofiler.rowidentfier";
	private final String PROFILE_THRESHOLDREACHED_IDENTIFIER 	= "threadlocalprofiler.thresholdreached.rowidentfier";
	private final String PROFILE_DISABLED					 	= "threadlocalprofiler.disabled";
	

	@Override
	public void init(FilterConfig filterConfig) {
		super.init(filterConfig);
		try {
			
			long thresHoldMillis = GetterUtil.get(PrefsPropsUtil.getString(PROFILE_THRESHOLDMILLIS), 5000);
			String rowIdentifier = GetterUtil.get(PrefsPropsUtil.getString(PROFILE_ROWIDENTIFIER), "THREADLOCALPROFILER");
			String thresholdReached = GetterUtil.get(PrefsPropsUtil.getString(PROFILE_THRESHOLDREACHED_IDENTIFIER), "THRESHOLD-REACHED");
			ThreadLocalProfiler.setDisabled(GetterUtil.get(PrefsPropsUtil.getString(PROFILE_DISABLED), true));
			
			LiferayLogReporter liferayLogReporter = new LiferayLogReporter(thresHoldMillis, rowIdentifier, thresholdReached, getLog());
			
			ThreadLocalProfiler.setReporter(liferayLogReporter);
			

		} catch (Exception exception) {
			getLog().error("Problem while reading properties",exception);
		}
	}

	@Override
	protected void processFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws Exception {
		if (isAlreadyFiltered(request) || 
				ThreadLocalProfiler.isDisabled()) {
			processFilter(ProfilerFilter.class, request, response, filterChain);

		} else {
			Watch watch = ThreadLocalProfiler.start();
			try {
				processFilter(ProfilerFilter.class, request, response,
						filterChain);
			} finally {
				Log log = getLog();
				String query = request.getQueryString();
				ThreadLocalProfiler.stop(
						watch,
						query == null ? request.getRequestURI() : request
								.getRequestURI() + "?" + query);
			}
		}
	}

	protected boolean isAlreadyFiltered(HttpServletRequest request) {
		if (request.getAttribute(SKIP_FILTER) != null) {
			return true;
		} else {
			request.setAttribute(SKIP_FILTER, true);
			return false;
		}
	}

}
