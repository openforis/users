package org.openforis.users.web;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.openforis.users.web.controller.GroupController;
import org.openforis.users.web.controller.UserController;
import org.openforis.users.web.controller.UserGroupController;

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

		UserController userController = new UserController(jsonTransformer);
		GroupController groupController = new GroupController(jsonTransformer);
		UserGroupController userGroupController = new UserGroupController(jsonTransformer);

		Server.enebleCORS();
		Server.enebleExceptionHandler();

		post("/api/login", JSON_CONTENT_TYPE, userController.login, new JsonTransformer());
		post("/api/change-password", JSON_CONTENT_TYPE, userController.changePassword, new JsonTransformer());

		// GROUP
		get("/api/group", groupController.findGroups, new JsonTransformer());
		get("/api/group/:id", groupController.getGroup, new JsonTransformer());
		post("/api/group", JSON_CONTENT_TYPE, groupController.addGroup, new JsonTransformer());
		patch("/api/group/:id", JSON_CONTENT_TYPE, groupController.editGroup, new JsonTransformer());
		delete("/api/group/:id", groupController.deleteGroup, new JsonTransformer());

		// USER
		get("/api/user", userController.findUsers, new JsonTransformer());
		get("/api/user/:id", userController.getUser, new JsonTransformer());
		post("/api/user", JSON_CONTENT_TYPE, userController.addUser, new JsonTransformer());
		patch("/api/user/:id", JSON_CONTENT_TYPE, userController.editUser, new JsonTransformer());
		delete("/api/user/:id", userController.deleteUser, new JsonTransformer());

		//USER_GROUP
		get("/api/user/:id/groups", userGroupController.getGroupsByUser, new JsonTransformer());
		get("/api/group/:id/users", userGroupController.getUsersByGroup, new JsonTransformer());

	}

}
