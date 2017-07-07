package org.openforis.users.web.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.joda.time.DateTime;
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
		if (user!= null) {
			return user;
		} else {
			rsp.status(404);
			return new ResponseBody(404, "", "User not found").toJson();
		}
	};

	public Route addUser = (Request req, Response rsp) -> {
		try {
			String body = req.body();
			Map<String, Object> bodyMap = jsonTransformer.parse(body);
			//
			String username = bodyMap.get("username").toString();
			String password = bodyMap.get("rawPassword").toString();
			Boolean enabled = bodyMap.containsKey("enabled") ? Boolean.valueOf(bodyMap.get("enabled").toString()) : false;
			BigDecimal lat = bodyMap.containsKey("lat") ? new BigDecimal(bodyMap.get("lat").toString()) : null;
			BigDecimal lon = bodyMap.containsKey("lat") ? new BigDecimal(bodyMap.get("lat").toString()) : null;
			String location = bodyMap.containsKey("location") ? bodyMap.get("location").toString() : null;
			String affiliations =  bodyMap.containsKey("affiliations") ? bodyMap.get("affiliations").toString() : null;
			//
			User user = new User();
			user.setUsername(username);
			user.setRawPassword(password);
			user.setEnabled(enabled);
			user.setLat(lat);
			user.setLon(lon);
			user.setLocation(location);
			user.setAffiliations(affiliations);
			DateTime dt = DateTime.now();
			user.setCreationDate(new Timestamp(dt.getMillis()));
			//
			USER_MANAGER.save(user);
			user.setRawPassword("");
			return user;
		} catch(Exception e) {
			rsp.status(500);
			return new ResponseBody(500, "", "").toJson();
		}
	};

	public Route editUser = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		User user = USER_MANAGER.findById(id);
		String body = req.body();
		Map<String, Object> bodyMap = jsonTransformer.parse(body);
		//
		String username = bodyMap.get("username").toString();
		Boolean enabled = Boolean.valueOf(bodyMap.get("enabled").toString());
		BigDecimal lat = bodyMap.containsKey("lat") ? new BigDecimal(bodyMap.get("lat").toString()) : null;
		BigDecimal lon = bodyMap.containsKey("lat") ? new BigDecimal(bodyMap.get("lat").toString()) : null;
		String location = bodyMap.containsKey("location") ? bodyMap.get("location").toString() : null;
		String affiliations =  bodyMap.containsKey("affiliations") ? bodyMap.get("affiliations").toString() : null;
		//
		if (username == null) username = user.getUsername();
		if (enabled == null) enabled = user.getEnabled();
		if (lat == null) lat = user.getLat();
		if (lon == null) lon = user.getLon();
		if (location == null) location = user.getLocation();
		if (affiliations == null) affiliations = user.getAffiliations();
		//
		user.setUsername(username);
		user.setRawPassword(null); // do not overwrite password
		user.setEnabled(enabled);
		user.setLat(lat);
		user.setLon(lon);
		user.setLocation(location);
		user.setAffiliations(affiliations);
		USER_MANAGER.save(user);
		return user;
	};

	public Route deleteUser = (Request req, Response rsp) -> {
		boolean ret = false;
		try {
			long id = getLongParam(req, "id");
			User user = USER_MANAGER.findById(id);
			if (user != null) {
				USER_MANAGER.deleteById(id);
				ret = true;
			}
		} catch (Exception e) {
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
