package org.openforis.users.web;

import static spark.Spark.staticFileLocation;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.patch;
import static spark.Spark.delete;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.UserManager;
import org.openforis.users.model.User;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
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

	private static final UserManager USER_MANAGER = EntityManagerFactory.getInstance().getUserManager();

	private JsonTransformer jsonTransformer;

	private static FreeMarkerEngine getTemplateRenderer() throws Exception {
		Configuration cfg = new Configuration();
		URL templateDirectory = Server.class.getResource("/template/freemarker");
		cfg.setDirectoryForTemplateLoading(new File(templateDirectory.toURI()));
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
		// cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		// cfg.setLogTemplateExceptions(false);
		return new FreeMarkerEngine(cfg);
	}

	private static void enebleCORS() {
		Spark.options("/*", (request, response) -> {
			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}
			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}
			return "OK";
		});
		Spark.before((request, response) -> {
			response.header("Access-Control-Allow-Origin", "*");
		});
	}

	@Override
	public void init() {

		Server.enebleCORS();

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

		// USER
		get("/user", findUsers, new JsonTransformer());
		post("/user", JSON_CONTENT_TYPE, addUser, new JsonTransformer());
		patch("/user", JSON_CONTENT_TYPE, editUser, new JsonTransformer());
		delete("/user/:id", JSON_CONTENT_TYPE, deleteUser, new JsonTransformer());

	}

	private Route findUsers = (Request req, Response rsp) -> {
		String username = req.queryParams("username");
		if (username == null) {
			return USER_MANAGER.findAll();
		} else {
			User user = USER_MANAGER.findByUsername(username);
			if (user == null) {
				return Collections.emptyList();
			} else {
				return Arrays.asList(user);
			}
		}
	};

	private Route addUser = (Request req, Response rsp) -> {
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		String username = bodyMap.get("username").toString();
		String password = bodyMap.get("password").toString();
		//
		User user = new User();
		user.setUsername(username);
		user.setPlainPassword(password);
		//
		USER_MANAGER.save(user);
		return user;
	};

	private Route editUser = (Request req, Response rsp) -> {
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		//
		Long id = Long.parseLong(bodyMap.get("id").toString());
		String username = bodyMap.get("username").toString();
		String password = bodyMap.get("password").toString();
		//
		User user = USER_MANAGER.findById(id);
		user.setUsername(username);
		user.setPassword(password);
		//
		USER_MANAGER.save(user);
		return user;
	};

	private Route deleteUser = (Request req, Response rsp) -> {
		String idParam = req.params("id");
		Long id = Long.parseLong(idParam);
		//
		USER_MANAGER.deleteById(id);
		return true;
	};

}
