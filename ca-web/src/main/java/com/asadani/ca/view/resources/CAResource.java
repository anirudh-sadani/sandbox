package com.asadani.ca.view.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.hadoop.hbase.shaded.org.apache.commons.math3.util.MultidimensionalCounter.Iterator;
import org.apache.hadoop.hbase.util.Bytes;

import com.asadani.ca.dao.PageHitsDAO;
import com.asadani.ca.hbase.HBaseConnectionManager;
import com.asadani.ca.hbase.HBaseQueryExecutor;


/**
 * 
 */
@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
public class CAResource {
	
	private PageHitsDAO pageHitsDao;
	
	
	public CAResource(){
		HBaseConnectionManager manager = new HBaseConnectionManager();
		HBaseQueryExecutor executor = new HBaseQueryExecutor(manager);
		this.pageHitsDao = new PageHitsDAO(executor);
	}
	
	@GET
    @Path("most_visited_pages_by_users")
    public List<Map<String, String>>  most_visited_pages(){
    	
List<Map<byte[], byte[]>> result = pageHitsDao.getMostVisitedPagesByDay();
		
		java.util.Iterator<Map<byte[], byte[]>> iter = result.iterator();
		
		NavigableMap<byte[], byte[]> tempObject;
		
		List<Map<String, String>> finalResult = new ArrayList<Map<String, String>> ();
		
		while (iter.hasNext())
		{
			tempObject = (NavigableMap<byte[], byte[]>) iter.next();
			System.out.println(tempObject);
			Map<String, String> resultMap = new HashMap<String, String> (); 
						
			for(Entry<byte[], byte[]> entry : tempObject.entrySet()){
				resultMap.put(Bytes.toString(entry.getKey()), Bytes.toString(entry.getValue()) );
			}
			finalResult.add(resultMap);
			
			System.out.println(finalResult);
			
			
		}
		
		//NavigableMap<byte[], byte[]>
		
		return finalResult;
    }
	
	@GET
    @Path("hits_by_day")
    public List<Map<String, String>> hits_by_day(){
    	
		List<Map<byte[], byte[]>> result = pageHitsDao.getHitsByDay();
		
		java.util.Iterator<Map<byte[], byte[]>> iter = result.iterator();
		
		NavigableMap<byte[], byte[]> tempObject;
		
		List<Map<String, String>> finalResult = new ArrayList<Map<String, String>> ();
		
		while (iter.hasNext())
		{
			tempObject = (NavigableMap<byte[], byte[]>) iter.next();
			System.out.println(tempObject);
			Map<String, String> resultMap = new HashMap<String, String> (); 
						
			for(Entry<byte[], byte[]> entry : tempObject.entrySet()){
				resultMap.put(Bytes.toString(entry.getKey()), Bytes.toString(entry.getValue()) );
			}
			finalResult.add(resultMap);
			
			System.out.println(finalResult);
			
			
		}
		
		//NavigableMap<byte[], byte[]>
		
		return finalResult;
    }
	
}
