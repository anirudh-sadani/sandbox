FLAT_DATA = load '/home/asadani/rampup/output/days/*' USING PigStorage(',') AS (clientIPAddress:chararray, timestamp:chararray, zipcode:chararray, httpMethod:chararray, httpURL:chararray, userId:chararray, userAuthToken:chararray, userBrowser:chararray,  eventDate:chararray, eventMonth:chararray, eventYear:chararray);


FLAT_DATA_FILTERED = FILTER FLAT_DATA BY (httpURL matches '.*view.*');


FLAT_DATA_WITH_DATE = foreach FLAT_DATA_FILTERED generate clientIPAddress, timestamp, zipcode, httpMethod, httpURL, userId, userAuthToken, userBrowser, CONCAT(CONCAT(CONCAT((chararray)eventYear, '-'), (chararray)eventMonth, '-'), (chararray)eventDate) AS eventDateCon:chararray;

GROUPED_BY_DAY_PAGE = GROUP FLAT_DATA_WITH_DATE BY (httpURL,eventDateCon);

COUNT_HITS_PER_PAGE_BY_DAY = FOREACH GROUPED_BY_DAY_PAGE GENERATE group.httpURL as httpURL, group.eventDateCon as eventDateCon, COUNT(FLAT_DATA_WITH_DATE) as countForDay;

OUTPUT_DATA_TO_SORT = GROUP COUNT_HITS_PER_PAGE_BY_DAY by (eventDateCon);

TOP_FIVE_FOR_STORAGE = FOREACH OUTPUT_DATA_TO_SORT {
        SORTED = ORDER COUNT_HITS_PER_PAGE_BY_DAY by countForDay desc;
        TOP    = LIMIT SORTED 5;
        GENERATE group, FLATTEN(TOP);
};


TOP_FIVE_FORHBASE_STORAGE = FOREACH TOP_FIVE_FOR_STORAGE GENERATE group, TOMAP(httpURL, countForDay) ;

STORE TOP_FIVE_FORHBASE_STORAGE INTO 'hbase://DAILY_UPDATES' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage ('Output_Most_Visited_Pages:*');
