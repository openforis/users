package org.openforis.users.web.controller;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.GroupManager;
import org.openforis.users.model.Group;
import org.openforis.users.web.JsonTransformer;

import spark.Request;
import spark.Response;
import spark.Route;

public class GroupController extends AbstractController {

	public GroupManager GROUP_MANAGER = EntityManagerFactory.getInstance().getGroupManager();

	public GroupController(JsonTransformer jsonTransformer) {
		super(jsonTransformer);
	}

	public Route findGroups = (Request req, Response rsp) -> {
		return GROUP_MANAGER.findAll();
	};

	public Route getGroup = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		Group group = GROUP_MANAGER.findById(id);
		return group;
	};

	public Route addGroup = (Request req, Response rsp) -> {
		Group group = this.jsonTransformer.parse(req.body(), Group.class);
		GROUP_MANAGER.save(group);
		return group;
	};

	public Route editGroup = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		Group group = GROUP_MANAGER.findById(id);
		setNotNullParams(req, group);
		GROUP_MANAGER.save(group);
		return group;
	};

	public Route deleteGroup = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		Group group = GROUP_MANAGER.findById(id);
		if (group != null) {
			GROUP_MANAGER.deleteById(id);
			return true;
		} else {
			return false;
		}
	};

}
