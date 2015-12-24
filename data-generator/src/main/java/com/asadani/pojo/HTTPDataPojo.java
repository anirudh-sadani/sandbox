package com.asadani.pojo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	
	public void populateModel(Map historyEventMap) {
		
		
		URLPattern up = urls.get(ThreadLocalRandom.current().nextInt(0, urls.size()));
		this.httpMethod = up.getHttpMethod();
		String generatedURL = formAbsoluteURLFromPattern(up.getRequestURL());
		boolean allowed = false;
		
		this.httpUrl = "NULL";

		String product="";

		//System.out.println("generatedURL " + generatedURL);
		if(generatedURL.indexOf("view/product") != -1)
		{
			product=generatedURL.substring(13);
			//System.out.println("product " + product);
			((ArrayList)historyEventMap.get("viewed")).add(product);
			allowed=true;
		}
		else if(generatedURL.indexOf("?action") != -1)
		{
			product = generatedURL.substring(13, generatedURL.indexOf("?"));
			//System.out.println("else product " + product);
		}			
		else 
			allowed=true;
		
		if(!allowed)
		{
			if(generatedURL.indexOf("buy") != -1)
			{
				//System.out.println("in buy" + product);
				ArrayList temp = (ArrayList)historyEventMap.get("cart");
				if(temp.size() >= 2)
				{
					product = (String)
								(temp).get(ThreadLocalRandom.current().nextInt(1, temp.size()));
					generatedURL = "data/product/" + product+ "?action=buy";
					allowed = true;
					//System.out.println("allowed " + allowed);
					if (allowed)
						((ArrayList)historyEventMap.get("purchased")).add(product);
				}
				else
					allowed = false;
			}
			else if(generatedURL.indexOf("addToCart") != -1)
			{
				//System.out.println("in addToCart" + product);

				allowed = ((ArrayList)historyEventMap.get("viewed")).contains(product);
				//System.out.println("allowed " + allowed);
				if (allowed)
					((ArrayList)historyEventMap.get("cart")).add(product);
			}
			else
				allowed=true;
		}		
		if (allowed)
			this.httpUrl = generatedURL;
	}
	
	private String formAbsoluteURLFromPattern(String requestURL) {
		String url = requestURL;
		
		if(url.contains("[categoryId]"))
			url = url.replace("[categoryId]", "C-" + ThreadLocalRandom.current().nextInt(0, 100));
		if(url.contains("[productId]"))
			url = url.replace("[productId]", "P-" + ThreadLocalRandom.current().nextInt(0, 50));
		if(url.contains("[productIds]"))
			url = url.replace("[productIds]", "P-" + ThreadLocalRandom.current().nextInt(0, 50));
		if(url.contains("[searchString]"))
			url = url.replace("[searchString]", searchStrings[ThreadLocalRandom.current().nextInt(0, searchStrings.length-1)]);
		if(url.contains("[action]"))
			url = url.replace("[action]", productActions[ThreadLocalRandom.current().nextInt(0, productActions.length-1)]);
		if(url.contains("[userId]"))
			url = url.replace("[userId]", this.headerParamUserId);
		return url;
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
