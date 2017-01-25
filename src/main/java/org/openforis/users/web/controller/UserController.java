package org.openforis.users.web.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.UserManager;
import org.openforis.users.manager.UserManager.OperationResult;
import org.openforis.users.model.User;
import org.openforis.users.web.JsonTransformer;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserController {

	public UserManager USER_MANAGER = EntityManagerFactory.getInstance().getUserManager();

	private JsonTransformer jsonTransformer;

	public UserController(JsonTransformer jsonTransformer) {
		this.jsonTransformer = jsonTransformer;
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
		String idParam = req.params("id");
		long id = Long.parseLong(idParam);
		User user = USER_MANAGER.findById(id);
		return user;
	};

	public Route addUser = (Request req, Response rsp) -> {
		String body = req.body();
		Map<String, Object> bodyMap = this.jsonTransformer.parse(body);
		String username = bodyMap.get("username").toString();
		String password = bodyMap.get("rawPassword").toString();
		Boolean enabled = Boolean.valueOf(bodyMap.get("enabled").toString());
		//
		User user = new User();
		user.setUsername(username);
		user.setRawPassword(password);
		user.setEnabled(enabled);
		//
		USER_MANAGER.save(user);
		return user;
	};

	public Route editUser = (Request req, Response rsp) -> {
		String idParam = req.params("id");
		long id = Long.parseLong(idParam);
		User user = USER_MANAGER.findById(id);
		//
		String body = req.body();
		Map<String, Object> bodyMap = this.jsonTransformer.parse(body);
		String username = (bodyMap.get("username") != null) ? bodyMap.get("username").toString() : user.getUsername();
		boolean enabled = (bodyMap.get("enabled") != null) ? Boolean.valueOf(bodyMap.get("enabled").toString()) : false;
		//
		user.setUsername(username);
		user.setRawPassword(null); // do not overwrite password
		user.setEnabled(enabled);
		//
		USER_MANAGER.save(user);
		return user;
	};

	public Route deleteUser = (Request req, Response rsp) -> {
		boolean ret = false;
		String idParam = req.params("id");
		long id = Long.parseLong(idParam);
		User user = USER_MANAGER.findById(id);
		if (user != null) {
			USER_MANAGER.deleteById(id);
			ret = true;
		}
		return ret;
	};

	public Route login = (Request req, Response rsp) -> {
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		String username = bodyMap.get("username").toString();
		String password = bodyMap.get("rawPassword").toString();
		if (USER_MANAGER.verifyPassword(username, password)) {
			return new OperationResult();
		} else {
			return new OperationResult(false, "WRONG_PASSWORD", "Wrong username or password");
		}
	};

	public Route changePassword = (Request req, Response rsp) -> {
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		String username = bodyMap.get("username").toString();
		String oldPassword = bodyMap.get("oldPassword").toString();
		String newPassword = bodyMap.get("newPassword").toString();
		return USER_MANAGER.changePassword(username, oldPassword, newPassword);
	};

}
