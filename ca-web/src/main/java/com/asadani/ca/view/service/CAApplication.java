package com.asadani.ca.view.service;

import com.asadani.ca.view.resources.CAResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * The Class CAApplication.
 */
public class CAApplication extends Application<CAConfiguration>{
	
	/* (non-Javadoc)
	 * @see io.dropwizard.Application#initialize(io.dropwizard.setup.Bootstrap)
	 */
	@Override
	public void initialize(Bootstrap<CAConfiguration> bootstrap) {
		super.initialize(bootstrap);
		bootstrap.addBundle(new AssetsBundle("/assets/", "/"));
	}
	
	/* (non-Javadoc)
	 * @see io.dropwizard.Application#run(io.dropwizard.Configuration, io.dropwizard.setup.Environment)
	 */
	@Override
	public void run(CAConfiguration config, Environment env)	throws Exception {
		env.jersey().setUrlPattern("/ca/*");
		final CAResource resource = new CAResource();
		env.jersey().register(resource);
	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		new CAApplication().run(args);
	}

	/* (non-Javadoc)
	 * @see io.dropwizard.Application#getName()
	 */
	@Override
	public String getName() {
		return "Click-Analytics";
	}
}
