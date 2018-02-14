package org.openforis.users.dao;

import static org.openforis.users.jooq.tables.OfUserToken.OF_USER_TOKEN;

import java.sql.Timestamp;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.TransactionalRunnable;
import org.jooq.impl.DSL;
import org.openforis.users.jooq.tables.daos.OfUserTokenDao;
import org.openforis.users.model.UserToken;

public class UserTokenDao extends OfUserTokenDao {

	public UserTokenDao(Configuration configuration) {
		super(configuration);
	}

	public void insert(UserToken userToken) {
		insert(userToken.getUserId(), userToken.getToken(),
				userToken.getTokenDatetime());
	}

	public void insert(long userId, String token, Timestamp tokenDatetime) {
		runInTransaction(new Runnable() {
			public void run() {
				dsl().insertInto(OF_USER_TOKEN, OF_USER_TOKEN.USER_ID,
						OF_USER_TOKEN.TOKEN, OF_USER_TOKEN.TOKEN_DATETIME)
						.values(userId, token, tokenDatetime).execute();
			}
		});
	}

	public void deleteByUserIdAndToken(long userId, String token) {
		runInTransaction(new Runnable() {
			public void run() {
				dsl().deleteFrom(OF_USER_TOKEN)
						.where(OF_USER_TOKEN.USER_ID.eq(userId))
						.and(OF_USER_TOKEN.TOKEN.eq(token)).execute();
			}
		});
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
