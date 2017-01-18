package org.openforis.users.manager;

import org.openforis.users.dao.UserGroupDao;
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
	
	private UserGroupDao userGroupDao;
	
	public UserGroupManager(UserGroupDao userGroupDao) {
		super();
		this.userGroupDao = userGroupDao;
	}

	public void acceptJoinRequest(Group group, User user) {
		userGroupDao.updateJoinRequestStatus(group, user, UserGroupRequestStatus.ACCEPTED);
	}

	public void rejectJoinRequest(Group group, User user) {
		userGroupDao.updateJoinRequestStatus(group, user, UserGroupRequestStatus.REJECTED);
	}

	public void requestJoin(Group group, User user) {
		requestJoin(group, user, UserGroupRole.OPERATOR);
	}

	public void join(Group group, User user, UserGroupRole role) {
		insertJoin(group, user, role, UserGroupRequestStatus.ACCEPTED);
	}
	
	public void requestJoin(Group group, User user, UserGroupRole role) {
		insertJoin(group, user, role, UserGroupRequestStatus.PENDING);
	}

	private void insertJoin(Group group, User user, UserGroupRole role, UserGroupRequestStatus status) {
		UserGroup userGroup = new UserGroup();
		userGroup.setUserId(user.getId());
		userGroup.setGroupId(group.getId());
		userGroup.setStatus(status.getCode());
		userGroup.setRole(role.getCode());
		userGroupDao.insert(userGroup);
	}
}
