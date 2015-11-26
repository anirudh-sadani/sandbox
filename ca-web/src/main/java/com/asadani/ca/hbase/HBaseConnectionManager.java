package com.asadani.ca.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

/**
 * The Class HBaseConfigManager.
 */
public class HBaseConnectionManager {

	/** The configuration object. */
	private Configuration conf;

	public Configuration getConf() {
		return conf;
	}


	public void setConf(Configuration conf) {
		this.conf = conf;
	}


	/**
	 * Initializes a configuration object with provided properties.
	 */
	public HBaseConnectionManager(){
		try {
			conf  = HBaseConfiguration.create();
			conf.clear();
			conf.set(HBaseConstants.HBASE_MASTER_CONF_PROP, "127.0.0.1:16000");
			conf.set(HBaseConstants.HBASE_ZK_QUORUM_CONF_PROP, "127.0.0.1");
			conf.set(HBaseConstants.HBASE_ZK_CLIENT_PORT_CONF_PROP,"2181");

		}
		catch(Exception e){
			throw new RuntimeException("Not able to initialize Connection Manager's configuration object", e);
		}
	}

	
	/**
	 * Establish connection.
	 *
	 * @return connection to query
	 * @throws IOException 
	 */
	public Connection getConnection() throws IOException {
		return ConnectionFactory.createConnection(conf);
	}

}