export BASE_CODEBASE_PATH=/home/IMPETUS/asadani/codebase/rampup/sandbox
export BASE_HDFS_PATH=/home/asadani/rampup
export BASE_INSTALLS_PATH=/home/IMPETUS/asadani/Installs
export JAVA_HOME=/usr
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin/lib/hive-*.jar
export PATH=/home/IMPETUS/asadani/Installs/pig-0.15.0/bin:/home/IMPETUS/asadani/Installs/hadoop-2.7.0/bin:/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin/bin:$PATH
export HADOOP_HOME=/home/IMPETUS/asadani/Installs/hadoop-2.7.0
export HIVE_HOME=/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin


dateCounter=16

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

$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -mkdir /home/asadani/rampup/archive/eventData/$prefixDate

$BASE_INSTALLS_PATH/hadoop-2.7.0/bin/hdfs dfs -mv /home/asadani/rampup/hive/eventData/* /home/asadani/rampup/archive/eventData/$prefixDate

java -jar $BASE_CODEBASE_PATH/ca-web/target/ca-web-0.0.1-SNAPSHOT.jar server $BASE_CODEBASE_PATH/ca-web/src/main/resources/ca.yaml
