package org.openforis.users.manager;

import java.sql.Connection;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.openforis.users.model.User;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class SessionFactoryUtils {

	private static final String DB_JNDI_RESOURCE_NAME = "java:comp/env/jdbc/of-users-ds";
	private static final String SCHEMA_NAME = "of_users";
	
	private static final SessionFactory sessionFactory;
	
	static {
		createDbSchema();
		
		initLiquibase();
		
		// setup the session factory
		Configuration cfg = new Configuration().addAnnotatedClass(User.class)
				.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
				.setProperty("hibernate.connection.datasource", DB_JNDI_RESOURCE_NAME)
				.setProperty("hibernate.default_schema", SCHEMA_NAME)
				.setProperty("hibernate.order_updates", "true")
				.setProperty("hibernate.current_session_context_class", "thread")
		// .setProperty("hibernate.hbm2ddl.auto", "create")
		;
		sessionFactory = cfg.buildSessionFactory();
	}

	private static void createDbSchema() {
		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(String.format("CREATE SCHEMA IF NOT EXISTS %s", SCHEMA_NAME));
			conn.commit();
		} catch (Exception e) {
			throw new RuntimeException("Error creating schema", e);
		}
	}
	
	private static void initLiquibase() {
		try {
			Connection conn = getConnection();
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
			database.setDefaultSchemaName(SCHEMA_NAME);
			Liquibase liquibase = new Liquibase("org/openforis/users/db/db.changelog-0000.xml",
					new ClassLoaderResourceAccessor(), database);
			String ctx = null;
			liquibase.update(ctx);
		} catch (Exception e) {
			throw new RuntimeException("Error initializing Liquibase", e);
		}
	}
	
	private static Connection getConnection() {
		try {
			Context initialContext = new InitialContext();
			DataSource datasource = (DataSource) initialContext.lookup(DB_JNDI_RESOURCE_NAME);
			return datasource.getConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
