package com.asadani.ca.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asadani.ca.hbase.HBaseConstants;
import com.asadani.ca.hbase.HBaseQueryExecutor;

public class ZipHitsDAO extends AbstractDAO {
	
	public ZipHitsDAO(HBaseQueryExecutor executor) {
		super(executor);
		this.setHbaseTable(HBaseConstants.TABLE_HITS_BY_LOCATION);
	}

	public List<Map<byte[], byte[]>> getHitsByZipDataData(String startDate, String endDate)
	{
		List<Map<byte[], byte[]>> tempMap = new ArrayList<Map<byte[], byte[]>> (); 
		
		tempMap=  this.getExecutor().fetchDataWithPrefixFilter(this.getHbaseTable(), HBaseConstants.CF_HITS_BY_ZIP, startDate, endDate);
		
		return tempMap;
	}
}