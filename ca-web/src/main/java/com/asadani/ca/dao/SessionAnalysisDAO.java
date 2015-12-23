package com.asadani.ca.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asadani.ca.hbase.HBaseConstants;
import com.asadani.ca.hbase.HBaseQueryExecutor;

public class SessionAnalysisDAO extends AbstractDAO {
	
	public SessionAnalysisDAO(HBaseQueryExecutor executor) {
		super(executor);
		this.setHbaseTable(HBaseConstants.TABLE_USER_SESSION_DETAILS);
	}

	public List<Map<byte[], byte[]>> getUserSessionAnalysisData(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithPrefixFilter(this.getHbaseTable(), HBaseConstants.CF_SESSION_ANALYSIS, startDate, endDate);
		
		return tempMap;
	}
	public List<Map<byte[], byte[]>> getProductVisitsData(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithPrefixFilter(this.getHbaseTable(), HBaseConstants.CF_PRODUCT_VISITS, startDate, endDate);
		
		return tempMap;
	}	
	
	public List<Map<byte[], byte[]>> getSessionDataForUser(String emailId)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithSubstringFilter(this.getHbaseTable(), HBaseConstants.CF_SESSION_ANALYSIS, emailId);
		
		return tempMap;
	}
	
	public List<Map<byte[], byte[]>> getItemVisitsForUser(String emailId)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithSubstringFilter(this.getHbaseTable(), HBaseConstants.CF_PRODUCT_VISITS, emailId);
		
		return tempMap;
	}
}