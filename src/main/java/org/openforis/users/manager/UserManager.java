package org.openforis.users.manager;

import org.openforis.users.model.User;

public class UserManager extends AbstractEntityManager<User> {

	public UserManager() {
		super(User.class);
	}

}
