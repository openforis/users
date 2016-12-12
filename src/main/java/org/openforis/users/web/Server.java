package org.openforis.users.web;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.model.User;

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
		
		get("/users/all", listAllUsers, new JsonTransformer());
		
		post("/users/", addUser, new JsonTransformer());

	}
	
	private Route listAllUsers = (Request req, Response rsp) -> {
		return EntityManagerFactory.getUserManager().listAll();
	};

	private Route addUser = (Request req, Response rsp) -> {
		String username = req.queryParams("username");
		String password = req.queryParams("password");
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		EntityManagerFactory.getUserManager().save(user);
		return user;
	};
}
