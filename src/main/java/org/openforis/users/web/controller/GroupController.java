package org.openforis.users.web.controller;

import org.openforis.users.exception.NotFoundException;
import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.GroupManager;
import org.openforis.users.manager.GroupManager.SearchParameters;
import org.openforis.users.model.Group;
import org.openforis.users.model.Group.Visibility;
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
		SearchParameters params = new SearchParameters();
		params.setName(getStringParam(req, "name"));
		params.setEnabled(getBooleanParam(req, "enabled", true));
		params.setSystemDefined(getBooleanParam(req, "systemDefined"));
		params.setVisibility(getEnumParam(req, "visibility", Visibility.class));
		return GROUP_MANAGER.findAll(params);
	};

	public Route getGroup = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		Group group = GROUP_MANAGER.findById(id);
		if (group!= null) {
			return group;
		} else {
			throw new NotFoundException("Group not found");
		}
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
		} else {
			throw new NotFoundException("Group not found");
		}
		return rsp;
	};

}
