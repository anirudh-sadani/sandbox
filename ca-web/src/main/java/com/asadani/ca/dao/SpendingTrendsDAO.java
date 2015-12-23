package com.asadani.ca.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asadani.ca.hbase.HBaseConstants;
import com.asadani.ca.hbase.HBaseQueryExecutor;

public class SpendingTrendsDAO extends AbstractDAO {
	
	public SpendingTrendsDAO(HBaseQueryExecutor executor) {
		super(executor);
		this.setHbaseTable(HBaseConstants.TABLE_SPENDING_TRENDS);
	}

	public List<Map<byte[], byte[]>> getSpendingTrendsData(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithPrefixFilter(this.getHbaseTable(), HBaseConstants.CF_SPENDING_TRENDS, startDate, endDate);
		
		return tempMap;
	}
}