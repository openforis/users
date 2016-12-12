package org.openforis.users.web;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.delete;
import static spark.Spark.staticFileLocation;

import java.math.BigInteger;
import java.io.File;
import java.net.URL;
import java.util.Map;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.model.User;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * 
 * @author R. Fontanarosa
 * @author S. Ricci
 *
 */
public class Server implements SparkApplication {

	private static final String JSON_CONTENT_TYPE = "application/json";

	private JsonTransformer jsonTransformer;

	private static FreeMarkerEngine getTemplateRenderer() throws Exception {
		Configuration cfg = new Configuration();
		URL templateDirectory = Server.class.getResource("/template/freemarker");
		cfg.setDirectoryForTemplateLoading(new File(templateDirectory.toURI()));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		// cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		//cfg.setLogTemplateExceptions(false);
		return new FreeMarkerEngine(cfg);
	}

	@Override
	public void init() {

		jsonTransformer = new JsonTransformer();

		FreeMarkerEngine renderer = null;
		try {
			renderer = getTemplateRenderer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		staticFileLocation("/public");

		get("/hello/:name", Views.home);

		get("/hello2/:name", Views.home2, renderer);
		
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
