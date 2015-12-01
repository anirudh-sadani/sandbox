package com.asadani.ca.view.resources;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
    public Map<String, Long> most_visited_pages(){
    	return new HashMap<String, Long> ();
    }
	
}
