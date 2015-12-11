FLAT_DATA = load '/home/asadani/rampup/output/days/*' USING PigStorage(',') AS (clientIPAddress:chararray, timestamp:chararray, zipcode:chararray, httpMethod:chararray, httpURL:chararray, userId:chararray, userAuthToken:chararray, userBrowser:chararray,  eventDate:chararray, eventMonth:chararray, eventYear:chararray);

FLAT_DATA_WITH_DATE = foreach FLAT_DATA generate clientIPAddress, timestamp, zipcode, httpMethod, httpURL, userId, userAuthToken, userBrowser, CONCAT(CONCAT(CONCAT((chararray)eventDate, '-'), (chararray)eventMonth, '-'), (chararray)eventYear) AS eventDateCon:chararray;

GROUPED_BY_USER_AUTH_TOKEN = GROUP FLAT_DATA_WITH_DATE BY (userAuthToken, userId, eventDateCon);

USER_BEHAVIOR = FOREACH GROUPED_BY_USER_AUTH_TOKEN {
SORTED_EVENTS = ORDER FLAT_DATA_WITH_DATE BY timestamp;
CART_DETAILS = FILTER SORTED_EVENTS BY httpURL matches '.*addToCart.*';
PURCHASED_ITEMS = FILTER SORTED_EVENTS BY httpURL matches '.*buy.*';
WISHLIST_ITEMS = FILTER SORTED_EVENTS BY httpURL matches '.*addToWishList.*';
GENERATE
CONCAT(CONCAT(CONCAT(CONCAT((chararray)group.eventDateCon, '_'),group.userId ), '_'), group.userAuthToken) AS DATA_ROWKEY,
COUNT(CART_DETAILS) as CART_ITEMS, 
COUNT(WISHLIST_ITEMS) as WISHLIST, 
COUNT(PURCHASED_ITEMS) as PURCHASED; 
};



STORE USER_BEHAVIOR INTO 'hbase://USER_BEHAVIOR' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage ('Output:CART_ITEMS, Output:WISHLIST, Output:PURCHASED');
