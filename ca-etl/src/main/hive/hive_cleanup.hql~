CREATE EXTERNAL TABLE event_data
(
clientIPAddress STRING, 
eventTimestamp BIGINT,
zipcode STRING, 
httpMethod STRING,
httpURL STRING,
userId STRING,
userAuthToken STRING,
userBrowser STRING,
dummy int,
eventMonth int,
eventYear int
)
PARTITIONED BY(eventDate int)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
LOCATION '/home/asadani/rampup/hive/eventData';


CREATE EXTERNAL TABLE prod_cat_ref
(
productId STRING, 
categoryId STRING,
productName STRING,
categoryName STRING,
price int
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
LOCATION '/home/asadani/rampup/hive/productRefData';

LOAD DATA INPATH '/home/asadani/rampup/input/prod_cat_ref_data.txt' OVERWRITE INTO TABLE prod_cat_ref;

CREATE EXTERNAL TABLE zip_lat_long_ref
(
zipcode STRING, 
city STRING,
state STRING,
latitude STRING,
longitude STRING
)
ROW FORMAT DELIMITED FIELDS TERMINATED BY ','
LOCATION '/home/asadani/rampup/hive/zipLatLongRefData';

LOAD DATA INPATH '/home/asadani/rampup/input/zipcode.csv' OVERWRITE INTO TABLE zip_lat_long_ref;
