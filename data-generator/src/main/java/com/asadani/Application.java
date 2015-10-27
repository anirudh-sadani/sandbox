package com.asadani;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;

import com.asadani.pojo.JSONDataPojo;

/**
 * Hello world!
 *
 */

public class Application implements Callable<List<String>> {

	final static Logger logger = Logger.getLogger(Application.class);
	
	int days;
	Calendar startDate;
	
	public Application(Calendar start)
	{
		startDate = start;
				
	}
	@Override
	public List call() throws Exception {
		
		List<String> temp = new ArrayList<String>();
		JSONDataPojo jsd = new JSONDataPojo();
		
		int i = 0;
		int max = ThreadLocalRandom.current().nextInt(12, 50);
		while(i<max)
		{
			jsd.populateModel(startDate);
			temp.add(jsd.toString());
			logger.debug(jsd.toString());
			Thread.sleep(50);
			i++;
		}
		
		return temp;
	}

	public static void main(String args[]) {
		

		ExecutorService executor = Executors.newFixedThreadPool(50);
		
		List<Future<List<String>>> list = new ArrayList<Future<List<String>>>();
		
		Calendar cal = Calendar.getInstance();
		
		cal.set(2015, 8, 22);
		
		long days = (Calendar.getInstance().getTimeInMillis() - cal.getTimeInMillis())/(1000*60*60*24);
		
		for(int j = 1; j<= days; j++ )
		{
			Callable<List<String>> callable = new Application(cal);
			
			for (int i = 0; i < 300; i++) {
				
				Future<List<String>> future = executor.submit(callable);
				
				list.add(future);
			}
			
			for (Future<List<String>> fu : list)
			{
				try {
					fu.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("Done for : " + cal.getTime());
			
			cal.set(2015, 8, 22);
			
			cal.add(Calendar.DATE, j);
		}
		
		System.out.println("Done..!");
		
		executor.shutdown();		
	}
	
	
}
