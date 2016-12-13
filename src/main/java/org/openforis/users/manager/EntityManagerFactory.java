package org.openforis.users.manager;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.impl.DialectAwareJooqConfiguration;
import org.openforis.users.jooq.tables.daos.UserDao;

public class EntityManagerFactory {

	private static EntityManagerFactory instance;
	
	private Configuration config;
	private UserDao userDao;
	private UserManager userManager;

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
		
		//DAOs
		this.userDao = new UserDao(config);
		
		//managers
		this.userManager = new UserManager(userDao);
	}

	public UserManager getUserManager() {
		return userManager;
	}
	
}
