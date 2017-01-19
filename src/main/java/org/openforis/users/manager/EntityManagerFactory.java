package org.openforis.users.manager;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.impl.DialectAwareJooqConfiguration;
import org.openforis.users.dao.GroupDao;
import org.openforis.users.dao.ResourceGroupDao;
import org.openforis.users.dao.UserDao;
import org.openforis.users.dao.UserGroupDao;

/**
 * 
 * @author S. Ricci
 *
 */
public class EntityManagerFactory {

	private static EntityManagerFactory instance;
	
	private Configuration config;
	
	private UserDao userDao;
	private GroupDao groupDao;
	private UserGroupDao userGroupDao;
	private ResourceGroupDao resourceGroupDao;
	
	private UserManager userManager;
	private GroupManager groupManager;
	private UserGroupManager userGroupManager;
	private ResourceGroupManager resourceGroupManager;

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
		this.resourceGroupDao = new ResourceGroupDao(config);
		
		//managers
		this.groupManager = new GroupManager(groupDao);
		this.userGroupManager = new UserGroupManager(userGroupDao);
		this.resourceGroupManager = new ResourceGroupManager(resourceGroupDao, groupManager);
		this.userManager = new UserManager(userDao, groupManager, userGroupManager, resourceGroupManager);
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
