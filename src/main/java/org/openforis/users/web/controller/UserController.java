package org.openforis.users.web.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.UserManager;
import org.openforis.users.model.User;
import org.openforis.users.web.JsonTransformer;
import org.openforis.users.web.ResponseBody;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserController extends AbstractController {

	public UserManager USER_MANAGER = EntityManagerFactory.getInstance().getUserManager();

	public UserController(JsonTransformer jsonTransformer) {
		super(jsonTransformer);
	}

	public Route findUsers = (Request req, Response rsp) -> {
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

	public Route getUser = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		User user = USER_MANAGER.findById(id);
		return user;
	};

	public Route addUser = (Request req, Response rsp) -> {
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		String username = bodyMap.get("username").toString();
		String password = bodyMap.get("rawPassword").toString();
		Boolean enabled = Boolean.valueOf(bodyMap.get("enabled").toString());
		User user = new User();
		user.setUsername(username);
		user.setRawPassword(password);
		user.setEnabled(enabled);
		USER_MANAGER.save(user);
		return user;
	};

	public Route editUser = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		User user = USER_MANAGER.findById(id);
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		String username = (bodyMap.get("username") != null) ? bodyMap.get("username").toString() : user.getUsername();
		boolean enabled = (bodyMap.get("enabled") != null) ? Boolean.valueOf(bodyMap.get("enabled").toString()) : false;
		user.setUsername(username);
		user.setRawPassword(null); // do not overwrite password
		user.setEnabled(enabled);
		USER_MANAGER.save(user);
		return user;
	};

	public Route deleteUser = (Request req, Response rsp) -> {
		boolean ret = false;
		long id = getLongParam(req, "id");
		User user = USER_MANAGER.findById(id);
		if (user != null) {
			USER_MANAGER.deleteById(id);
			ret = true;
		}
		return ret;
	};

	public Route login = (Request req, Response rsp) -> {
		ResponseBody result;
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		String username = bodyMap.get("username").toString();
		String password = bodyMap.get("rawPassword").toString();
		if (USER_MANAGER.verifyPassword(username, password)) {
			result = new ResponseBody(200, "", "");
		} else {
			result = new ResponseBody(400, "", "Wrong username or password");
		}
		return result;
	};

	public Route changePassword = (Request req, Response rsp) -> {
		ResponseBody result;
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		String username = bodyMap.get("username").toString();
		String newPassword = bodyMap.get("newPassword").toString();
		try {
			USER_MANAGER.changePassword(username, newPassword);
			result = new ResponseBody(200, "", "");
		} catch(IllegalArgumentException e) {
			result = new ResponseBody(400, "", e.getMessage());
		}
		return result;
	};

}
