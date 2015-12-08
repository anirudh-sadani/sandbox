package com.asadani.ca.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
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
	
	public Map<byte[], byte[]> fetchData(String tableName, String rowkey, String columnFamily){
		
		Table table = null;
		try {
			table = manager.getConnection().getTable(TableName.valueOf(tableName));
			Get get = new Get(Bytes.toBytes(rowkey));
			get.addFamily(Bytes.toBytes(columnFamily));
			Result result = table.get(get);
			Map<byte[], byte[]> familyMap = result.getFamilyMap(Bytes.toBytes(columnFamily));
			return familyMap;
		} catch (IOException e) {
			
		}
		finally{
			if(table != null)
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
		}
		return null;
	}


	public Map<byte[], NavigableMap<byte[], byte[]>> fetchData(String tableName, String rowkey){
		
		Table table = null;
		try {
			table = manager.getConnection().getTable(TableName.valueOf(tableName));
			Get get = new Get(Bytes.toBytes(rowkey));
			Result result = table.get(get);
			Map<byte[], NavigableMap<byte[], byte[]>> familyMap = result.getNoVersionMap();
			
			return familyMap;
		} catch (IOException e) {
		}
		finally{
			if(table != null)
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
		}
		return null;
	}


	public List<Map<byte[], byte[]>> fetchData(String tableName, String columnFamily, String fromrowkey, String torowkey){
		Table table = null;
		List<Map<byte[], byte[]>> resultList = new ArrayList();
		try {
			table = manager.getConnection().getTable(TableName.valueOf(tableName));
			Scan scan = new Scan(Bytes.toBytes(fromrowkey), Bytes.toBytes(torowkey));
			scan.addFamily(Bytes.toBytes(columnFamily));
			ResultScanner scanner = table.getScanner(scan);
			for (Result result = scanner.next(); result != null; result = scanner.next()){
				NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(Bytes.toBytes(columnFamily));
				familyMap.put(Bytes.toBytes("rowkey"), result.getRow());
				resultList.add(familyMap);
			}
			return resultList;
		} catch (IOException e) {
			
		}
		finally{
			if(table != null)
				try {
					table.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		
		}return resultList;
	}
}
