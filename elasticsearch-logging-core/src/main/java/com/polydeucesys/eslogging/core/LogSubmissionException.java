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
 * An exception classed used for both exceptions thrown in this component, and as a wrapper for 
 * exceptions thrown in frameworks or components used.
 * @author Kevin McLellan
 * @version 1.0
 *
 */
public class LogSubmissionException extends Exception {

	private static final long serialVersionUID = -8041186358570402381L;
	
	/**
	 * Creates an exception with the given message
	 * @param message
	 */
	public LogSubmissionException(final String message){
		super(message);
	}
	
	/**
	 * creates an exception with the given message and wrapped cause
	 * @param message
	 * @param cause
	 */
	public LogSubmissionException(final String message, final Throwable cause){
		super(message, cause);
	}
	
}
