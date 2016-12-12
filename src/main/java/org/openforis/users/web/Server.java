package org.openforis.users.web;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.delete;
import static spark.Spark.staticFileLocation;

import java.math.BigInteger;
import java.util.Map;

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

	private JsonTransformer jsonTransformer;

	@Override
	public void init() {

		jsonTransformer = new JsonTransformer();

		staticFileLocation("/public");

		get("/hello/:name", Views.home);
		
		get("/users/all", listAllUsers, new JsonTransformer());
		
		post("/users/", addUser, new JsonTransformer());
		
		delete("/users/:id", deleteUser, new JsonTransformer());

	}
	
	private Route listAllUsers = (Request req, Response rsp) -> {
		return EntityManagerFactory.getUserManager().listAll();
	};

	private Route addUser = (Request req, Response rsp) -> {
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		String username = bodyMap.get("username").toString();
		String password = bodyMap.get("password").toString();

		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		
		EntityManagerFactory.getUserManager().save(user);
		return user;
	};
	
	private Route deleteUser = (Request req, Response rsp) -> {
		String idStr = req.params("id");
		BigInteger id = new BigInteger(idStr);
		return EntityManagerFactory.getUserManager().deleteById(id);
	};
		
}
