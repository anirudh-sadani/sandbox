package com.asadani.ca.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asadani.ca.hbase.HBaseConnectionManager;
import com.asadani.ca.hbase.HBaseQueryExecutor;

public class ZipHitsDAO extends AbstractDAO {
	
	public ZipHitsDAO(HBaseQueryExecutor executor) {
		super(executor);
		this.setHbaseTable("HITS_BY_LOCATION");
	}

	public List<Map<byte[], byte[]>> getHitsByZipDataData(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithFilter(this.getHbaseTable(), "Output_Hits_By_Zip", startDate, endDate);
		
		return tempMap;
	}
	
	
	public static void main (String args[])
	{
		HBaseConnectionManager manager = new HBaseConnectionManager();
		HBaseQueryExecutor executor1 = new HBaseQueryExecutor(manager);
		ZipHitsDAO pdo = new ZipHitsDAO(executor1);
		
		pdo.getHitsByZipDataData("7-11-2015","21-11-2015");
	}
}
