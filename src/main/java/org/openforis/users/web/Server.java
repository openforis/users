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

	private static void CORS() {
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

	private static void exceptionHandler() {
		Spark.exception(Exception.class, (e, req, res) -> {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			System.err.println(sw.getBuffer().toString());
			res.status(500);
			res.type(JSON_CONTENT_TYPE);
			res.body((new ResponseBody(500, "", "Internal server error")).toJson());
		});
	}

	private static void errorHandler() {
		Spark.notFound((req, res) -> {
			res.type(JSON_CONTENT_TYPE);
			return (new ResponseBody(404, "", "Not found")).toJson();
		});
		Spark.internalServerError((req, res) -> {
			res.type(JSON_CONTENT_TYPE);
			return (new ResponseBody(500, "", "Internal server error")).toJson();
		});
	}

	@Override
	public void init() {

		staticFileLocation("/public");

		jsonTransformer = new JsonTransformer();

		UserController userController = new UserController(jsonTransformer);
		GroupController groupController = new GroupController(jsonTransformer);
		UserGroupController userGroupController = new UserGroupController(jsonTransformer);

		Server.CORS();
		Server.exceptionHandler();
		Server.errorHandler();

		post("/api/login", JSON_CONTENT_TYPE, userController.login, jsonTransformer);
		post("/api/change-password", JSON_CONTENT_TYPE, userController.changePassword, jsonTransformer);

		// GROUP
		get("/api/group", groupController.findGroups, jsonTransformer);
		get("/api/group/:id", groupController.getGroup, jsonTransformer);
		post("/api/group", JSON_CONTENT_TYPE, groupController.addGroup, jsonTransformer);
		patch("/api/group/:id", JSON_CONTENT_TYPE, groupController.editGroup, jsonTransformer);
		delete("/api/group/:id", groupController.deleteGroup, jsonTransformer);

		// USER
		get("/api/user", userController.findUsers, jsonTransformer);
		get("/api/user/:id", userController.getUser, jsonTransformer);
		post("/api/user", JSON_CONTENT_TYPE, userController.addUser, jsonTransformer);
		patch("/api/user/:id", JSON_CONTENT_TYPE, userController.editUser, jsonTransformer);
		delete("/api/user/:id", userController.deleteUser, jsonTransformer);

		// USER_GROUP
		get("/api/user/:id/groups", userGroupController.findGroupsByUser, jsonTransformer);
		get("/api/group/:id/users", userGroupController.findUsersByGroup, jsonTransformer);
		post("/api/group/:groupId/user/:userId", userGroupController.addUserGroupJoinRequest, jsonTransformer);
		patch("/api/group/:groupId/user/:userId", userGroupController.updateUserGroupJoinRequest, jsonTransformer);

	}

}
