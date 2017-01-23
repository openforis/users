package org.openforis.users.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;

/**
 * 
 * @author S. Ricci
 *
 */
public class MigrateCollectDatabaseCustomTaskChange implements CustomTaskChange {

	private static final String COLLECT_JNDI_DS_NAME = "jdbc/collectDs";

	/**
	 * For every Collect user insert a user and a private Group owned by the user himself, then insert a public Group and associate it
	 * to the user specifying a role compatible with the Collect one.  
	 */
	@Override
	public void execute(Database database) throws CustomChangeException {
		Connection collectConn = null;
		try {
			collectConn = getCollectConnection();
			if (collectConn == null) {
				//do nothing
				return;
			}
			String schemaPrefix = getCollectSchemaPrefix(collectConn);
			JdbcConnection conn = (JdbcConnection) database.getConnection();
			PreparedStatement insertUserStmt = conn.prepareStatement("INSERT INTO of_users.of_user (username, password, enabled) VALUES (?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);

			PreparedStatement insertGroupStmt = conn.prepareStatement("INSERT INTO of_users.of_group (name, label, enabled, system_defined, visibility_code) VALUES (?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);

			PreparedStatement insertUserGroupStmt = conn.prepareStatement("INSERT INTO of_users.of_user_group (group_id, user_id, status_code, role_code) VALUES (?, ?, ?, ?)");

			long defaultPublicGroupId = insertDefaultPublicGroup(insertGroupStmt);
			
			PreparedStatement selectUsersStmt = collectConn.prepareStatement(String.format("SELECT * FROM %sOFC_USER", schemaPrefix));
			PreparedStatement selectUserRoleStmt = collectConn.prepareStatement(String.format("SELECT role FROM %sOFC_USER_ROLE WHERE user_id = ?", schemaPrefix));

			ResultSet rs = selectUsersStmt.executeQuery();
			while (rs.next()) {
				int collectUserId = rs.getInt("id");
				String username = rs.getString("username");
				String password = rs.getString("password");
				Boolean enabled = rs.getBoolean("enabled");
				
				long newUserId = insertUser(insertUserStmt, username, password, enabled);
				insertPrivateGroup(insertGroupStmt, insertUserGroupStmt, newUserId, username);
				
				insertUserRoleRelation(insertUserGroupStmt, selectUserRoleStmt, defaultPublicGroupId, collectUserId,
						username, newUserId);
			}
			conn.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			closeQuietly(collectConn);
		}
	}

	@Override
	public String getConfirmationMessage() {
		return null;
	}

	@Override
	public void setUp() throws SetupException {
	}

	@Override
	public void setFileOpener(ResourceAccessor resourceAccessor) {
	}

	@Override
	public ValidationErrors validate(Database database) {
		return null;
	}
	
	private void insertUserRoleRelation(PreparedStatement insertUserGroupStmt, PreparedStatement selectUserRoleStmt,
			long defaultPublicGroupId, int collectUserId, String username, long newUserId) throws SQLException {
		selectUserRoleStmt.setInt(1, collectUserId);
		ResultSet userRoleRS = selectUserRoleStmt.executeQuery();
		
		boolean administrator = false;
		boolean analysis = false;
		boolean cleansing = false;
		boolean entry = false;
		boolean view = false;
		
		while(userRoleRS.next()) {
			String role = userRoleRS.getString(1);
			if (role.equals("ROLE_ADMIN")) {
				administrator = true;
			} else if (role.equals("ROLE_ANALYSIS")) {
				analysis = true;
			} else if (role.equals("ROLE_CLEANSING")) {
				cleansing = true;
			} else if (role.equals("ROLE_ENTRY")) {
				entry = true;
			} else if (role.equals("ROLE_VIEW")) {
				view = true;
			}
		}
		String roleCode = 
				"admin".equals(username) ? "OWN"
				: administrator ? "ADM"
				: analysis 		? "ALY" 
				: cleansing 	? "CLE"
				: entry 		? "OPR"
				: view 			? "VWR"
				: null;
		
		insertUserGroupStmt.setLong(1, defaultPublicGroupId);
		insertUserGroupStmt.setLong(2, newUserId);
		insertUserGroupStmt.setString(3, "A");
		insertUserGroupStmt.setString(4, roleCode);
		
		insertUserGroupStmt.executeUpdate();
	}

	private void insertPrivateGroup(PreparedStatement insertGroupStmt, PreparedStatement insertUserGroupStmt,
			long newUserId, String username) throws SQLException {
		insertGroupStmt.setString(1, username + "_private_group");
		insertGroupStmt.setString(2, username + " Private Group");
		insertGroupStmt.setBoolean(3, true);
		insertGroupStmt.setBoolean(4, true);
		insertGroupStmt.setString(5, "PRV");
		
		insertGroupStmt.executeUpdate();
		
		ResultSet privateGroupGeneratedKeys = insertGroupStmt.getGeneratedKeys();
		privateGroupGeneratedKeys.next();
		long privateGroupId = privateGroupGeneratedKeys.getLong(1);
		
		insertUserGroupStmt.setLong(1, privateGroupId);
		insertUserGroupStmt.setLong(2, newUserId);
		insertUserGroupStmt.setString(3, "A");
		insertUserGroupStmt.setString(4, "OWN");
		
		insertUserGroupStmt.executeUpdate();
	}
	
	private long insertDefaultPublicGroup(PreparedStatement insertGroupStmt) throws SQLException {
		insertGroupStmt.setString(1, "default_public_group");
		insertGroupStmt.setString(2, "Default Public Group");
		insertGroupStmt.setBoolean(3, true);
		insertGroupStmt.setBoolean(4, true);
		insertGroupStmt.setString(5, "PUB");
		
		insertGroupStmt.executeUpdate();
		
		ResultSet generatedKeys = insertGroupStmt.getGeneratedKeys();
		generatedKeys.next();
		return generatedKeys.getLong(1);
	}

	private long insertUser(PreparedStatement insertUserStmt, String username, String password, Boolean enabled)
			throws SQLException {
		insertUserStmt.setString(1, username);
		insertUserStmt.setString(2, password);
		insertUserStmt.setBoolean(3, enabled);
		
		insertUserStmt.executeUpdate();
		
		ResultSet userGeneratedKeys = insertUserStmt.getGeneratedKeys();
		userGeneratedKeys.next();
		long newUserId = userGeneratedKeys.getLong(1);
		return newUserId;
	}

	private void closeQuietly(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				//do nothing
			}
		}
	}

	private Connection getCollectConnection() {
		try {
			InitialContext context = new InitialContext();
			Context env = (Context) context.lookup("java:/comp/env/");
			DataSource dataSource = (DataSource) env.lookup(COLLECT_JNDI_DS_NAME);
			Connection connection = dataSource.getConnection();
			return connection;
		} catch (Exception e) {
			return null;
		}
	}
	
	private String getCollectSchemaPrefix(Connection collectConnection) {
		String schemaPrefix = "";
		try {
			schemaPrefix = collectConnection.getMetaData().getDatabaseProductName().equalsIgnoreCase("sqlite") ? "" : "collect.";
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return schemaPrefix;
	}

}
