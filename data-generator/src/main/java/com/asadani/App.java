package com.asadani;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.asadani.pojo.JSONDataPojo;

/**
 * Hello world!
 *
 */

public class App implements Callable<List<String>> {

	final static Logger logger = Logger.getLogger(App.class);
	@Override
	public List call() throws Exception {
		
		List<String> temp = new ArrayList<String>();
		JSONDataPojo jsd = new JSONDataPojo();
		
		int i = 0;
		while(i<10)
		{
			jsd.populateModel();
			temp.add(jsd.toString());
			logger.debug(jsd.toString());
			Thread.sleep(200);
			i++;
		}
		
		return temp;
	}

	public static void main(String args[]) {
		
		ExecutorService executor = Executors.newFixedThreadPool(200);
		
		List<Future<List<String>>> list = new ArrayList<Future<List<String>>>();
		Callable<List<String>> callable = new App();
		
		for (int i = 0; i < 1000; i++) {
			
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
		
		System.out.println("Done..!");
		
		executor.shutdown();		
	}
	
	
}
