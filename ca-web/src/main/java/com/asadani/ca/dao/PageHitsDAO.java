package com.asadani.ca.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asadani.ca.hbase.HBaseConstants;
import com.asadani.ca.hbase.HBaseQueryExecutor;

public class PageHitsDAO extends AbstractDAO {
	
	public PageHitsDAO(HBaseQueryExecutor executor) {
		super(executor);
		this.setHbaseTable(HBaseConstants.TABLE_DAILY_UPDATES);
	}

	public List<Map<byte[], byte[]>> getMostVisitedPagesByDay(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithPrefixFilter(this.getHbaseTable(), HBaseConstants.CF_MOST_VISITED, startDate, endDate);
		
		return tempMap;
	}
	
	public List<Map<byte[], byte[]>> getHitsByDay(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithPrefixFilter(this.getHbaseTable(), HBaseConstants.CF_DAILY_HITS, startDate, endDate);
		
		return tempMap;
	}
}