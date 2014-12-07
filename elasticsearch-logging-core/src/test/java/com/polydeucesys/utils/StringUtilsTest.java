package com.polydeucesys.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testSplit() {
		String t1 = "";
		String t2 = "bob ";
		String t3 = "bob, jane, john  , emily";
		String t4 = "a;b;c;";
		Collection<String> s1 = new HashSet<String>();
		s1 = StringUtils.splitToCollection(t1, s1);
		assertArrayEquals(new String[]{}, s1.toArray());
		Collection<String> s2 = new ArrayList<String>();
		s2 = StringUtils.splitToCollection(t2, s2);
		assertArrayEquals(new String[]{"bob"}, s2.toArray());
		Collection<String> s3 = new ArrayList<String>();
		s3 = StringUtils.splitToCollection(t3, s3);
		assertArrayEquals(new String[]{"bob", "jane", "john", "emily"}, s3.toArray());
		Collection<String> s4 = new ArrayList<String>();
		s4 = StringUtils.splitToCollection(t4,";", s4);
		assertArrayEquals(new String[]{"a", "b", "c"}, s4.toArray());
	}
	
	@Test
	public void testJoin(){
		String[] l1 = new String[]{};
		String[] l2 = new String[] {"a", "b", "c"};
		assertEquals("", StringUtils.join(l1, ";"));
		assertEquals("", StringUtils.join(Arrays.asList(l1)));
		assertEquals("a and b and c", StringUtils.join(l2, " and "));
		assertEquals("a and b and c", StringUtils.join(Arrays.asList(l2), " and "));
	}

}
