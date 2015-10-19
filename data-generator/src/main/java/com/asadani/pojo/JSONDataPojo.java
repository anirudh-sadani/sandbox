package com.asadani.pojo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

import com.asadani.pojo.utils.ZipRange;

public class JSONDataPojo {
	
	String zipcode;
	String originatingIPAddress;
	HTTPDataPojo httpData;
	long timestamp;
	
	List<ZipRange> validZipRanges;
	
	public JSONDataPojo()
	{
		
		validZipRanges = populateZipRanges();
		httpData = new HTTPDataPojo();
		System.out.println(validZipRanges);
	}
	
	public List<ZipRange> populateZipRanges()
	{
		List<ZipRange> populatedRanges = new ArrayList<ZipRange>();
		
		BufferedReader br = null;

		try {

			String sCurrentLine;

			br = new BufferedReader(new FileReader("/home/IMPETUS/asadani/codebase/rampup/sandbox/data-generator/src/main/resources/zipranges.txt"));

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				
				StringTokenizer str = new StringTokenizer(sCurrentLine, "-");
				//System.out.println(str.nextToken());
				//System.out.println(str.nextToken());
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
			
		return populatedRanges;
	}
	
	public JSONDataPojo populateModel()
	{
		JSONDataPojo model = new JSONDataPojo();
		
		model.populateZipcode();
		
		model.populateIPAddress();
		model.populateHTTPData();		
		model.populateTimestamp();
		
		return model;		
	}
	

	private void populateTimestamp() {
		setTimestamp(Calendar.getInstance().getTimeInMillis());
		
	}


	private void populateHTTPData() {
		this.httpData.populateModel();
		
	}


	private void populateIPAddress() {
		setOriginatingIPAddress("192.168.64.61");
		
	}

	private void populateZipcode() {
		Random randomNumberGenerator = new Random(validZipRanges.size());
		ZipRange zipRange = validZipRanges.get(ThreadLocalRandom.current().nextInt(0, validZipRanges.size() + 1));
		
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
	
	public static void main(String args[])
	{
		JSONDataPojo jsd = new JSONDataPojo();
		jsd.populateZipRanges();
		jsd = jsd.populateModel();
		System.out.println(jsd);
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
