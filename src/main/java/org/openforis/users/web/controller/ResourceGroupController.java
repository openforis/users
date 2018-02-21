package org.openforis.users.web.controller;

import org.openforis.users.exception.BadRequestException;
import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.ResourceGroupManager;
import org.openforis.users.web.JsonTransformer;
import org.openforis.users.web.ResponseBody;

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
		String resourceType = getStringParam(req, "resourceType");
		String resourceId = getStringParam(req, "resourceId");
		long groupId = getLongParam(req, "groupId");
		return RESOURCE_GROUP_MANAGER.associate(resourceType, resourceId, groupId);
	};

	public Route deleteResource = (Request req, Response rsp) -> {
		try {
			String resourceType = getStringParam(req, "resourceType");
			String resourceId = getStringParam(req, "resourceId");
			long groupId = getLongParam(req, "groupId");
			RESOURCE_GROUP_MANAGER.disassociate(resourceType, resourceId, groupId);
		} catch (IllegalArgumentException e) {
			throw new BadRequestException(e.getMessage());
		}
		return new ResponseBody(200);
	};

}
