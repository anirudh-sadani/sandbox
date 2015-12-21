FLAT_DATA_FROM_HIVE_FOR_ZIP = load '/home/asadani/rampup/hive/hitsByZip/*' USING PigStorage(',') AS (zipcode:chararray, eventDateCon:chararray, hitCount:int, zipcodeAnother:chararray, city:chararray, state:chararray, latitude:chararray, longitude:chararray);

FLAT_DATA_FROM_HIVE_FOR_ZIP_FOR_STORAGE = FOREACH FLAT_DATA_FROM_HIVE_FOR_ZIP GENERATE 
CONCAT(CONCAT((chararray)eventDateCon, '_'),zipcode ) AS DATA_ROWKEY, hitCount, city, state, latitude, longitude;


STORE FLAT_DATA_FROM_HIVE_FOR_ZIP_FOR_STORAGE INTO 'hbase://HITS_BY_LOCATION' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage ('Output_Hits_By_Zip:hitCount, Output_Hits_By_Zip:city, Output_Hits_By_Zip:state, Output_Hits_By_Zip:latitude, Output_Hits_By_Zip:longitude');

