package org.openforis.users.web.controller;

import java.util.Map;

import org.openforis.users.exception.BadRequestException;
import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.manager.UserGroupManager;
import org.openforis.users.model.UserGroup;
import org.openforis.users.model.UserGroup.UserGroupRequestStatus;
import org.openforis.users.model.UserGroup.UserGroupRole;
import org.openforis.users.web.JsonTransformer;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * 
 * @author R. Fontanarosa
 * @author S. Ricci
 *
 */
public class UserGroupController extends AbstractController {

	public UserGroupManager USER_GROUP_MANAGER = EntityManagerFactory.getInstance().getUserGroupManager();

	public UserGroupController(JsonTransformer jsonTransformer) {
		super(jsonTransformer);
	}

	public Route findGroupsByUser = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		return USER_GROUP_MANAGER.findJoinByUser(id);
	};

	public Route findUsersByGroup = (Request req, Response rsp) -> {
		long id = getLongParam(req, "id");
		return USER_GROUP_MANAGER.findJoinByGroup(id);
	};

	public Route addUserGroupJoinRequest = (Request req, Response rsp) -> {
		long groupId = getLongParam(req, "groupId");
		long userId = getLongParam(req, "userId");
		UserGroup userGroup = USER_GROUP_MANAGER.requestJoin(groupId, userId);
		return userGroup;
	};

	public Route editUserGroup = (Request req, Response rsp) -> {
		long groupId = getLongParam(req, "groupId");
		long userId = getLongParam(req, "userId");
		Map<String, Object> body = jsonTransformer.parse(req.body());
		try {
			String roleCode = body.get("roleCode").toString();
			String statusCode = body.get("statusCode").toString();
			UserGroupRole role = UserGroupRole.fromCode(roleCode);
			UserGroupRequestStatus status = UserGroupRequestStatus.fromCode(statusCode);
			USER_GROUP_MANAGER.editByGroupIdAndUserId(groupId, userId, role, status);
		} catch (NullPointerException | IllegalArgumentException e) {
			throw new BadRequestException();
		}
		return true;
	};


	public Route deleteUserGroup = (Request req, Response rsp) -> {
		boolean ret = false;
		long groupId = getLongParam(req, "groupId");
		long userId = getLongParam(req, "userId");
		try {
			USER_GROUP_MANAGER.deleteByGroupIdAndUserId(groupId, userId);
			ret = true;
		} catch (Exception e) {
			System.out.println(1);
		}
		return ret;
	};

}
