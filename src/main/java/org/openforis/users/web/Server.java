package org.openforis.users.web;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

import spark.servlet.SparkApplication;

/**
 * 
 * @author R. Fontanarosa
 * @author S. Ricci
 *
 */
public class Server implements SparkApplication {

	@Override
	public void init() {

		staticFileLocation("/public");

		get("/hello/:name", Views.home);

	}

}
