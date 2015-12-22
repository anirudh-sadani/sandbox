export BASE_CODEBASE_PATH=/home/IMPETUS/asadani/codebase/rampup/sandbox
export BASE_HDFS_PATH=/home/asadani/rampup
export BASE_INSTALLS_PATH=/home/IMPETUS/asadani/Installs
export JAVA_HOME=/usr
export HADOOP_CLASSPATH=$HADOOP_CLASSPATH:/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin/lib/hive-*.jar
export PATH=/home/IMPETUS/asadani/Installs/pig-0.15.0/bin:/home/IMPETUS/asadani/Installs/hadoop-2.7.0/bin:/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin/bin:$PATH
export HADOOP_HOME=/home/IMPETUS/asadani/Installs/hadoop-2.7.0
export HIVE_HOME=/home/IMPETUS/asadani/Installs/apache-hive-1.2.1-bin


$BASE_INSTALLS_PATH/hbase-1.1.2/bin/hbase shell $BASE_CODEBASE_PATH/ca-etl/src/main/hbase/hbase_cleanup
$BASE_INSTALLS_PATH/apache-hive-1.2.1-bin/bin/hive -f $BASE_CODEBASE_PATH/ca-etl/src/main/hive/hive_cleanup.hql













