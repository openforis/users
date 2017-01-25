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
 * @author S. Rricci
 *
 */
public class UserGroupManager {
	
	private GroupManager groupManager;
	private UserGroupDao userGroupDao;
	
	public UserGroupManager(UserGroupDao userGroupDao) {
		super();
		this.userGroupDao = userGroupDao;
	}
	
	public List<UserGroup> loadAcceptedUserDefinedGroupsByUser(User user) {
		List<OfUserGroup> ofUserGroups = userGroupDao.fetchByUserId(user.getId());
		List<UserGroup> userGroups = new ArrayList<UserGroup>(ofUserGroups.size());
		for (OfUserGroup ofUserGroup : ofUserGroups) {
			UserGroupRequestStatus status = UserGroupRequestStatus.fromCode(ofUserGroup.getStatusCode());
			if (status == UserGroupRequestStatus.ACCEPTED) {
				Group group = groupManager.findById(ofUserGroup.getGroupId());
				if (! group.getSystemDefined()) {
					UserGroup userGroup = new UserGroup(ofUserGroup);
					userGroup.setUser(user);
					userGroup.setGroup(group);
					userGroups.add(userGroup);
				}
			}
		}
		return userGroups;
	}
	
	public void requestJoin(Group group, User user) {
		requestJoin(group, user, UserGroupRole.OPERATOR);
	}

	public void requestJoin(Group group, User user, UserGroupRole role) {
		insertJoin(group, user, role, UserGroupRequestStatus.PENDING);
	}

	public void acceptJoinRequest(Group group, User user) {
		userGroupDao.updateJoinRequestStatus(group, user, UserGroupRequestStatus.ACCEPTED);
	}

	public void rejectJoinRequest(Group group, User user) {
		userGroupDao.updateJoinRequestStatus(group, user, UserGroupRequestStatus.REJECTED);
	}

	public void join(Group group, User user, UserGroupRole role) {
		insertJoin(group, user, role, UserGroupRequestStatus.ACCEPTED);
	}
	
	private void insertJoin(Group group, User user, UserGroupRole role, UserGroupRequestStatus status) {
		UserGroup userGroup = new UserGroup();
		userGroup.setUserId(user.getId());
		userGroup.setGroupId(group.getId());
		userGroup.setStatusCode(status.getCode());
		userGroup.setRoleCode(role.getCode());
		userGroupDao.insert(userGroup);
	}

	public List<OfUserGroup> getJoinByUser(Long id) {
		return userGroupDao.fetchByUserId(id);
	}

	public List<OfUserGroup> getJoinByGroup(Long id) {
		return userGroupDao.fetchByGroupId(id);
	}

}
