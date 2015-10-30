package com.asadani.pojo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.asadani.pojo.utils.URLPattern;

public class HTTPDataPojo {

	String baseURL = "/shoppe/";
	
	String [] productActions = {"addToCart", "addToWishList", "buy", "removeFromCart"};
	
	String [] searchStrings = {"mobile", "Mi4i", "microwave", "sports shoes"};
	
	
	String httpMethod;
	String headerParamUserId;
	String headerParamAuthToken;
	String httpUrl;
	
	public HTTPDataPojo(String id, String token)
	{
		headerParamUserId = id;
		headerParamAuthToken = token;
	}
	
	static List<URLPattern> urls;
	
	static
	{
		urls = new ArrayList<URLPattern>();
		BufferedReader br = null;

		try {

			String sCurrentLine;
			br = new BufferedReader(new FileReader("/home/IMPETUS/asadani/codebase/rampup/sandbox/data-generator/src/main/resources/urlPatterns.csv"));
			while ((sCurrentLine = br.readLine()) != null) {
				String[] urlDetails = sCurrentLine.split(",");
				urls.add(new URLPattern(urlDetails[0], urlDetails[1]));
				
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
		
	}
	
	public void populateModel() {
		URLPattern up = urls.get(ThreadLocalRandom.current().nextInt(0, urls.size()));
		this.httpMethod = up.getHttpMethod();
		this.httpUrl = up.getRequestURL();
		//this.headerParamAuthToken = "asdf";
		//this.headerParamUserId= "example";

	}
	
	public String toString()
	{
		return "{" + 
				"httpMethod:'" + httpMethod + "', " +
				"httpUrl:'" + httpUrl + "', " +
				"headerParamAuthToken:'" + headerParamAuthToken+ "', " +
				"headerParamUserId:'" + headerParamUserId +
				"'}";
	}

}
