package com.asadani.flattener;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JSONDataFlattener {

	public static class JSONDataFlattenerMapper extends
			Mapper<Object, Text, Text, NullWritable> {

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			StringBuffer sb = new StringBuffer();
			
			try {
				JSONObject eventData = new JSONObject(value.toString());
				JSONObject httpData = eventData.getJSONObject("httpData");
				
				
				
				sb.append(eventData.get("originatingIPAddress"));
				sb.append(",");
				sb.append(eventData.get("timestamp"));
				sb.append(",");
				sb.append(eventData.get("zipcode"));
				sb.append(",");
				sb.append(httpData.get("httpMethod"));
				sb.append(",");
				sb.append(httpData.get("httpUrl"));
				sb.append(",");
				sb.append(httpData.get("headerParamUserId"));
				sb.append(",");
				sb.append(httpData.get("headerParamAuthToken"));

				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			context.write(new Text(sb.toString()), null);
		}
	}

	public static class JSONDataFlattenerReducer extends
			Reducer<Text, NullWritable, Text, NullWritable> {

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {

			context.write(key, null);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf, "Data flattener");
		job.setJarByClass(JSONDataFlattener.class);
		job.setMapperClass(JSONDataFlattenerMapper.class);
		// job.setCombinerClass(CountReducer.class);
		job.setReducerClass(JSONDataFlattenerReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
