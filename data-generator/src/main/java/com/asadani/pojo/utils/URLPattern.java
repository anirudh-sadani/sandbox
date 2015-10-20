package com.asadani.pojo.utils;

public class URLPattern {
	
	String httpMethod;
	String requestURL;
	
	public URLPattern(String method, String url)
	{
		httpMethod = method;
		requestURL = url;
	}
	
	public String getHttpMethod() {
		return httpMethod;
	}
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}
	public String getRequestURL() {
		return requestURL;
	}
	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

}
