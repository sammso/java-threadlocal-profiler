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
package com.sohlman.profiler.liferay.filter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.sohlman.profiler.ThreadLocalProfiler;
import com.sohlman.profiler.Watch;

public class ProfilerFilter extends BasePortalFilter {
	public final String SKIP_FILTER = getClass().getName() + "SKIP_FILTER";

	@Override
	protected void processFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws Exception {
		if (isAlreadyFiltered(request)) {
			processFilter(ProfilerFilter.class, request,

			response, filterChain);
		} else {
			Watch watch = null;
			try {
				ThreadLocalProfiler.setup();
				watch = ThreadLocalProfiler.start();
				processFilter(ProfilerFilter.class, request,

				response, filterChain);
			} finally {
				ThreadLocalProfiler.stop(watch, request.getRequestURI() + "?"
						+ request.getQueryString());
				getLog().info(ThreadLocalProfiler.printReport());
				ThreadLocalProfiler.tearDown();
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
