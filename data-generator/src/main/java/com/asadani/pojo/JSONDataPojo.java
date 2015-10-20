package com.asadani.pojo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

import com.asadani.pojo.utils.ZipRange;

public class JSONDataPojo {
	
	String zipcode;
	String originatingIPAddress;
	HTTPDataPojo httpData;
	long timestamp;
	
	static List<ZipRange> validZipRanges;
	
	public JSONDataPojo()
	{
		
		
		setOriginatingIPAddress
		(
			ThreadLocalRandom.current().nextInt(0, 256) + "." +
					ThreadLocalRandom.current().nextInt(0, 256) + "." +
					ThreadLocalRandom.current().nextInt(0, 256) + "." +
					ThreadLocalRandom.current().nextInt(0, 256)
		);
		populateZipcode();
		httpData = new HTTPDataPojo();
		
	}
	
	static 
	{
		List<ZipRange> populatedRanges = new ArrayList<ZipRange>();
		
		BufferedReader br = null;

		try {

			String sCurrentLine;
			br = new BufferedReader(new FileReader("/home/IMPETUS/asadani/codebase/rampup/sandbox/data-generator/src/main/resources/zipranges.txt"));
			while ((sCurrentLine = br.readLine()) != null) {
				StringTokenizer str = new StringTokenizer(sCurrentLine, "-");
				populatedRanges.add(new ZipRange(Integer.parseInt(str.nextToken()), Integer.parseInt(str.nextToken())));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
			
		validZipRanges = populatedRanges;
	}
	
	public void populateModel()
	{
		populateHTTPData();		
		populateTimestamp();
		
	}
	

	private void populateTimestamp() {
		setTimestamp(Calendar.getInstance().getTimeInMillis());
		
	}


	private void populateHTTPData() {
		this.httpData.populateModel();
		
	}

	private void populateZipcode() {
		ZipRange zipRange = validZipRanges.get(ThreadLocalRandom.current().nextInt(0, validZipRanges.size()));
		
		setZipcode(zipRange.getStartZip() + ThreadLocalRandom.current().nextInt(0, zipRange.getDifference() + 1) + "");
	}


	public String getZipcode() {
		return zipcode;
	}
	
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	
	public String getOriginatingIPAddress() {
		return originatingIPAddress;
	}
	
	public void setOriginatingIPAddress(String originatingIPAddress) {
		this.originatingIPAddress = originatingIPAddress;
	}
	
	public HTTPDataPojo getHttpData() {
		return httpData;
	}
	
	public void setHttpData(HTTPDataPojo httpData) {
		this.httpData = httpData;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}	
	
	
	public String toString()
	{
		return "{" + 
					"originatingIPAddress:" + originatingIPAddress + ", " +
					"timestamp:" + timestamp + ", " +
					"httpData:" + httpData.toString() + ", " +
					"zipcode:" + zipcode +
					"}";
	}

}
