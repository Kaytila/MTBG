package net.ck.util.sql;

import net.ck.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RunSQLTester
{
	private static final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(RunSQLTester.class));



	public static void main(String[] args)
	{
		Connection c = null;
		Statement stmt = null;

		try
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:test.db");
			System.out.println("Opened database successfully");

			stmt = c.createStatement();
			String sql = "CREATE TABLE COMPANY " + "(ID INT PRIMARY KEY     NOT NULL,"
					+ " NAME           TEXT    NOT NULL, " + " AGE            INT     NOT NULL, "
					+ " ADDRESS        CHAR(50), " + " SALARY         REAL)";
			stmt.executeUpdate(sql);
			stmt.close();
			c.close();
		} catch (Exception e)
		{
			logger.error(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		logger.error("Table created successfully");
	}

}
