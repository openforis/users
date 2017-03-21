package org.openforis.users.web;

import static spark.Spark.path;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openforis.users.auth.UsersConfigFactory;
import org.openforis.users.web.controller.GroupController;
import org.openforis.users.web.controller.UserController;
import org.openforis.users.web.controller.UserGroupController;
import org.pac4j.core.config.Config;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.sparkjava.SecurityFilter;
import org.pac4j.sparkjava.SparkWebContext;

import spark.Request;
import spark.Response;
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

		final Config config = new UsersConfigFactory().build();
		before("/api/*", new SecurityFilter(config, "DirectBasicAuthClient, IpClient", null, "HttpMethodMatcher"));

		path("/api", () -> {

			post("/login", JSON_CONTENT_TYPE, userController.login, jsonTransformer);
			post("/change-password", JSON_CONTENT_TYPE, userController.changePassword, jsonTransformer);

			// GROUP
			get("/group", groupController.findGroups, jsonTransformer);
			get("/group/:id", groupController.getGroup, jsonTransformer);
			post("/group", JSON_CONTENT_TYPE, groupController.addGroup, jsonTransformer);
			patch("/group/:id", JSON_CONTENT_TYPE, groupController.editGroup, jsonTransformer);
			delete("/group/:id", groupController.deleteGroup, jsonTransformer);

			// USER
			get("/user", userController.findUsers, jsonTransformer);
			get("/user/:id", userController.getUser, jsonTransformer);
			post("/user", JSON_CONTENT_TYPE, userController.addUser, jsonTransformer);
			patch("/user/:id", JSON_CONTENT_TYPE, userController.editUser, jsonTransformer);
			delete("/user/:id", userController.deleteUser, jsonTransformer);

			// USER_GROUP
			get("/user/:id/groups", userGroupController.findGroupsByUser, jsonTransformer);
			get("/group/:id/users", userGroupController.findUsersByGroup, jsonTransformer);
			post("/group/:groupId/user/:userId", userGroupController.addUserGroupJoinRequest, jsonTransformer);
			patch("/group/:groupId/user/:userId", userGroupController.updateUserGroupJoinRequest, jsonTransformer);

		});

	}

	private static Map userInfo(final Request request, final Response response) {
		final Map map = new HashMap();
		map.put("profiles", getProfiles(request, response));
		return map;
	}

	private static List<CommonProfile> getProfiles(final Request request, final Response response) {
		final SparkWebContext context = new SparkWebContext(request, response);
		final ProfileManager manager = new ProfileManager(context);
		return manager.getAll(true);
	}

}
