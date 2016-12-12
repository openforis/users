package org.openforis.users.manager;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.openforis.users.model.User;
import org.openforis.users.utils.DbUtils;

public class SessionFactoryUtils {
	
	private static final SessionFactory sessionFactory;
	
	static {
		// setup the session factory
		Configuration cfg = new Configuration().addAnnotatedClass(User.class)
				.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
				.setProperty("hibernate.connection.datasource", DbUtils.DB_JNDI_RESOURCE_NAME)
				.setProperty("hibernate.default_schema", DbUtils.SCHEMA_NAME)
				.setProperty("hibernate.order_updates", "true")
				.setProperty("hibernate.current_session_context_class", "thread")
		// .setProperty("hibernate.hbm2ddl.auto", "create")
		;
		sessionFactory = cfg.buildSessionFactory();
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
