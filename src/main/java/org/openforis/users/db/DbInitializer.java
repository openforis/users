package org.openforis.users.db;

import java.sql.Connection;
import java.sql.Statement;

import org.jooq.ConnectionProvider;
import org.openforis.users.utils.DbUtils;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

public class DbInitializer {
	
	private static final String LIQUIBASE_CHANGELOG_FILE = "org/openforis/users/db/db.changelog-master.xml";
	
	private ConnectionProvider connectionProvider;

	public DbInitializer(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}
	
	public void start() {
		createDbSchema();
		initLiquibase();
	}
	
	private void createDbSchema() {
		Connection conn = null;
		try {
			conn = connectionProvider.acquire();
			Statement stmt = conn.createStatement();
			stmt.execute(String.format("CREATE SCHEMA IF NOT EXISTS %s", DbUtils.SCHEMA_NAME));
			conn.commit();
		} catch (Exception e) {
			throw new RuntimeException("Error creating schema", e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}
	
	private void initLiquibase() {
		Connection conn = null;
		try {
			conn = connectionProvider.acquire();
			Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
			database.setDefaultSchemaName(DbUtils.SCHEMA_NAME);
			Liquibase liquibase = new Liquibase(LIQUIBASE_CHANGELOG_FILE,
					new ClassLoaderResourceAccessor(), database);
			String ctx = null;
			liquibase.update(ctx);
		} catch (Exception e) {
			throw new RuntimeException("Error initializing Liquibase", e);
		} finally {
			DbUtils.closeQuietly(conn);
		}
	}

}
