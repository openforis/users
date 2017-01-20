package org.openforis.users.web;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.UserManager;
import org.openforis.users.model.User;

import freemarker.template.utility.StringUtil;
import liquibase.util.StringUtils;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.servlet.SparkApplication;

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

	private static void enebleExceptionHandler() {
		Spark.exception(Exception.class, (e, request, response) -> {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			System.err.println(sw.getBuffer().toString());
		});
	}

	@Override
	public void init() {

		staticFileLocation("/public");

		jsonTransformer = new JsonTransformer();

		Server.enebleCORS();
		Server.enebleExceptionHandler();

		// USER
		get("/api/user", findUsers, new JsonTransformer());
		post("/api/user", JSON_CONTENT_TYPE, addUser, new JsonTransformer());
		patch("/api/user", JSON_CONTENT_TYPE, editUser, new JsonTransformer());
		delete("/api/user/:id", JSON_CONTENT_TYPE, deleteUser, new JsonTransformer());

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
		//
		String username = bodyMap.get("username").toString();
		String password = bodyMap.get("password").toString();
		Boolean enabled = Boolean.valueOf(bodyMap.get("enabled").toString());
		//
		User user = new User();
		user.setUsername(username);
		user.setPlainPassword(password);
		user.setEnabled(enabled);
		//
		USER_MANAGER.save(user);
		return user;
	};

	private Route editUser = (Request req, Response rsp) -> {
		String body = req.body();

		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		//
		Double idDouble = Double.parseDouble(bodyMap.get("id").toString());
		long id = idDouble.longValue();
		//
		String username = bodyMap.get("username").toString();
		//String password = bodyMap.get("password").toString();
		Boolean enabled = Boolean.valueOf(bodyMap.get("enabled").toString());
		//
		User user = USER_MANAGER.findById(id);
		user.setUsername(username);
		user.setPlainPassword(null); // do not overwrite password
		user.setEnabled(enabled);
		//
		USER_MANAGER.save(user);
		return user;
	};

	private Route deleteUser = (Request req, Response rsp) -> {
		String idParam = req.params("id");
		//
		long id = Long.parseLong(idParam);
		//
		USER_MANAGER.deleteById(id);
		return true;
	};

}
