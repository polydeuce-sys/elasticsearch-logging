package com.polydeucesys.eslogging.core;

/**
 * Copyright (c) 2016 Polydeuce-Sys Ltd
 *
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 **/
/**
 * Provides an abstraction for callbacks from asynchronous submission.
 * An example implementation would wrap a JestResultHander for Jest client
 * based communications. 
 * @author Kevin McLellan
 * @version 1.0
 *
 */
public interface AsyncSubmitCallback<R> {
	/**
	 * Called when the submission has been successful.
	 * @param result 
	 * 				Instance of the result type used in the comminucation framework (for example, JestResult)
	 */
	void completed( final R result );
	
	/**
	 * Called when the submission has failed. Any errors from the communication framework are wrapped in the {@code wrappedException}
	 * @param wrappedException {@link LogSubmissionException} wrapping error information.
	 */
	void error( final LogSubmissionException wrappedException);
}
