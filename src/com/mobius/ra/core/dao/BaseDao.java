package com.mobius.ra.core.dao;

/**
 * 
 */

import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import com.mobius.ra.core.common.Constants;
import com.mobius.ra.core.common.Tools;

/**
 * @author Daniel.liu
 * @date June 25, 2012
 * @version v 1.0
 */
public class BaseDao {

	// connection
	private Connection conn = null;
	// callableStatement
	private CallableStatement cstmt = null;

	public String dbAlias = null;

	public String prefix = "proxool.";

	// prepared statment
	private PreparedStatement prepstmt = null;

	// statment
	private Statement stmt = null;

	/**
	 * BaseDao
	 * 
	 */
	public BaseDao(String dbAlias) {
		try {
			this.dbAlias = dbAlias;
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			conn = DriverManager.getConnection(prefix + dbAlias);
			stmt = conn.createStatement();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BaseDao(String dbAlias, boolean isAutoCommit) {
		try {
			this.dbAlias = dbAlias;
			Class.forName("org.logicalcobwebs.proxool.ProxoolDriver");
			conn = DriverManager.getConnection(prefix + dbAlias);
			conn.setAutoCommit(isAutoCommit);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * add batch sql
	 * 
	 * @throws SQLException
	 */
	public void addBatch() throws SQLException {
		if (prepstmt != null)
			prepstmt.addBatch();
	}

	public void clearParameters() throws SQLException {
		prepstmt.clearParameters();
	}

	/**
	 * close connection
	 * 
	 * @throws SQLException
	 */
	public void close() throws SQLException {
		if (stmt != null) {
			stmt.close();
			stmt = null;
		}
		if (prepstmt != null) {
			prepstmt.close();
			prepstmt = null;
		}
		if (cstmt != null) {
			cstmt.close();
			cstmt = null;
		}
		conn.close();
		conn = null;
	}

	/**
	 * submit annually.
	 * 
	 * @throws SQLException
	 */
	public void conCommit() throws SQLException {
		if (conn != null) {
			conn.commit();
		}
	}

	/**
	 * execute batch update for prepared statement.
	 * 
	 * @throws SQLException
	 */
	public void exeBatchUpdate() throws SQLException {
		if (prepstmt != null)
			prepstmt.executeBatch();
	}

	/**
	 * execute batch update
	 * 
	 * @throws SQLException
	 */
	public void executeBatchUpdate() throws SQLException {
		if (stmt != null) {
			stmt.executeBatch();
		}
	}

	/**
	 * execute callableStatment
	 * 
	 * @param sql
	 * @throws SQLException
	 */
	public void executeCallableStatemen(String sql) throws SQLException {
		if (cstmt != null) {
			cstmt.close();
			cstmt = null;
		}
		cstmt = conn.prepareCall(sql);
	}

	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery() throws SQLException {
		if (prepstmt != null) {
			return prepstmt.executeQuery();
		} else
			return null;
	}

	/**
	 * execute sql query
	 * 
	 * @param sql
	 * @return ResultSet
	 */
	public ResultSet executeQuery(String sql) throws SQLException {
		if (stmt != null) {
			return stmt.executeQuery(sql);
		} else
			return null;
	}

	/**
	 * 
	 * @throws SQLException
	 */
	public void executeUpdate() throws SQLException {
		if (prepstmt != null)
			prepstmt.executeUpdate();
	}

	/**
	 * execute sql update
	 * 
	 * @param sql
	 */
	public void executeUpdate(String sql) throws SQLException {
		if (stmt != null)
			stmt.executeUpdate(sql);
	}

	/**
	 * get connection
	 * 
	 * @return Connection
	 */
	public Connection getConnection() {
		return conn;
	}

	public PreparedStatement getPreparedStatement() {
		return prepstmt;
	}

	public Statement getStatement() {
		return stmt;
	}

	/**
	 * PreparedStatement
	 * 
	 * @return sql
	 */
	public void prepareStatement(String sql) throws SQLException {
		if (prepstmt != null) {
			prepstmt.close();
			prepstmt = null;
		}
		if (!Tools.isProductEnv()) {
			if (sql.contains("select") && !sql.contains("limit")) {
				sql = sql + " limit " + Constants.TEST_ENV_SELECT_LIMIT;
			}
		}
		prepstmt = conn.prepareStatement(sql);
	}

	/**
	 * realse stmt and prepstmt
	 * 
	 * @throws SQLException
	 */
	public void releaseStmt() throws SQLException {
		if (stmt != null) {
			stmt.close();
			stmt = null;
			stmt = conn.createStatement();
		}
		if (prepstmt != null) {
			prepstmt.close();
			prepstmt = null;
		}
	}

	public void setBinaryStream(int index, InputStream in, int length) throws SQLException {
		prepstmt.setBinaryStream(index, in, length);
	}

	public void setBoolean(int index, boolean value) throws SQLException {
		prepstmt.setBoolean(index, value);
	}

	public void setDate(int index, Date date) throws SQLException {
		prepstmt.setDate(index, (java.sql.Date) date);
	}

	public void setDouble(int index, double value) throws SQLException {
		prepstmt.setDouble(index, value);
	}

	public void setFloat(int index, float value) throws SQLException {
		prepstmt.setFloat(index, value);
	}

	public void setInt(int index, int value) throws SQLException {
		prepstmt.setInt(index, value);
	}

	public void setLong(int index, long value) throws SQLException {
		prepstmt.setLong(index, value);
	}

	/**
	 * set String
	 * 
	 * @param index
	 * @param value
	 */
	public void setString(int index, String value) throws SQLException {
		if (value == null) {
			value = "";
		}
		prepstmt.setString(index, value);
	}

}
