package org.openforis.users.web.controller;

import java.util.HashSet;
import java.util.Set;

import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.ResourceGroupManager;
import org.openforis.users.manager.GroupManager.SearchParameters;
import org.openforis.users.model.Group.Visibility;
import org.openforis.users.web.JsonTransformer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ResourceGroupController extends AbstractController {
	
	public ResourceGroupManager RESOURCE_GROUP_MANAGER = EntityManagerFactory.getInstance().getResourceGroupManager();

	public ResourceGroupController(JsonTransformer jsonTransformer) {
		super(jsonTransformer);
	}
	
	public Route findResources = (Request req, Response rsp) -> {
		String resourceType = getStringParam(req, "resourceType");
		long groupId = getLongParam(req, "groupId");
		return RESOURCE_GROUP_MANAGER.loadResourceIds(resourceType, groupId);
	};

	public Route addResource = (Request req, Response rsp) -> {
		long groupId = getLongParam(req, "groupId");
		String resourceType = getStringParam(req, "resourceType");
		String resourceId = getStringParam(req, "resourceId");
		return RESOURCE_GROUP_MANAGER.associate(groupId, resourceType, resourceId);
	};
	
	public Route deleteResource = (Request req, Response rsp) -> {
		long groupId = getLongParam(req, "groupId");
		String resourceType = getStringParam(req, "resourceType");
		String resourceId = getStringParam(req, "resourceId");
		return RESOURCE_GROUP_MANAGER.disassociate(groupId, resourceType, resourceId);
	};
	
	public Route saveResources = (Request req, Response rsp) -> {
		long groupId = getLongParam(req, "groupId");
		String resourceType = getStringParam(req, "resourceType");
		String resourceId = getStringParam(req, "resourceIds"); //TODO get list of ids
		Set<String> resourceIds = new HashSet<String>();
		return RESOURCE_GROUP_MANAGER.saveAssociations(groupId, resourceType, resourceIds);
	};
}
