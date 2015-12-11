package com.asadani.ca.dataload;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

public class MostVisitedPagesUploader {

		public static class MostVisitedPagesUploaderMapper extends Mapper<Object, Text, Text, Text>{

			@Override
			protected void map(Object key, Text value, Mapper<Object, Text, Text, Text>.Context context)
					throws IOException, InterruptedException {
				String[] values = value.toString().split(",");
				
				context.write(new Text(values[0]), value);
			}
		}
		public static class MostVisitedPagesUploaderReducer extends TableReducer<Text, Text, ImmutableBytesWritable> {

			@Override
			protected void reduce(Text key, Iterable<Text> values,
					Reducer<Text, Text, ImmutableBytesWritable, Mutation>.Context context)
							throws IOException, InterruptedException {
				Put put = new Put(Bytes.toBytes(key.toString()));
				for(Text value : values){
					String[] fieldvalues = value.toString().split(",");
					put.add(Bytes.toBytes("outputCF"), Bytes.toBytes(fieldvalues[1]), Bytes.toBytes(fieldvalues[3]));
				}
				context.write(null, put);
			}
			
		}
		
		public static void main(String[] args) throws Exception {
			Configuration  config  = HBaseConfiguration.create();
			config.clear();
			
	        config.set("hbase.zookeeper.quorum", "127.0.0.1");
	        config.set("hbase.zookeeper.property.clientPort","2181");
	        config.set("hbase.master", "127.0.0.1:16000");
			
	        Job job = Job.getInstance(config, "Click Analytics HBase Uploader");
			job.setJarByClass(MostVisitedPagesUploader.class);
			
			
			job.setMapperClass(MostVisitedPagesUploaderMapper.class);
			TableMapReduceUtil.initTableReducerJob(
					"MOST_VISITED_PAGES",
					MostVisitedPagesUploaderReducer.class,
					job);
			
			
			job.setNumReduceTasks(4);
			FileInputFormat.addInputPath(job, new Path("/home/asadani/rampup/output/pig_output/most_visited_pages"));
			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);
			System.exit(job.waitForCompletion(true) ? 0 : 1);
		}
	}