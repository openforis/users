package org.openforis.users.manager;

import java.util.ArrayList;
import java.util.List;

import org.openforis.users.dao.UserGroupDao;
import org.openforis.users.jooq.tables.pojos.OfUserGroup;
import org.openforis.users.model.Group;
import org.openforis.users.model.User;
import org.openforis.users.model.UserGroup;
import org.openforis.users.model.UserGroup.UserGroupRequestStatus;
import org.openforis.users.model.UserGroup.UserGroupRole;

/**
 * 
 * @author S. Ricci
 *
 */
public class UserGroupManager {

	private UserGroupDao userGroupDao;
	private GroupManager groupManager;
	private UserManager userManager;

	public UserGroupManager(UserGroupDao userGroupDao, GroupManager groupManager, UserManager userManager) {
		super();
		this.userGroupDao = userGroupDao;
		this.groupManager = groupManager;
		this.userManager = userManager;
	}

	public List<UserGroup> findAcceptedUserDefinedGroupsByUser(User user) {
		List<OfUserGroup> ofUserGroups = userGroupDao.fetchByUserId(user.getId());
		List<UserGroup> userGroups = new ArrayList<UserGroup>(ofUserGroups.size());
		for (OfUserGroup ofUserGroup : ofUserGroups) {
			UserGroupRequestStatus status = UserGroupRequestStatus.fromCode(ofUserGroup.getStatusCode());
			if (status == UserGroupRequestStatus.ACCEPTED) {
				UserGroup userGroup = fill(ofUserGroup);
				if (!userGroup.getGroup().getSystemDefined()) {
					userGroups.add(userGroup);
				}
			}
		}
		return userGroups;
	}

	public List<UserGroup> findJoinByUser(Long id) {
		List<OfUserGroup> ofUserGroups = userGroupDao.fetchByUserId(id);
		return fill(ofUserGroups);
	}

	public List<UserGroup> findJoinByGroup(Long id) {
		List<OfUserGroup> ofUserGroups = userGroupDao.fetchByGroupId(id);
		return fill(ofUserGroups);
	}

	public UserGroup getJoinByGroupAndUser(long groupId, long userId) {
		return userGroupDao.fetchByGroupIdAndUserId(groupId, userId);
	}

	public UserGroup requestJoin(long groupId, long userId) {
		return requestJoin(groupId, userId, UserGroupRole.OPERATOR);
	}

	public UserGroup requestJoin(long groupId, long userId, UserGroupRole role) {
		return insertJoin(groupId, userId, role, UserGroupRequestStatus.PENDING);
	}

	public UserGroup insertJoin(long groupId, long userId, UserGroupRole role) {
		return insertJoin(groupId, userId, role, UserGroupRequestStatus.ACCEPTED);
	}

	public UserGroup insertJoin(long groupId, long userId, UserGroupRole role, UserGroupRequestStatus status) {
		UserGroup userGroup = new UserGroup();
		userGroup.setUserId(userId);
		userGroup.setGroupId(groupId);
		userGroup.setStatusCode(status.getCode());
		userGroup.setRoleCode(role.getCode());
		userGroupDao.insert(userGroup);
		return userGroup;
	}
	
	public UserGroup approveJoinRequest(long groupId, long userId) {
		UserGroup userGroup = userGroupDao.findById(groupId, userId);
		userGroup.setStatusCode(UserGroupRequestStatus.ACCEPTED.getCode());
		userGroupDao.update(userGroup);
		return userGroup;
	}

	private List<UserGroup> fill(List<OfUserGroup> ofUserGroups) {
		List<UserGroup> result = new ArrayList<UserGroup>();
		for (OfUserGroup ofUserGroup : ofUserGroups) {
			result.add(fill(ofUserGroup));
		}
		return result;
	}

	private UserGroup fill(OfUserGroup ofUserGroup) {
		User user = userManager.findById(ofUserGroup.getUserId());
		Group group = groupManager.findById(ofUserGroup.getGroupId());
		return new UserGroup(ofUserGroup, user, group);
	}

	public void editByGroupIdAndUserId(long groupId, long userId, UserGroupRole role, UserGroupRequestStatus status) {
		userGroupDao.editByGroupIdAndUserId(groupId, userId, role, status);
	}

	public void deleteByGroupIdAndUserId(long groupId, long userId) {
		userGroupDao.deleteByGroupIdAndUserId(groupId, userId);
	}

}
