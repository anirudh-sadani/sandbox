export BASE_CODEBASE_PATH=/home/IMPETUS/asadani/codebase/rampup/sandbox
export BASE_HDFS_PATH=/home/asadani/rampup
export BASE_INSTALLS_PATH=/home/IMPETUS/asadani/Installs
export JAVA_HOME=/usr
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin/lib/hive-*.jar
export PATH=/home/IMPETUS/asadani/Installs/pig-0.15.0/bin:/home/IMPETUS/asadani/Installs/hadoop-2.7.0/bin:/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin/bin:$PATH
export HADOOP_HOME=/home/IMPETUS/asadani/Installs/hadoop-2.7.0
export HIVE_HOME=/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin
mvn -f $BASE_CODEBASE_PATH/data-generator/pom.xml clean package
mvn -f $BASE_CODEBASE_PATH/data-flattener/pom.xml clean package
mvn -f $BASE_CODEBASE_PATH/ca-etl/pom.xml clean package
mvn -f $BASE_CODEBASE_PATH/ca-web/pom.xml clean package

echo $1

mvn -f $BASE_CODEBASE_PATH/data-generator/pom.xml exec:java -Dexec.args=$2

$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -rmr -skipTrash /home/asadani/rampup/output*
$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -rmr -skipTrash /home/asadani/rampup/hive/eventData/*
$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -rm -skipTrash /home/asadani/rampup/input/*



if [ $1 = "COMPLETE" ];
then
   	echo "Copying reference data files"

	$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -copyFromLocal $BASE_CODEBASE_PATH/prod_cat_ref_data.txt /home/asadani/rampup/input
	$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -copyFromLocal $BASE_CODEBASE_PATH/zipcode.csv /home/asadani/rampup/input

	echo "Creating Hive & HBase schema"
	
	$BASE_INSTALLS_PATH/hbase-1.1.2/bin/hbase shell /home/IMPETUS/asadani/codebase/rampup/sandbox/ca-etl/src/main/hbase/hbase_schema
	$BASE_INSTALLS_PATH/apache-hive-1.2.1-bin/bin/hive -f $BASE_CODEBASE_PATH/ca-etl/src/main/hive/hive_schema.hql
fi

$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -copyFromLocal $BASE_CODEBASE_PATH/user-traffic.log /home/asadani/rampup/input

$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hadoop jar $BASE_CODEBASE_PATH/data-flattener/target/data-flattener-0.0.1-SNAPSHOT.jar com.asadani.flattener.JSONDataFlattener $BASE_HDFS_PATH/input/user-traffic.log $BASE_HDFS_PATH/output/



$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/daily_hits.pig
$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/item_visits.pig
$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/most_visited_pages.pig
$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/user_session_analysis.pig

dateCounter=31

while [ $dateCounter -ge 1 ]
do
   echo $dateCounter
	queryString="LOAD DATA INPATH '/home/asadani/rampup/output/days/*/$dateCounter*' OVERWRITE INTO TABLE event_data PARTITION (eventDate=$dateCounter);"
	$BASE_INSTALLS_PATH/apache-hive-1.2.1-bin/bin/hive -e "$queryString"
   dateCounter=`expr $dateCounter - 1`

done

$BASE_INSTALLS_PATH/apache-hive-1.2.1-bin/bin/hive -f $BASE_CODEBASE_PATH/ca-etl/src/main/hive/spending_trends.hql
$BASE_INSTALLS_PATH/apache-hive-1.2.1-bin/bin/hive -f $BASE_CODEBASE_PATH/ca-etl/src/main/hive/zip_hits.hql

$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/zip_hits_count.pig
$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/spending_trends.pig

export prefixDate=`date +%Y%m%d`

$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -mkdir /home/asadani/rampup/archive/mroutput/$prefixDate

$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -mkdir /home/asadani/rampup/archive/eventData/$prefixDate

$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -mv /home/asadani/rampup/output/* /home/asadani/rampup/archive/mroutput/$prefixDate

$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -mv /home/asadani/rampup/hive/eventData/* /home/asadani/rampup/archive/eventData/$prefixDate

java -jar $BASE_CODEBASE_PATH/ca-web/target/ca-web-0.0.1-SNAPSHOT.jar server $BASE_CODEBASE_PATH/ca-web/src/main/resources/ca.yaml
