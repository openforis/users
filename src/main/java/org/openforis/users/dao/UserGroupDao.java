package org.openforis.users.dao;

import static org.openforis.users.jooq.tables.OfUserGroup.OF_USER_GROUP;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.TransactionalRunnable;
import org.jooq.impl.DSL;
import org.openforis.users.jooq.tables.daos.OfUserGroupDao;
import org.openforis.users.model.UserGroup;
import org.openforis.users.model.UserGroup.UserGroupRequestStatus;
import org.openforis.users.model.UserGroup.UserGroupRole;

/**
 * 
 * @author S. Ricci
 *
 */
public class UserGroupDao extends OfUserGroupDao {

	public UserGroupDao(Configuration configuration) {
		super(configuration);
	}

	public void insert(UserGroup userGroup) {
		Record record = dsl().newRecord(OF_USER_GROUP, userGroup);
		runInTransaction(new Runnable() {
			public void run() {
				dsl().insertInto(OF_USER_GROUP).set(record).execute();
			}
		});
	}

	public UserGroup findById(long groupId, long userId) {
		UserGroup userGroup = dsl().selectFrom(OF_USER_GROUP)
				.where(OF_USER_GROUP.GROUP_ID.eq(groupId)
						.and(OF_USER_GROUP.USER_ID.eq(userId)))
				.fetchOneInto(UserGroup.class);
		return userGroup;
	}

	public void deleteByGroupIdAndUserId(long groupId, long userId) {
		runInTransaction(new Runnable() {
			public void run() {
				dsl().deleteFrom(OF_USER_GROUP)
						.where(OF_USER_GROUP.GROUP_ID.eq(groupId))
						.and(OF_USER_GROUP.USER_ID.eq(userId)).execute();
			}
		});
	}

	public void editByGroupIdAndUserId(long groupId, long userId,
			UserGroupRole role, UserGroupRequestStatus status) {
		runInTransaction(new Runnable() {
			public void run() {
				dsl().update(OF_USER_GROUP)
						.set(OF_USER_GROUP.STATUS_CODE, status.getCode())
						.set(OF_USER_GROUP.ROLE_CODE, role.getCode())
						.where(OF_USER_GROUP.GROUP_ID.eq(groupId)
								.and(OF_USER_GROUP.USER_ID.eq(userId)))
						.execute();
			}
		});
	}

	public UserGroup fetchByGroupIdAndUserId(long groupId, long userId) {
		UserGroup result = dsl().selectFrom(OF_USER_GROUP)
				.where(OF_USER_GROUP.GROUP_ID.eq(groupId)
						.and(OF_USER_GROUP.USER_ID.eq(userId)))
				.fetchOneInto(UserGroup.class);
		return result;
	}

	protected void runInTransaction(Runnable runnable) {
		dsl().transaction(new TransactionalRunnable() {
			public void run(Configuration configuration) throws Exception {
				runnable.run();
			}
		});
	}

	private DSLContext dsl() {
		return DSL.using(configuration());
	}

}
