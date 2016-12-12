package org.openforis.users.web;

import static spark.Spark.get;
import static spark.Spark.staticFileLocation;

import org.openforis.users.manager.EntityManagerFactory;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

/**
 * 
 * @author R. Fontanarosa
 * @author S. Ricci
 *
 */
public class Server implements SparkApplication {

	private static final String JSON_CONTENT_TYPE = "application/json";

	@Override
	public void init() {

		staticFileLocation("/public");

		get("/hello/:name", Views.home);
		
		get("/users/all", JSON_CONTENT_TYPE, listAllUsers, new JsonTransformer());
		
		

	}
	
	private Route listAllUsers = (Request req, Response rsp) -> {
		return EntityManagerFactory.getUserManager().listAll();
	};

}
