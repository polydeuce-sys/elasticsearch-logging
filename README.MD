# ElasticSearch Log Appenders
The elasticsearch-logging project provides Appender implementations for common logging frameworks which provides 
direct submission of logs to ElasticSearch via the Jest HTTP Rest client. It is intended as a drop in facility to help
enable construction of ElasticSearch based monitoring tools for Java applications. 

## Installation

At present the project only available as srouce via GitHub. The project consists of a parent pom and four sub modules.
The main module is the elasticsearch-logging-core which provides the common classes and interfaces used to provide the
implementations of the logging framework specific appenders.


## Usage

### Log4J 1.2.x
The simplest usage is to configure the BaseElasticsearchJestAppender in your log4j configuration. The minimal configuration 
simply specifies the connection string property to something like "http://myhost:9200". This will create an appender instance which
will write to Elasticsearch in blocks of 100 lines (or after 2 seconds is 100 lines have not been submitted).

### Logback
As per log4j, a BaseElasticsearchJestAppender can be configured to write logs directly to Elasticsearch.

### Log4j 2.x
Again, a BaseElasticsearchJestAppender is provided which will log directly to Elasticsearch.