package org.openforis.users.manager;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.impl.DialectAwareJooqConfiguration;
import org.openforis.users.dao.GroupDao;
import org.openforis.users.dao.UserDao;
import org.openforis.users.dao.UserGroupDao;

public class EntityManagerFactory {

	private static EntityManagerFactory instance;
	
	private Configuration config;
	
	private UserDao userDao;
	private GroupDao groupDao;
	private UserGroupDao userGroupDao;
	
	private UserManager userManager;
	private GroupManager groupManager;
	private UserGroupManager userGroupManager;

	public static void init(ConnectionProvider connectionProvider) {
		instance = new EntityManagerFactory(connectionProvider);
	}
	
	public static EntityManagerFactory getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Connection provider not initialized");
		}
		return instance;
	}
	
	public EntityManagerFactory(ConnectionProvider connectionProvider) {
		this.config = new DialectAwareJooqConfiguration(connectionProvider);
		this.config.settings().setUpdatablePrimaryKeys(true);
		
		//DAOs
		this.groupDao = new GroupDao(config);
		this.userDao = new UserDao(config);
		this.userGroupDao = new UserGroupDao(config);
		
		//managers
		this.groupManager = new GroupManager(groupDao);
		this.userGroupManager = new UserGroupManager(userGroupDao);
		this.userManager = new UserManager(groupManager, userGroupManager, userDao);
	}

	public UserManager getUserManager() {
		return userManager;
	}
	
	public GroupManager getGroupManager() {
		return groupManager;
	}
	
	public UserGroupManager getUserGroupManager() {
		return userGroupManager;
	}
	
}
