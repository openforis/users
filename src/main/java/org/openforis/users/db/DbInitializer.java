package org.openforis.users.db;

import java.sql.Connection;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.openforis.users.utils.DbUtils;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class DbInitializer {

	public void init() {
		createDbSchema();
		initLiquibase();
	}
	
	private void createDbSchema() {
		try {
			Connection conn = getConnection();
			Statement stmt = conn.createStatement();
			stmt.execute(String.format("CREATE SCHEMA IF NOT EXISTS %s", DbUtils.SCHEMA_NAME));
			conn.commit();
		} catch (Exception e) {
			throw new RuntimeException("Error creating schema", e);
		}
	}
	
	private void initLiquibase() {
		try {
			Connection conn = getConnection();
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
			database.setDefaultSchemaName(DbUtils.SCHEMA_NAME);
			Liquibase liquibase = new Liquibase("org/openforis/users/db/db.changelog-master.xml",
					new ClassLoaderResourceAccessor(), database);
			String ctx = null;
			liquibase.update(ctx);
		} catch (Exception e) {
			throw new RuntimeException("Error initializing Liquibase", e);
		}
	}
	
	private Connection getConnection() {
		try {
			Context initialContext = new InitialContext();
			DataSource datasource = (DataSource) initialContext.lookup(DbUtils.DB_JNDI_RESOURCE_NAME);
			return datasource.getConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
