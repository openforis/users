package org.openforis.users.dao;

import static org.openforis.users.jooq.tables.OfGroup.OF_GROUP;
import static org.openforis.users.jooq.tables.OfUser.OF_USER;
import static org.openforis.users.jooq.tables.OfUserGroup.OF_USER_GROUP;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.openforis.users.jooq.tables.daos.OfUserGroupDao;
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

	public void updateJoinRequestStatus(long groupId, long userId, UserGroupRequestStatus status) {
		dsl().update(OF_USER_GROUP).set(OF_USER_GROUP.STATUS_CODE, status.getCode())
				.where(OF_USER_GROUP.GROUP_ID.eq(groupId).and(OF_USER_GROUP.USER_ID.eq(userId))).execute();
	}

	public void deleteByGroupIdAndUserId(long groupId, long userId) {
		dsl().deleteFrom(OF_USER_GROUP).where(OF_USER_GROUP.GROUP_ID.eq(groupId)).and(OF_USER_GROUP.USER_ID.eq(userId)).execute();
	}

	private DSLContext dsl() {
		return DSL.using(configuration());
	}

}
