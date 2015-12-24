package com.asadani.pojo.utils;

public class ZipRange {

	int startZip;
	public int getStartZip() {
		return startZip;
	}

	public int getEndZip() {
		return endZip;
	}

	public int getDifference() {
		return difference;
	}



	int endZip;
	int difference;
	
	public ZipRange()
	{
		startZip = 0;
		endZip = 0;
		difference = -1;
	}
	
	public ZipRange(int start, int end)
	{
		startZip = start;
		endZip = end;
		difference = end-start;
	}
	
	
	
	public String toString()
	{
		return "{" + startZip + ", " + endZip + "}";
	}
}
