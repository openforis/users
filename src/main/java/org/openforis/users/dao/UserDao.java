package org.openforis.users.dao;

import static org.openforis.users.jooq.tables.User.USER;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.openforis.users.model.User;

public class UserDao extends org.openforis.users.jooq.tables.daos.UserDao {

	public UserDao(Configuration configuration) {
		super(configuration);
	}
	
	public User findByUsername(String username) {
		User result = dsl()
			.selectFrom(getTable())
			.where(USER.USERNAME.eq(username))
			.fetchOneInto(User.class);
		return result;
	}

	private DSLContext dsl() {
		return DSL.using(configuration());
	}
	
}
