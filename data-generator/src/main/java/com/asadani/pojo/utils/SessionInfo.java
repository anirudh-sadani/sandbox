package com.asadani.pojo.utils;

public class SessionInfo {
	
	String userId;
	String authToken;
	
	public SessionInfo(String id, String token)
	{
		userId = id;
		authToken = token;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

}
