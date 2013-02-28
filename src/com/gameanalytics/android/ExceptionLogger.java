    /* 
       Game Analytics Android Wrapper
       Copyright (c) 2013 Tim Wicksteed <tim@twicecircled.com>
       http:/www.gameanalytics.com

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

package com.gameanalytics.android;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionLogger implements UncaughtExceptionHandler {

	private final UncaughtExceptionHandler defaultHandler;

	public ExceptionLogger() {
		defaultHandler = Thread.currentThread().getUncaughtExceptionHandler();
	}

	public void uncaughtException(Thread thread, Throwable ex) {
		// Get stack trace associated with exceptions as String
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);

		// Find root cause
		Throwable cause = ex.getCause();
		while (cause.getCause() != null) {
			cause = cause.getCause();
		}

		// Log the error
		// eventId = Exception:'ExceptionType' e.g. java.lang.Arithmetic
		// message = Stack trace
		GameAnalytics.newQualityEvent(
				"Exception:" + cause.getClass().getName(), sw.toString());

		// Pass to default exception handler once data has been sent.
		defaultHandler.uncaughtException(thread, ex);
	}
}