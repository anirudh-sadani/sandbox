package com.asadani.ca.dao;

import java.util.HashMap;
import java.util.Map;

import com.asadani.ca.hbase.HBaseConnectionManager;
import com.asadani.ca.hbase.HBaseConstants;
import com.asadani.ca.hbase.HBaseQueryExecutor;

public class PageHitsDAO extends AbstractDAO {
	
	public PageHitsDAO(HBaseQueryExecutor executor) {
		super(executor);
		//this.setHbaseTable(HBaseConstants.TABLE_PAGE_HITS_BY_USER);
		this.setHbaseTable("pig_table");
	}

	public Map getMostVisitedPagesByDay()
	{
		
		return this.getExecutor().fetchData(this.getHbaseTable());
	}
	
	public static void main (String args[])
	{
		HBaseConnectionManager manager = new HBaseConnectionManager();
		HBaseQueryExecutor executor1 = new HBaseQueryExecutor(manager);
		PageHitsDAO pdo = new PageHitsDAO(executor1);
		
		pdo.getMostVisitedPagesByDay();
	}
}
