package com.asadani.ca.hbase;

public class HBaseConstants {

	public static final String HBASE_MASTER_CONF_PROP = "hbase.master";
	public static final String HBASE_ZK_QUORUM_CONF_PROP = "hbase.zookeeper.quorum";
	public static final String HBASE_ZK_CLIENT_PORT_CONF_PROP = "hbase.zookeeper.property.clientPort";
	
	public static final String TABLE_PAGE_HITS_BY_USER = "MOST_VISITED_PAGES_BY_USERS";
	
	public static final String TABLE_USER_SESSION_DETAILS = "USER_SESSION_DETAILS";
	public static final String TABLE_HITS_BY_LOCATION = "HITS_BY_LOCATION";
	public static final String TABLE_DAILY_UPDATES = "DAILY_UPDATES";
	public static final String TABLE_SPENDING_TRENDS = "SPENDING_TRENDS";
	
	public static final String CF_SESSION_ANALYSIS = "Output_Session_Analysis";
	public static final String CF_PRODUCT_VISITS = "Output_Product_Visits";
	public static final String CF_MOST_VISITED = "Output_Most_Visited_Pages";
	public static final String CF_DAILY_HITS = "Output_Hits_By_Day";
	public static final String CF_HITS_BY_ZIP = "Output_Hits_By_Zip";
	public static final String CF_SPENDING_TRENDS = "Output_Spending_Trends";	
}