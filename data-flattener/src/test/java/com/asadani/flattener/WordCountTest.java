package com.asadani.flattener;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import com.asadani.flattener.WordCount.CountReducer;
import com.asadani.flattener.WordCount.WordMapper;

public class WordCountTest {
	  MapDriver<Object, Text, Text, IntWritable> mapDriver;
	  ReduceDriver<Text,IntWritable,Text,IntWritable> reduceDriver;
	  MapReduceDriver<Object, Text, Text, IntWritable, Text, IntWritable> mapReduceDriver;
	 
	  @Before
	  public void setUp() {
		  WordMapper mapper = new WordMapper();
		  CountReducer reducer = new CountReducer();
	    mapDriver = MapDriver.newMapDriver(mapper);
	    reduceDriver = ReduceDriver.newReduceDriver(reducer);
	    mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
	  }
	 
	  @Test
	  public void testMapper() {
	    mapDriver.withInput(new LongWritable(), new Text(
	        "This is cool is"));
	    

	    
	    mapDriver.withOutput(new Text("This"), new IntWritable(1));
	    
	    mapDriver.withOutput(new Text("is"), new IntWritable(1));
	    
	    mapDriver.withOutput(new Text("cool"), new IntWritable(1));
	    
	    mapDriver.withOutput(new Text("is"), new IntWritable(1));
	    
	    mapDriver.runTest();
	  }
	 
	  @Test
	  public void testReducer() {
	    List<IntWritable> values = new ArrayList<IntWritable>();
	    values.add(new IntWritable(1));
	    
	    
	    reduceDriver.withInput(new Text("This"), values);
	    
	    reduceDriver.withOutput(new Text("This"), new IntWritable(1));
	    
	    reduceDriver.runTest();
	    
	  }
	   
	  @Test
	  public void testMapReduce() {
	    mapReduceDriver.withInput(new LongWritable(), new Text(
	              "This is a a"));
	    List<IntWritable> values = new ArrayList<IntWritable>();
	    values.add(new IntWritable(1));
	    mapReduceDriver.withOutput(new Text("This"), new IntWritable(1));
	    mapReduceDriver.withOutput(new Text("a"), new IntWritable(2));

	    mapReduceDriver.withOutput(new Text("is"), new IntWritable(1));
	    
	    mapReduceDriver.runTest();
	  }
	}
