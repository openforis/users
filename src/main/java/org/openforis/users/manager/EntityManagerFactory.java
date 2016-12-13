package org.openforis.users.manager;

import org.jooq.Configuration;
import org.jooq.ConnectionProvider;
import org.jooq.impl.DialectAwareJooqConfiguration;
import org.openforis.users.generated.tables.daos.UserDao;

public class EntityManagerFactory {

	private static EntityManagerFactory instance;
	
	private Configuration config;
	private UserDao userDao;
	private UserManager userManager;

	private ConnectionProvider connectionProvider;

	public EntityManagerFactory(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	public UserManager getUserManager() {
		if (userManager == null) {
			userManager = new UserManager(getUserDao());
		}
		return userManager;
	}
	
	private UserDao getUserDao() {
		if (userDao == null) {
			userDao = new UserDao(getConfiguration());
		}
		return userDao;
	}

	private Configuration getConfiguration() {
		if (config == null) {
			config = new DialectAwareJooqConfiguration(connectionProvider);
		}
		return config;
	}

	public static void init(ConnectionProvider connectionProvider) {
		instance = new EntityManagerFactory(connectionProvider);
	}
	
	public static EntityManagerFactory getInstance() {
		if (instance == null) {
			throw new IllegalStateException("Connection provider not initialized");
		}
		return instance;
	}
	
}
