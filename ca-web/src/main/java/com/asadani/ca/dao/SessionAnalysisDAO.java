package com.asadani.ca.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asadani.ca.hbase.HBaseConnectionManager;
import com.asadani.ca.hbase.HBaseQueryExecutor;

public class SessionAnalysisDAO extends AbstractDAO {
	
	public SessionAnalysisDAO(HBaseQueryExecutor executor) {
		super(executor);
		this.setHbaseTable("USER_SESSION_DETAILS");
	}

	public List<Map<byte[], byte[]>> getUserSessionAnalysisData(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithFilter(this.getHbaseTable(), "Output_Session_Analysis", startDate, endDate);
		
		return tempMap;
	}
	public List<Map<byte[], byte[]>> getProductVisitsData(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithFilter(this.getHbaseTable(), "Output_Product_Visits", startDate, endDate);
		
		return tempMap;
	}
	
	public static void main (String args[])
	{
		HBaseConnectionManager manager = new HBaseConnectionManager();
		HBaseQueryExecutor executor1 = new HBaseQueryExecutor(manager);
		SessionAnalysisDAO pdo = new SessionAnalysisDAO(executor1);
		
		//pdo.getUserSessionAnalysisData();
	}
}
