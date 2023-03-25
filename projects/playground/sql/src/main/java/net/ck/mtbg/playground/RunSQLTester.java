package net.ck.mtbg.playground;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class RunSQLTester
{
	public static void main(String[] args) throws Throwable
	{
		Connection c = null;
		Statement stmt = null;

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
	}
}
