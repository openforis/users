package org.openforis.users.web.controller;

import java.util.Map;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.GroupManager;
import org.openforis.users.model.Group;
import org.openforis.users.web.JsonTransformer;

import spark.Request;
import spark.Response;
import spark.Route;

public class GroupController {

	public GroupManager GROUP_MANAGER = EntityManagerFactory.getInstance().getGroupManager();

	private JsonTransformer jsonTransformer;

	public GroupController(JsonTransformer jsonTransformer) {
		this.jsonTransformer = jsonTransformer;
	}

	public Route getGroup = (Request req, Response rsp) -> {
		String idParam = req.params("id");
		long id = Long.parseLong(idParam);
		Group group = GROUP_MANAGER.findById(id);
		return group;
	};

	public Route addGroup = (Request req, Response rsp) -> {
		String body = req.body();
		Map<String, Object> bodyMap = this.jsonTransformer.parse(body);
		String name = bodyMap.get("name").toString();
		String label = bodyMap.get("label").toString();
		String description = bodyMap.get("description").toString();
		boolean enabled = (bodyMap.get("enabled") != null) ?  Boolean.valueOf(bodyMap.get("enabled").toString()) : false;
		boolean systemDefined = (bodyMap.get("system_defined") != null) ?  Boolean.valueOf(bodyMap.get("system_defined").toString()) : false;
		String visibilityCode = bodyMap.get("visibility_code").toString();
		//
		Group group = new Group();
		group.setName(name);
		group.setLabel(label);
		group.setDescription(description);
		group.setEnabled(enabled);
		group.setSystemDefined(systemDefined);
		group.setVisibilityCode(visibilityCode);
		//
		GROUP_MANAGER.save(group);
		return group;
	};

	public Route editUser = (Request req, Response rsp) -> {
		String idParam = req.params("id");
		long id = Long.parseLong(idParam);
		Group group = GROUP_MANAGER.findById(id);
		//
		String body = req.body();
		Map<String, Object> bodyMap = this.jsonTransformer.parse(body);
		String name = (bodyMap.get("name") != null) ? bodyMap.get("name").toString() : group.getName();
		String label = (bodyMap.get("label") != null) ? bodyMap.get("label").toString() : group.getLabel();
		String description = (bodyMap.get("description") != null) ? bodyMap.get("description").toString() : group.getDescription();
		boolean enabled = (bodyMap.get("enabled") != null) ?  Boolean.valueOf(bodyMap.get("enabled").toString()) : false;
		boolean systemDefined = (bodyMap.get("system_defined") != null) ? Boolean.valueOf(bodyMap.get("system_defined").toString()) : false;
		String visibilityCode = (bodyMap.get("visibility_code") != null) ? bodyMap.get("visibility_code").toString() : group.getVisibilityCode();
		//
		group.setName(name);
		group.setLabel(label);
		group.setDescription(description);
		group.setEnabled(enabled);
		group.setSystemDefined(systemDefined);
		group.setVisibilityCode(visibilityCode);
		//
		GROUP_MANAGER.save(group);
		return group;
	};

	public Route deleteUser = (Request req, Response rsp) -> {
		boolean ret = false;
		String idParam = req.params("id");
		long id = Long.parseLong(idParam);
		Group group = GROUP_MANAGER.findById(id);
		if (group != null) {
			GROUP_MANAGER.deleteById(id);
			ret = true;
		}
		return ret;
	};

}
