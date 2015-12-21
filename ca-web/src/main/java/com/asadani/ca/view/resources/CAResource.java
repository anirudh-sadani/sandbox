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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.hadoop.hbase.shaded.org.apache.commons.math3.util.MultidimensionalCounter.Iterator;
import org.apache.hadoop.hbase.util.Bytes;

import com.asadani.ca.dao.PageHitsDAO;
import com.asadani.ca.dao.SessionAnalysisDAO;
import com.asadani.ca.dao.SpendingTrendsDAO;
import com.asadani.ca.dao.ZipHitsDAO;
import com.asadani.ca.hbase.HBaseConnectionManager;
import com.asadani.ca.hbase.HBaseQueryExecutor;


/**
 * 
 */
@Path("/data")
@Produces(MediaType.APPLICATION_JSON)
public class CAResource {
	
	private PageHitsDAO pageHitsDao;
	
	private SessionAnalysisDAO sessionAnalysisDao;
	
	private ZipHitsDAO zipHitsDAO;
	
	private SpendingTrendsDAO spendingTrendsDAO;
	
	public CAResource(){
		HBaseConnectionManager manager = new HBaseConnectionManager();
		HBaseQueryExecutor executor = new HBaseQueryExecutor(manager);
		this.pageHitsDao = new PageHitsDAO(executor);
		this.sessionAnalysisDao = new SessionAnalysisDAO(executor);
		this.zipHitsDAO = new ZipHitsDAO(executor);
		this.spendingTrendsDAO = new SpendingTrendsDAO(executor);
	}
	
	@GET
    @Path("most_visited_pages_by_users")
    public List<Map<String, String>>  most_visited_pages(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate){
    	
List<Map<byte[], byte[]>> result = pageHitsDao.getMostVisitedPagesByDay(startDate, endDate);
		
		java.util.Iterator<Map<byte[], byte[]>> iter = result.iterator();
		
		NavigableMap<byte[], byte[]> tempObject;

		List<Map<String, String>> finalResult = new ArrayList<Map<String, String>> ();
		
		while (iter.hasNext())
		{
			tempObject = (NavigableMap<byte[], byte[]>) iter.next();
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
    public List<Map<String, String>> hitsByDay(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate){
    	
		List<Map<byte[], byte[]>> result = pageHitsDao.getHitsByDay(startDate, endDate);
		
		java.util.Iterator<Map<byte[], byte[]>> iter = result.iterator();
		
		NavigableMap<byte[], byte[]> tempObject;
		
		List<Map<String, String>> finalResult = new ArrayList<Map<String, String>> ();
		
		while (iter.hasNext())
		{
			tempObject = (NavigableMap<byte[], byte[]>) iter.next();
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
    @Path("session_details")
public List<Map<String, String>> sessionDetails(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate){
    	
		List<Map<byte[], byte[]>> result = sessionAnalysisDao.getUserSessionAnalysisData(startDate, endDate);
		
		java.util.Iterator<Map<byte[], byte[]>> iter = result.iterator();
		
		NavigableMap<byte[], byte[]> tempObject;
		
		List<Map<String, String>> finalResult = new ArrayList<Map<String, String>> ();
		
		while (iter.hasNext())
		{
			tempObject = (NavigableMap<byte[], byte[]>) iter.next();
			Map<String, String> resultMap = new HashMap<String, String> (); 
						
			for(Entry<byte[], byte[]> entry : tempObject.entrySet()){
				resultMap.put(Bytes.toString(entry.getKey()), Bytes.toString(entry.getValue()) );
			}
			finalResult.add(resultMap);
		}
		
		//NavigableMap<byte[], byte[]>
		System.out.println(finalResult);
		return finalResult;
    }
	
	@GET
    @Path("hits_by_location")
public List<Map<String, String>> hitsbyLocationdetails(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate){
    	
		List<Map<byte[], byte[]>> result = zipHitsDAO.getHitsByZipDataData(startDate, endDate);
		
		java.util.Iterator<Map<byte[], byte[]>> iter = result.iterator();
		
		NavigableMap<byte[], byte[]> tempObject;
		
		List<Map<String, String>> finalResult = new ArrayList<Map<String, String>> ();
		
		while (iter.hasNext())
		{
			tempObject = (NavigableMap<byte[], byte[]>) iter.next();
			Map<String, String> resultMap = new HashMap<String, String> (); 
						
			for(Entry<byte[], byte[]> entry : tempObject.entrySet()){
				resultMap.put(Bytes.toString(entry.getKey()), Bytes.toString(entry.getValue()) );
			}
			finalResult.add(resultMap);
		}
		
		//NavigableMap<byte[], byte[]>
		System.out.println(finalResult);
		return finalResult;
    }
	
	@GET
    @Path("item_visits")
public List<Map<String, String>> itemVisits(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate){
    	
		List<Map<byte[], byte[]>> result = sessionAnalysisDao.getProductVisitsData(startDate, endDate);
		
		java.util.Iterator<Map<byte[], byte[]>> iter = result.iterator();
		
		NavigableMap<byte[], byte[]> tempObject;
		
		List<Map<String, String>> finalResult = new ArrayList<Map<String, String>> ();
		
		while (iter.hasNext())
		{
			tempObject = (NavigableMap<byte[], byte[]>) iter.next();
			Map<String, String> resultMap = new HashMap<String, String> (); 
						
			for(Entry<byte[], byte[]> entry : tempObject.entrySet()){
				resultMap.put(Bytes.toString(entry.getKey()), Bytes.toString(entry.getValue()) );
			}
			finalResult.add(resultMap);
		}
		
		//NavigableMap<byte[], byte[]>
		System.out.println(finalResult);
		return finalResult;
    }
	
	@GET
    @Path("spending_trends")
public List<Map<String, String>> spendingTrends(@QueryParam("startDate") String startDate, @QueryParam("endDate") String endDate){
    	
		List<Map<byte[], byte[]>> result = spendingTrendsDAO.getSpendingTrendsData(startDate, endDate);
		
		java.util.Iterator<Map<byte[], byte[]>> iter = result.iterator();
		
		NavigableMap<byte[], byte[]> tempObject;
		
		List<Map<String, String>> finalResult = new ArrayList<Map<String, String>> ();
		
		while (iter.hasNext())
		{
			tempObject = (NavigableMap<byte[], byte[]>) iter.next();
			Map<String, String> resultMap = new HashMap<String, String> (); 
						
			for(Entry<byte[], byte[]> entry : tempObject.entrySet()){
				resultMap.put(Bytes.toString(entry.getKey()), Bytes.toString(entry.getValue()) );
			}
			finalResult.add(resultMap);
		}
		
		//NavigableMap<byte[], byte[]>
		System.out.println(finalResult);
		return finalResult;
    }
	
	
	
public static void main (String args[])
{
	CAResource ca = new CAResource();
	
	ca.most_visited_pages("1-12-2015", "10-12-2015");
}
	
}
