package com.polydeucesys.utils;

import java.util.Collection;
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
public class StringUtils {
	private StringUtils(){}
	private static final String DEFAULT_SEPARATOR=",";

    /**
     * Join an array of String with the given separator
     * @param strings
     * @param separator
     * @return
     */
	public static String join( String[] strings, String separator){
		StringBuilder sb = new StringBuilder();
		for(String s : strings){
			sb.append(s).append(separator);
		}
		if(sb.length() > 0){
			int li = sb.lastIndexOf(separator);
			sb.delete(li, sb.length());
		}
		return sb.toString();
	}

    /**
     * Join a {@link Collection} of String with the given separator
     * @param strings
     * @param separator
     * @return
     */
	public static String join( Collection<String> strings, String separator){
		return join(strings.toArray(new String[strings.size()]), separator);
	}

    /**
     * Join a {@link Collection} of String with ","
     * @param strings
     * @return
     */
	public static String join( Collection<String> strings ){
		return join(strings.toArray(new String[strings.size()]), DEFAULT_SEPARATOR);
	}

    /**
     * Split the given String on a separator and add the (trimmed) results to a given {@link Collection}
     * @param list
     * @param separator
     * @param dest
     * @return
     */
	public static Collection<String> splitToCollection(String list, String separator, Collection<String> dest){
		String[] parts = list.split(separator);
		for(String s : parts){
			if(!s.equals("")){
				dest.add(s.trim());
			}
		}
		return dest;
	}

    /**
     * Split a comma separated String and put the results in the given {@link Collection}
     * @param list
     * @param dest
     * @return
     */
	public static Collection<String> splitToCollection(String list, Collection<String> dest){
		return splitToCollection(list, DEFAULT_SEPARATOR, dest);
	}

}
