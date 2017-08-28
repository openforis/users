package org.openforis.users.web.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import org.joda.time.DateTime;
import org.openforis.users.exception.BadRequestException;
import org.openforis.users.exception.NotFoundException;
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
		if (user == null) throw new NotFoundException("User not found");
		return user;
	};

	public Route addUser = (Request req, Response rsp) -> {
		Map<String, Object> body = jsonTransformer.parse(req.body());
		if (body == null || !body.containsKey("username") || !body.containsKey("rawPassword")) throw new BadRequestException();
		//
		String username = body.get("username").toString();
		String password = body.get("rawPassword").toString();
		Boolean enabled = body.containsKey("enabled") ? Boolean.valueOf(body.get("enabled").toString()) : false;
		BigDecimal lat = body.containsKey("lat") ? new BigDecimal(body.get("lat").toString()) : null;
		BigDecimal lon = body.containsKey("lat") ? new BigDecimal(body.get("lat").toString()) : null;
		String location = body.containsKey("location") ? body.get("location").toString() : null;
		String affiliations =  body.containsKey("affiliations") ? body.get("affiliations").toString() : null;
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
		user.setRawPassword(""); // hide plain password
		return user;
	};

	public Route editUser = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		User user = USER_MANAGER.findById(id);
		if (user == null) throw new NotFoundException("User not found");
		//
		Map<String, Object> body = jsonTransformer.parse(req.body());
		if (body == null) throw new BadRequestException();
		//
		String username = body.containsKey("username") ? body.get("username").toString() : null;
		Boolean enabled =  body.containsKey("enabled") ? Boolean.valueOf(body.get("enabled").toString()) : null;
		BigDecimal lat = body.containsKey("lat") ? new BigDecimal(body.get("lat").toString()) : null;
		BigDecimal lon = body.containsKey("lat") ? new BigDecimal(body.get("lat").toString()) : null;
		String location = body.containsKey("location") ? body.get("location").toString() : null;
		String affiliations =  body.containsKey("affiliations") ? body.get("affiliations").toString() : null;
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
		//
		USER_MANAGER.save(user);
		return user;
	};

	public Route deleteUser = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		User user = USER_MANAGER.findById(id);
		if (user == null) throw new NotFoundException("User not found");
		USER_MANAGER.deleteById(id);
		return new ResponseBody(200, "", "");
	};

	public Route login = (Request req, Response rsp) -> {
		Map<String, Object> body = jsonTransformer.parse(req.body());
		if (body == null || !body.containsKey("username") || !body.containsKey("password")) throw new BadRequestException("Missing username or password");
		String username = body.get("username").toString();
		String password = body.get("rawPassword").toString();
		try {
			USER_MANAGER.verifyPassword(username, password);
		} catch (IllegalArgumentException e) {
			throw new BadRequestException(e.getMessage());
		}
		return new ResponseBody(200, "", "");
	};

	public Route changePassword = (Request req, Response rsp) -> {
		Map<String, Object> body = jsonTransformer.parse(req.body());
		if (body == null || !body.containsKey("username") || !body.containsKey("newPassword")) throw new BadRequestException("Missing username or password");
		String username = body.get("username").toString();
		String newPassword = body.get("newPassword").toString();
		try {
			USER_MANAGER.changePassword(username, newPassword);
		} catch (IllegalArgumentException e) {
			throw new BadRequestException(e.getMessage());
		}
		return new ResponseBody(200, "", "");
	};

}
