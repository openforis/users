package org.openforis.users.web;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.jooq.impl.DataSourceConnectionProvider;
import org.openforis.users.db.DbInitializer;
import org.openforis.users.utils.DbUtils;

public class ApplicationInitializerServlet implements Servlet {

	@Override
	public void destroy() {
	}

	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	@Override
	public String getServletInfo() {
		return null;
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		DataSourceConnectionProvider connectionProvider = new DataSourceConnectionProvider(DbUtils.getDataSource());
		new DbInitializer().init(connectionProvider);
	}
}
