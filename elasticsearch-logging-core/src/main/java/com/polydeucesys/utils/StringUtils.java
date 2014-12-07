package com.polydeucesys.utils;

import java.util.Collection;

public class StringUtils {
	private StringUtils(){}
	private static final String DEFAULT_SEPARATOR=",";
	
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
	
	public static String join( Collection<String> strings, String separator){
		return join(strings.toArray(new String[strings.size()]), separator);
	}
	
	public static String join( Collection<String> strings ){
		return join(strings.toArray(new String[strings.size()]), DEFAULT_SEPARATOR);
	}
	
	public static Collection<String> splitToCollection(String list, String separator, Collection<String> dest){
		String[] parts = list.split(separator);
		for(String s : parts){
			if(!s.equals("")){
				dest.add(s.trim());
			}
		}
		return dest;
	}
	
	public static Collection<String> splitToCollection(String list, Collection<String> dest){
		return splitToCollection(list, DEFAULT_SEPARATOR, dest);
	}

}
