package com.asadani.ca.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;

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
	
	public List<Map<byte[], byte[]>> fetchDataWithFilter(String tableName, String columnFamily, String fromRowKeyPattern, String toRowKeyPattern){
		Table table = null;
		List<Map<byte[], byte[]>> resultList = new ArrayList();
		try {
			table = manager.getConnection().getTable(TableName.valueOf(tableName));
			

			Scan scan = new Scan();
			scan.setFilter(getIndividualFiltersforRange(fromRowKeyPattern, toRowKeyPattern));
			
			scan.addFamily(Bytes.toBytes(columnFamily));
			ResultScanner scanner = table.getScanner(scan);
			for (Result result = scanner.next(); result != null; result = scanner.next()){
				NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(Bytes.toBytes(columnFamily));
				
				String rowKeyData = Bytes.toString(result.getRow());
				
				familyMap.put(Bytes.toBytes("rowkey"), rowKeyData.getBytes());
				
				if(rowKeyData.indexOf("_") != -1)
					familyMap.put(Bytes.toBytes("datekey"), rowKeyData.substring(0, rowKeyData.indexOf("_")).getBytes());

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

	private FilterList getIndividualFiltersforRange(String fromRowKeyPattern,
			String toRowKeyPattern) {
		
		FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);

		String[] startDate = fromRowKeyPattern.split("-");
		
		int startDay = Integer.parseInt(startDate[0]);
		int startMonth = Integer.parseInt(startDate[1]);
		int startYear = Integer.parseInt(startDate[2]);
		
		String[] endDate = toRowKeyPattern.split("-");
		
		int endDay = Integer.parseInt(endDate[0]);
		int endMonth = Integer.parseInt(endDate[1]);
		int endYear = Integer.parseInt(endDate[2]);
		
	    Calendar startCalendar = Calendar.getInstance();
	    
	    startCalendar.set(startYear, startMonth - 1, startDay);
	    
	    Calendar endCalendar = Calendar.getInstance();
	    
	    endCalendar.set(endYear, endMonth - 1, endDay);
	    System.out.println("start " + startCalendar.getTime());
	    System.out.println("end " + endCalendar.getTime());

	    long numberOfDays = (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis())/(1000*60*60*24);
		
	    System.out.println("numberOfDays " + numberOfDays);
	    
	    for(long i = 1; i <= numberOfDays; i++)
	    {
	    	String dateFilterString = 	startCalendar.get(Calendar.YEAR) + "-" +
	    								(startCalendar.get(Calendar.MONTH) + 1) + "-" +
	    								startCalendar.get(Calendar.DAY_OF_MONTH);
	    	
	    	filterList.addFilter( new PrefixFilter(dateFilterString.getBytes()));
	    	
	    	startCalendar.add(Calendar.DAY_OF_MONTH, 1);
	    	
	    	System.out.println("i " + i);
	    }
	    
		return filterList;
	}
}
