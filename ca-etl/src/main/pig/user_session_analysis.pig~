REGISTER /home/IMPETUS/asadani/codebase/rampup/sandbox/ca-etl/target/ca-etl-0.0.1-SNAPSHOT.jar;

FLAT_DATA = load '/home/asadani/rampup/output/days/*' USING PigStorage(',') AS (clientIPAddress:chararray, timestamp:chararray, zipcode:chararray, httpMethod:chararray, httpURL:chararray, userId:chararray, userAuthToken:chararray, userBrowser:chararray,  eventDate:chararray, eventMonth:chararray, eventYear:chararray);

FLAT_DATA_WITH_DATE = foreach FLAT_DATA generate clientIPAddress, timestamp, zipcode, httpMethod, httpURL, userId, userAuthToken, userBrowser, CONCAT(CONCAT(CONCAT((chararray)eventYear, '-'), (chararray)eventMonth, '-'), (chararray)eventDate) AS eventDateCon:chararray;

FLAT_DATA_WITH_DATE_ORDERED = ORDER FLAT_DATA_WITH_DATE BY timestamp ASC;

GROUPED_BY_USER_AUTH_TOKEN = GROUP FLAT_DATA_WITH_DATE_ORDERED BY (userAuthToken, eventDateCon);

GROUPED_BY_USER_AUTH_TOKEN_SORTED = FOREACH GROUPED_BY_USER_AUTH_TOKEN {
SORTED_EVENTS = ORDER FLAT_DATA_WITH_DATE_ORDERED BY timestamp;
GENERATE SORTED_EVENTS;
};

SESSION_DETAILS = FOREACH GROUPED_BY_USER_AUTH_TOKEN_SORTED GENERATE com.asadani.ca.pig.udf.SessionAnalyzer(SORTED_EVENTS) AS session;

FLAT_SESSION_DETAILS = FOREACH SESSION_DETAILS GENERATE session.rowkeyForOp as rowKey, session.refreshEventCount as refreshCnt, session.backEventCount as backCnt, session.sessionDuration as duration;

STORE FLAT_SESSION_DETAILS INTO 'hbase://USER_SESSION_DETAILS' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage ('Output_Session_Analysis:refreshCnt, Output_Session_Analysis:backCnt, Output_Session_Analysis:duration');
