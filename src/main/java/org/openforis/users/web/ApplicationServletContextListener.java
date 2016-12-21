package org.openforis.users.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.jooq.impl.DataSourceConnectionProvider;
import org.openforis.users.db.DbInitializer;
import org.openforis.users.manager.EntityManagerFactory;
import org.openforis.users.utils.DbUtils;

public class ApplicationServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DataSourceConnectionProvider connectionProvider = new DataSourceConnectionProvider(DbUtils.getDataSource());
		new DbInitializer(connectionProvider).start();
		EntityManagerFactory.init(connectionProvider);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
