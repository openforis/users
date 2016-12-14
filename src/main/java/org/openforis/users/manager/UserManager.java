package org.openforis.users.manager;

import org.openforis.users.dao.UserDao;
import org.openforis.users.model.User;

public class UserManager extends AbstractManager<User, UserDao> {

	public UserManager(UserDao userDao) {
		super(userDao);
	}
	
	public User findByUsername(String username) {
		return dao.findByUsername(username);
	}
	
}
