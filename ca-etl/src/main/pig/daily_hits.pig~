FLAT_DATA = load '/home/asadani/rampup/output/days/*' USING PigStorage(',') AS (clientIPAddress:chararray, timestamp:chararray, zipcode:chararray, httpMethod:chararray, httpURL:chararray, userId:chararray, userAuthToken:chararray, userBrowser:chararray,  eventDate:chararray, eventMonth:chararray, eventYear:chararray);

FLAT_DATA_WITH_DATE = foreach FLAT_DATA generate clientIPAddress, timestamp, zipcode, httpMethod, httpURL, userId, userAuthToken, userBrowser, CONCAT(CONCAT(CONCAT((chararray)eventYear, '-'), (chararray)eventMonth, '-'), (chararray)eventDate) AS eventDateCon:chararray;

GROUPED_BY_DAY = GROUP FLAT_DATA_WITH_DATE BY eventDateCon;

COUNT_HITS_BY_DAY = FOREACH GROUPED_BY_DAY GENERATE group as myVar, COUNT(FLAT_DATA_WITH_DATE) as countForDay;

COUNT_HITS_BY_DAY_SUMMARY = FOREACH COUNT_HITS_BY_DAY GENERATE (chararray)myVar as day, (int)countForDay as count;

STORE COUNT_HITS_BY_DAY_SUMMARY INTO 'hbase://DAILY_UPDATES' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage ('Output_Hits_By_Day:count');
