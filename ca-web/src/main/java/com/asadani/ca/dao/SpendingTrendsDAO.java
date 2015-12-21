package com.asadani.ca.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asadani.ca.hbase.HBaseConnectionManager;
import com.asadani.ca.hbase.HBaseQueryExecutor;

public class SpendingTrendsDAO extends AbstractDAO {
	
	public SpendingTrendsDAO(HBaseQueryExecutor executor) {
		super(executor);
		this.setHbaseTable("SPENDING_TRENDS");
	}

	public List<Map<byte[], byte[]>> getSpendingTrendsData(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithFilter(this.getHbaseTable(), "Output_Spending_Trends", startDate, endDate);
		
		return tempMap;
	}
	
	
	public static void main (String args[])
	{
		HBaseConnectionManager manager = new HBaseConnectionManager();
		HBaseQueryExecutor executor1 = new HBaseQueryExecutor(manager);
		SpendingTrendsDAO pdo = new SpendingTrendsDAO(executor1);
		
		pdo.getSpendingTrendsData("7-11-2015","21-11-2015");
	}
}
