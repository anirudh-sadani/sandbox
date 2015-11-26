package com.asadani.ca.hbase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseQueryExecutor {
	
	HBaseConnectionManager manager;
	
	public HBaseQueryExecutor(HBaseConnectionManager manager)
	{
		this.manager = manager;
	}

	public Map fetchData(String tablename)
	{
		 Table table = null;
		try {
			table = manager.getConnection().getTable(TableName.valueOf(tablename));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      // Instantiating Get class
	      Get g = new Get(Bytes.toBytes("28-9-2015"));

	      // Reading the data
	      Result result=null;
		try {
			result = table.get(g);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	      // Reading values from Result class object
	      byte [] value = result.getValue(Bytes.toBytes("cf"),Bytes.toBytes("count"));

	      System.out.println(value.toString());
	      System.out.println(value);
	      System.out.println(Bytes.toString(value));
	      //byte [] value1 = result.getValue(Bytes.toBytes("personal"),Bytes.toBytes("city"));

		return new HashMap();
	}
	
}
