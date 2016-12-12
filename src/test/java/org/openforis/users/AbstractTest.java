package org.openforis.users;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.openforis.users.model.User;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

public abstract class AbstractTest {

	private static final String DB_URL = "jdbc:h2:mem:test";
	private static final String SCHEMA = "of_users";
	
	SessionFactory sessionFactory;
	Session session = null;

	public AbstractTest() {
		super();
	}

	@Before
	public void before() throws SQLException, LiquibaseException {
		createDbSchema();
		
		initLiquibase();

		// setup the session factory
		Configuration cfg = new Configuration().addAnnotatedClass(User.class)
				.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
				.setProperty("hibernate.connection.driver_class", "org.h2.Driver")
				.setProperty("hibernate.connection.url", DB_URL)
				.setProperty("hibernate.default_schema", SCHEMA)
				.setProperty("hibernate.order_updates", "true")
		// .setProperty("hibernate.hbm2ddl.auto", "create")
		;
		
		sessionFactory = cfg.buildSessionFactory();
		session = sessionFactory.openSession();

	}

	private void createDbSchema() throws SQLException {
		Connection conn = DriverManager.getConnection(DB_URL, null, null);
		Statement stmt = conn.createStatement();
		stmt.execute(String.format("CREATE SCHEMA %s", SCHEMA));
		conn.commit();
	}

	private void initLiquibase() throws SQLException, DatabaseException, LiquibaseException {
		Properties info = new Properties();
		// info.setProperty("user", "liquibase");
		// info.setProperty("password", "test");
		org.h2.jdbc.JdbcConnection h2Conn = new org.h2.jdbc.JdbcConnection(DB_URL, info);
		Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(h2Conn));
		database.setDefaultSchemaName(SCHEMA);
		Liquibase liquibase = new Liquibase("org/openforis/users/db/db.changelog-master.xml",
				new ClassLoaderResourceAccessor(), database);
		String ctx = null;
		liquibase.update(ctx);
	}

	@After
	public void after() {
		session.close();
		sessionFactory.close();
	}

}