package com.asadani.ca.view.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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

	public CAResource() {
		HBaseConnectionManager manager = new HBaseConnectionManager();
		HBaseQueryExecutor executor = new HBaseQueryExecutor(manager);
		this.pageHitsDao = new PageHitsDAO(executor);
		this.sessionAnalysisDao = new SessionAnalysisDAO(executor);
		this.zipHitsDAO = new ZipHitsDAO(executor);
		this.spendingTrendsDAO = new SpendingTrendsDAO(executor);
	}

	@GET
	@Path("most_visited_pages_by_users")
	public List<Map<String, String>> most_visited_pages(
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {

		return transformData(pageHitsDao.getMostVisitedPagesByDay(startDate,
				endDate));

	}

	@GET
	@Path("hits_by_day")
	public List<Map<String, String>> hitsByDay(
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {

		return transformData(pageHitsDao.getHitsByDay(startDate, endDate));
	}

	@GET
	@Path("session_details")
	public List<Map<String, String>> sessionDetails(
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {

		return transformData(sessionAnalysisDao.getUserSessionAnalysisData(
				startDate, endDate));

	}

	@GET
	@Path("hits_by_location")
	public List<Map<String, String>> hitsbyLocationdetails(
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {

		return transformData(zipHitsDAO
				.getHitsByZipDataData(startDate, endDate));
	}

	@GET
	@Path("item_visits")
	public List<Map<String, String>> itemVisits(
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {

		return transformData(sessionAnalysisDao.getProductVisitsData(startDate,
				endDate));
	}

	@GET
	@Path("spending_trends")
	public List<Map<String, String>> spendingTrends(
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) {

		return transformData(spendingTrendsDAO.getSpendingTrendsData(startDate,
				endDate));
	}
	
	@GET
	@Path("item_visits_by_user")
	public List<Map<String, String>> itemVisitsByUser(
			@QueryParam("emailId") String emailId) {

		return transformData(sessionAnalysisDao.getItemVisitsForUser(emailId));
	}
	
	@GET
	@Path("session_details_by_user")
	public List<Map<String, String>> sessionDetailsByUser(
			@QueryParam("emailId") String emailId) {

		return transformData(sessionAnalysisDao.getSessionDataForUser(emailId));
	}

	private List<Map<String, String>> transformData(
			List<Map<byte[], byte[]>> result) {
		java.util.Iterator<Map<byte[], byte[]>> iter = result.iterator();

		NavigableMap<byte[], byte[]> tempObject;

		List<Map<String, String>> finalResult = new ArrayList<Map<String, String>>();

		while (iter.hasNext()) {
			tempObject = (NavigableMap<byte[], byte[]>) iter.next();
			Map<String, String> resultMap = new HashMap<String, String>();

			for (Entry<byte[], byte[]> entry : tempObject.entrySet()) {
				resultMap.put(Bytes.toString(entry.getKey()),
						Bytes.toString(entry.getValue()));
			}
			finalResult.add(resultMap);
		}
		return finalResult;
	}
	
	public static void main(String args[])
	{
		CAResource ca = new CAResource();
		
		System.out.println(ca.sessionDetailsByUser("rob@somemail.com"));
	}
}