/**
 * This class is generated by jOOQ
 */
package org.openforis.users.jooq.tables.daos;


import java.util.List;

import javax.annotation.Generated;

import org.jooq.Configuration;
import org.jooq.impl.DAOImpl;
import org.openforis.users.jooq.tables.User;
import org.openforis.users.jooq.tables.records.UserRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.6.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserDao extends DAOImpl<UserRecord, org.openforis.users.jooq.tables.pojos.User, Long> {

	/**
	 * Create a new UserDao without any configuration
	 */
	public UserDao() {
		super(User.USER, org.openforis.users.jooq.tables.pojos.User.class);
	}

	/**
	 * Create a new UserDao with an attached configuration
	 */
	public UserDao(Configuration configuration) {
		super(User.USER, org.openforis.users.jooq.tables.pojos.User.class, configuration);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Long getId(org.openforis.users.jooq.tables.pojos.User object) {
		return object.getId();
	}

	/**
	 * Fetch records that have <code>ID IN (values)</code>
	 */
	public List<org.openforis.users.jooq.tables.pojos.User> fetchById(Long... values) {
		return fetch(User.USER.ID, values);
	}

	/**
	 * Fetch a unique record that has <code>ID = value</code>
	 */
	public org.openforis.users.jooq.tables.pojos.User fetchOneById(Long value) {
		return fetchOne(User.USER.ID, value);
	}

	/**
	 * Fetch records that have <code>USERNAME IN (values)</code>
	 */
	public List<org.openforis.users.jooq.tables.pojos.User> fetchByUsername(String... values) {
		return fetch(User.USER.USERNAME, values);
	}

	/**
	 * Fetch a unique record that has <code>USERNAME = value</code>
	 */
	public org.openforis.users.jooq.tables.pojos.User fetchOneByUsername(String value) {
		return fetchOne(User.USER.USERNAME, value);
	}

	/**
	 * Fetch records that have <code>PASSWORD IN (values)</code>
	 */
	public List<org.openforis.users.jooq.tables.pojos.User> fetchByPassword(String... values) {
		return fetch(User.USER.PASSWORD, values);
	}

	/**
	 * Fetch records that have <code>ENABLED IN (values)</code>
	 */
	public List<org.openforis.users.jooq.tables.pojos.User> fetchByEnabled(Boolean... values) {
		return fetch(User.USER.ENABLED, values);
	}
}
