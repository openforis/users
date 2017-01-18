package org.openforis.users.dao;

import static org.openforis.users.jooq.tables.OfUserGroup.OF_USER_GROUP;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.openforis.users.jooq.tables.daos.OfUserGroupDao;
import org.openforis.users.model.Group;
import org.openforis.users.model.User;
import org.openforis.users.model.UserGroup.UserGroupRequestStatus;

/**
 * 
 * @author S. Ricci
 *
 */
public class UserGroupDao extends OfUserGroupDao {

	public UserGroupDao(Configuration configuration) {
		super(configuration);
	}

	public void updateJoinRequestStatus(Group group, User user, UserGroupRequestStatus status) {
		dsl().update(OF_USER_GROUP)
			.set(OF_USER_GROUP.STATUS_CODE, status.getCode())
			.where(OF_USER_GROUP.GROUP_ID.eq(group.getId())
				.and(OF_USER_GROUP.USER_ID.eq(user.getId())))
			.execute();
	}
	
	private DSLContext dsl() {
		return DSL.using(configuration());
	}
	
}
