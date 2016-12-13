package org.openforis.users.manager;

import org.jooq.DAO;
import org.openforis.users.jooq.tables.daos.UserDao;
import org.openforis.users.model.User;

public class UserManager extends AbstractManager<User> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public UserManager(UserDao userDao) {
		super((DAO) userDao);
	}
	
}
