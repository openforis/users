package org.openforis.users.manager;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.openforis.users.model.User;

public class SessionFactoryUtils {

	private static SessionFactory sessionFactory;
	
	static {
		// setup the session factory
		Configuration cfg = new Configuration().addAnnotatedClass(User.class)
				.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
				.setProperty("hibernate.connection.datasource", "java:comp/env/jdbc/of-users-ds")
				.setProperty("hibernate.default_schema", "of_users")
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
