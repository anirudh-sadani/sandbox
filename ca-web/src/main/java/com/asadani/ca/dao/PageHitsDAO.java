package com.asadani.ca.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asadani.ca.hbase.HBaseConnectionManager;
import com.asadani.ca.hbase.HBaseQueryExecutor;

public class PageHitsDAO extends AbstractDAO {
	
	public PageHitsDAO(HBaseQueryExecutor executor) {
		super(executor);
		//this.setHbaseTable(HBaseConstants.TABLE_PAGE_HITS_BY_USER);
		this.setHbaseTable("MOST_VISITED_PAGES");
	}

	public List<Map<byte[], byte[]>> getMostVisitedPagesByDay(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithFilter(this.getHbaseTable(), "outputCF", startDate, endDate);
		
		return tempMap;
	}
	
	public List<Map<byte[], byte[]>> getHitsByDay(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithFilter("HITS_BY_DAY", "output", startDate, endDate);
		
		return tempMap;
	}
	
	public static void main (String args[])
	{
		HBaseConnectionManager manager = new HBaseConnectionManager();
		HBaseQueryExecutor executor1 = new HBaseQueryExecutor(manager);
		PageHitsDAO pdo = new PageHitsDAO(executor1);
		
		//pdo.getMostVisitedPagesByDay();
	}
}
