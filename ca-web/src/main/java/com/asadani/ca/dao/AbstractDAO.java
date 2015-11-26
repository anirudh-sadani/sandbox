package com.asadani.ca.dao;

import com.asadani.ca.hbase.HBaseQueryExecutor;



public class AbstractDAO {
	
	private HBaseQueryExecutor executor;
	
	private String hbaseTable;
	
	public AbstractDAO(HBaseQueryExecutor executor) {
		this.executor = executor;
	}

	public HBaseQueryExecutor getExecutor() {
		return executor;
	}

	public void setManager(HBaseQueryExecutor executor) {
		this.executor = executor;
	}

	public String getHbaseTable() {
		return hbaseTable;
	}

	public void setHbaseTable(String hbaseTable) {
		this.hbaseTable = hbaseTable;
	}
	
	
}
