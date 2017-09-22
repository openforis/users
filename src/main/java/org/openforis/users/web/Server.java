package org.openforis.users.web;

import static spark.Spark.path;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.patch;
import static spark.Spark.post;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openforis.users.auth.UsersConfigFactory;
import org.openforis.users.exception.BadRequestException;
import org.openforis.users.exception.NotFoundException;
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
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";

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
		Spark.exception(BadRequestException.class, (e, req, res) -> {
			res.status(400);
			res.type(JSON_CONTENT_TYPE);
			res.body((new ResponseBody(400, "400 Bad Request", e.getMessage())).toJson());
		});
		Spark.exception(NotFoundException.class, (e, req, res) -> {
			res.status(404);
			res.type(JSON_CONTENT_TYPE);
			res.body((new ResponseBody(404, "404 Not Found", e.getMessage())).toJson());
		});
		Spark.exception(Exception.class, (e, req, res) -> {
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			System.err.println(sw.getBuffer().toString()); // TODO logger
			res.status(500);
			res.type(JSON_CONTENT_TYPE);
			res.body((new ResponseBody(500, "500 Internal Server Error", "Internal Server Error")).toJson());
		});
	}

	@Override
	public void init() {

		jsonTransformer = new JsonTransformer();

		UserController userController = new UserController(jsonTransformer);
		GroupController groupController = new GroupController(jsonTransformer);
		UserGroupController userGroupController = new UserGroupController(jsonTransformer);

		Server.CORS();
		Server.exceptionHandler();

		final Config config = new UsersConfigFactory("127\\..*|10\\..*").build();
		before("/api/*", new SecurityFilter(config, "DirectBasicAuthClient, IpClient", null, "HttpMethodMatcher"));

		path("/api", () -> {

			post("/login", JSON_CONTENT_TYPE, userController.login, jsonTransformer);
			post("/change-password", JSON_CONTENT_TYPE, userController.changePassword, jsonTransformer);

			// USER
			get("/user", userController.findUsers, jsonTransformer);
			get("/user/:id", userController.getUser, jsonTransformer);
			post("/user", JSON_CONTENT_TYPE, userController.addUser, jsonTransformer);
			patch("/user/:id", JSON_CONTENT_TYPE, userController.editUser, jsonTransformer);
			delete("/user/:id", userController.deleteUser, jsonTransformer);

			// GROUP
			get("/group", groupController.findGroups, jsonTransformer);
			get("/group/:id", groupController.getGroup, jsonTransformer);
			post("/group", MULTIPART_FORM_DATA, groupController.addGroup, jsonTransformer);
			patch("/group/:id", MULTIPART_FORM_DATA, groupController.editGroup, jsonTransformer);
			delete("/group/:id", groupController.deleteGroup, jsonTransformer);
			
			// USER_GROUP
			get("/user/:id/groups", userGroupController.findGroupsByUser, jsonTransformer);
			get("/group/:id/users", userGroupController.findUsersByGroup, jsonTransformer);

			//
			post("/group/:groupId/user/:userId", userGroupController.addUserGroupJoinRequest, jsonTransformer);
			patch("/group/:groupId/user/:userId", userGroupController.editUserGroup, jsonTransformer);
			delete("/group/:groupId/user/:userId", userGroupController.deleteUserGroup, jsonTransformer);

		});

	}

	@SuppressWarnings("rawtypes")
	private static Map userInfo(final Request request, final Response response) {
		final Map map = new HashMap();
		map.put("profiles", getProfiles(request, response));
		return map;
	}

	@SuppressWarnings("rawtypes")
	private static List<CommonProfile> getProfiles(final Request request, final Response response) {
		final SparkWebContext context = new SparkWebContext(request, response);
		final ProfileManager manager = new ProfileManager(context);
		return manager.getAll(true);
	}

}
