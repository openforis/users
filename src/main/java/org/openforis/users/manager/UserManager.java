package org.openforis.users.manager;

import org.openforis.users.dao.UserDao;
import org.openforis.users.model.Group;
import org.openforis.users.model.User;
import org.openforis.users.model.UserGroup.UserGroupRole;
import org.openforis.users.model.Group.Visibility;

/**
 * 
 * @author S. Ricci
 *
 */
public class UserManager extends AbstractManager<User, UserDao> {

	private GroupManager groupManager;
	private UserGroupManager userGroupManager;
	
	public UserManager(GroupManager groupManager, UserGroupManager userGroupManager, UserDao userDao) {
		super(userDao);
		this.groupManager = groupManager;
		this.userGroupManager = userGroupManager;
	}
	
	@Override
	protected void insert(User user) {
		super.insert(user);
		Group privateGroup = new Group();
		privateGroup.setName(user.getUsername() + "_private_group");
		privateGroup.setLabel(user.getUsername() + " Private Group");
		privateGroup.setVisibility(Visibility.PRIVATE);
		privateGroup.setSystemDefined(true);
		privateGroup.setEnabled(true);
		groupManager.insert(privateGroup);
		userGroupManager.join(privateGroup, user, UserGroupRole.OWNER);
	}
	
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}
	
}
