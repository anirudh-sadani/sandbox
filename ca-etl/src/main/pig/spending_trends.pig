FLAT_DATA_FROM_HIVE = load '/home/asadani/rampup/hive/spendingtrends/*' USING PigStorage(',') AS (eventDateCon:chararray, productId:chararray, productName:chararray, categoryName:chararray, price:int, categoryId:chararray, zipcode:chararray, userId:chararray,  userAuthToken:chararray, pid:chararray);

FLAT_DATA_FROM_HIVE_FOR_STORAGE = FOREACH FLAT_DATA_FROM_HIVE GENERATE 
CONCAT(CONCAT(CONCAT(CONCAT(CONCAT(CONCAT((chararray)eventDateCon, '_'),userId ), '_'), userAuthToken), '_'), pid) AS DATA_ROWKEY,
productId, productName, categoryName, price, categoryId, zipcode;


STORE FLAT_DATA_FROM_HIVE_FOR_STORAGE INTO 'hbase://SPENDING_TRENDS' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage ('Output_Spending_Trends:productId, Output_Spending_Trends:productName, Output_Spending_Trends:categoryName, Output_Spending_Trends:price, Output_Spending_Trends:categoryId, Output_Spending_Trends:zipcode');

