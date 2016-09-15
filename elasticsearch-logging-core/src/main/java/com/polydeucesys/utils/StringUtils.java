package com.polydeucesys.utils;

import java.util.Collection;
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
