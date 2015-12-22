export BASE_CODEBASE_PATH=/home/IMPETUS/asadani/codebase/rampup/sandbox

export BASE_HDFS_PATH=/home/asadani/rampup

export BASE_INSTALLS_PATH=/home/IMPETUS/asadani/Installs

export JAVA_HOME=/usr

export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin/lib/hive-*.jar

export PATH=/home/IMPETUS/asadani/Installs/pig-0.15.0/bin:/home/IMPETUS/asadani/Installs/hadoop-2.7.0/bin:/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin/bin:$PATH

export HADOOP_HOME=/home/IMPETUS/asadani/Installs/hadoop-2.7.0

export HIVE_HOME=/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin


$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/daily_hits.pig
$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/item_visits.pig
$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/most_visited_pages.pig
$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/user_session_analysis.pig

$BASE_INSTALLS_PATH/apache-hive-1.2.1-bin/bin/hive -i $BASE_CODEBASE_PATH/ca-etl/src/main/hive/hive_schema.hql
$BASE_INSTALLS_PATH/apache-hive-1.2.1-bin/bin/hive -i $BASE_CODEBASE_PATH/ca-etl/src/main/hive/hive_dataload.hql
$BASE_INSTALLS_PATH/apache-hive-1.2.1-bin/bin/hive -i $BASE_CODEBASE_PATH/ca-etl/src/main/hive/spending_trends.hql
$BASE_INSTALLS_PATH/apache-hive-1.2.1-bin/bin/hive -i $BASE_CODEBASE_PATH/ca-etl/src/main/hive/zip_hits.hql

$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/zip_hits_count.pig
$BASE_INSTALLS_PATH/pig-0.15.0/bin/pig $BASE_CODEBASE_PATH/ca-etl/src/main/pig/spending_trends.pig











