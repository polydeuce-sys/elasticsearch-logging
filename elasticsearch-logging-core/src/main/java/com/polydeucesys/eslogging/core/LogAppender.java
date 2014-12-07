package com.polydeucesys.eslogging.core;

public interface LogAppender<L, D, R> {
	void setName();
	String getName();
	Connection<D, R> getConnection();
	void setLogSubmissionStrategy(final LogSubmissionStrategy<L, D, R> strategy);
	LogSubmissionStrategy<L, D, R> getLogSubmissionStrategy();	
}