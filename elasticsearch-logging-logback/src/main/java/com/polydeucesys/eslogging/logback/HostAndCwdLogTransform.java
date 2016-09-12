package com.polydeucesys.eslogging.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.polydeucesys.eslogging.core.LogTransform;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
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
 * Adds host and current working directory information to the given {@link ILoggingEvent}, in addition
 * to any other properties provided in the constructor.
 */
public class HostAndCwdLogTransform implements LogTransform<ILoggingEvent, ILoggingEvent> {
    private static final String HOSTNAME_PROPERTY = "hostname";
    private static String hostname;

    private static final String CWD_PROPERTY = "cwd";
    private static String cwd = System.getProperty("user.dir");
    private final Map<String, String> contextProperties = new HashMap<String, String>();

    static {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostname = "Unknown";
        }
    }

    public HostAndCwdLogTransform(final Map<String, String> contextProperties){
        this.contextProperties.putAll(contextProperties);
        this.contextProperties.put(HOSTNAME_PROPERTY, hostname);
        this.contextProperties.put(CWD_PROPERTY, cwd);
    }

    @Override
    public ILoggingEvent transform(ILoggingEvent log) {
        for(String key : contextProperties.keySet()){
            log.getMDCPropertyMap().put(key, contextProperties.get(key));
        }
        return log;
    }

}

