package org.openforis.users.web.controller;

import java.util.List;

import org.openforis.users.jooq.tables.pojos.OfUserGroup;
import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.UserGroupManager;
import org.openforis.users.web.JsonTransformer;

import spark.Request;
import spark.Response;
import spark.Route;

public class UserGroupController {

	public UserGroupManager USER_GROUP_MANAGER = EntityManagerFactory.getInstance().getUserGroupManager();

	private JsonTransformer jsonTransformer;

	public UserGroupController(JsonTransformer jsonTransformer) {
		this.jsonTransformer = jsonTransformer;
	}

	public Route getGroupsByUser = (Request req, Response rsp) -> {
		String idParam = req.params("id");
		long id = Long.parseLong(idParam);
		List<OfUserGroup> userGroups = USER_GROUP_MANAGER.getJoinByUser(id);
		return userGroups;
	};

	public Route getUsersByGroup = (Request req, Response rsp) -> {
		String idParam = req.params("id");
		long id = Long.parseLong(idParam);
		List<OfUserGroup> userGroups = USER_GROUP_MANAGER.getJoinByGroup(id);
		return userGroups;
	};

}
