package com.polydeucesys.eslogging.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.StatusManager;
import com.polydeucesys.eslogging.core.LogAppenderErrorHandler;
import com.polydeucesys.eslogging.core.LogSubmissionException;
import org.slf4j.LoggerFactory;
/**
 *  Copyright 2016 Polydeuce-Sys Ltd
 *
 *       Licensed under the Apache License, Version 2.0 (the "License");
 *       you may not use this file except in compliance with the License.
 *       You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *       Unless required by applicable law or agreed to in writing, software
 *       distributed under the License is distributed on an "AS IS" BASIS,
 *       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *       See the License for the specific language governing permissions and
 *       limitations under the License.
 **/

/**
 * Wraps access to the Logback {@link StatusManager} so it can be accessed from the logging implementation
 * independent L{@link com.polydeucesys.eslogging.core.LogAppenderModule}
 */
public class LogbackStatusManagerErrorHandler implements LogAppenderErrorHandler {

    @Override
    public void error(LogSubmissionException ex) {
        error(ex.getLocalizedMessage(), ex);
    }

    public void error(String msg, LogSubmissionException ex) {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusManager statusManager = lc.getStatusManager();
        ErrorStatus errorStatus = new ErrorStatus(msg, this, ex);
        statusManager.add(errorStatus);
    }
}
