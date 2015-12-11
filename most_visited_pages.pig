
FLAT_DATA = load '/home/asadani/rampup/output/days/*' USING PigStorage(',') AS (clientIPAddress:chararray, timestamp:chararray, zipcode:chararray, httpMethod:chararray, httpURL:chararray, userId:chararray, userAuthToken:chararray, userBrowser:chararray,  eventDate:chararray, eventMonth:chararray, eventYear:chararray);


FLAT_DATA_FILTERED = FILTER FLAT_DATA BY (httpURL matches '.*view.*');


FLAT_DATA_WITH_DATE = foreach FLAT_DATA_FILTERED generate clientIPAddress, timestamp, zipcode, httpMethod, httpURL, userId, userAuthToken, userBrowser, CONCAT(CONCAT(CONCAT((chararray)eventDate, '-'), (chararray)eventMonth, '-'), (chararray)eventYear) AS eventDateCon:chararray;

GROUPED_BY_DAY_PAGE = GROUP FLAT_DATA_WITH_DATE BY (httpURL,eventDateCon);

COUNT_HITS_PER_PAGE_BY_DAY = FOREACH GROUPED_BY_DAY_PAGE GENERATE group.httpURL as httpURL, group.eventDateCon as eventDateCon, COUNT(FLAT_DATA_WITH_DATE) as countForDay;

to_sort = GROUP COUNT_HITS_PER_PAGE_BY_DAY by (eventDateCon);

top3 = foreach to_sort {
        sorted = order COUNT_HITS_PER_PAGE_BY_DAY by countForDay desc;
        top    = limit sorted 5;
        generate group, flatten(top);
};

STORE top3 INTO '/home/asadani/rampup/output/pig_output/most_visited_pages' using PigStorage(',');

