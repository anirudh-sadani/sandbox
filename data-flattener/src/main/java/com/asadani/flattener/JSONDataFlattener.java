package com.asadani.flattener;

import java.io.IOException;
import java.util.Calendar;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
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
				
				long timeInMillis = Long.parseLong((String)eventData.get("timestamp"));
				
				Calendar temp = Calendar.getInstance();
				temp.setTimeInMillis(timeInMillis);
				
				sb.append(eventData.get("originatingIPAddress"));
				sb.append(",");
				sb.append(timeInMillis);
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
				sb.append(",");
				sb.append(eventData.get("userBrowser"));
				sb.append(",");
				sb.append(temp.get(Calendar.DATE));
				sb.append(",");
				sb.append(temp.get(Calendar.MONTH) + 1);
				sb.append(",");
				sb.append(temp.get(Calendar.YEAR));

				
				context.write(new Text(sb.toString()),  NullWritable.get());
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	 public static class DayWisePartitioner extends Partitioner<Text, NullWritable> {
		 
	        @Override
	        public int getPartition(Text key, NullWritable value, int numReduceTasks) {
	 
	        	String[] valuesStr = key.toString().split(",");
				
				Calendar time = Calendar.getInstance();
				
				time.setTimeInMillis(Long.parseLong(valuesStr[1]));
				
				return (time.get(Calendar.DATE)-1);
	           
	        }
	    }

	
	public static class MultipleOutputsReducer extends Reducer<Text, NullWritable ,NullWritable, Text> 
	{			
		private MultipleOutputs<NullWritable,Text> multipleOutputs;
		
		public void setup(Context context) throws IOException, InterruptedException
		{
			multipleOutputs = new MultipleOutputs<NullWritable, Text>(context);
		}
		
		public void reduce(Text rkey, Iterable<NullWritable> rvalue, Context context) throws IOException, InterruptedException
		{
			String[] valuesStr = rkey.toString().split(",");
			
			Calendar time = Calendar.getInstance();
			
			time.setTimeInMillis(Long.parseLong(valuesStr[1]));
			
			multipleOutputs.write(NullWritable.get(), rkey,  ("days/" +(time.get(Calendar.MONTH) + 1 )+ "/"+ time.get(Calendar.DATE)+ "_" + Calendar.getInstance().get(Calendar.MONTH) + + Calendar.getInstance().get(Calendar.DATE)).toString());		
			context.write(NullWritable.get(), rkey);
					           
		}	
	
		public void cleanup(Context context) throws IOException, InterruptedException
		{
		      multipleOutputs.close();
		}		
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();

		Job job = Job.getInstance(conf, "Data flattener");
		
		job.setJarByClass(JSONDataFlattener.class);
		job.setMapperClass(JSONDataFlattenerMapper.class);
		// job.setCombinerClass(CountReducer.class);
		job.setReducerClass(MultipleOutputsReducer.class);
		
		job.setPartitionerClass(DayWisePartitioner.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		//job.setOutputFormatClass(TextOutputFormat.class);  
		job.setNumReduceTasks(31);
		//job.getConfiguration().set("mapred.child.java.opts","-Xmx256m");
		//job.getConfiguration().setInt("mapreduce.map.memory.mb",512);
		//job.getConfiguration().setInt("mapreduce.reduce.memory.mb",64);
		//job.getConfiguration().set("mapreduce.map.java.opts","-Xmx1024m");
		//job.getConfiguration().set("mapreduce.reduce.java.opts","-Xmx64m");

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
