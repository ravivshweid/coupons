package com.raviv.coupons.dao.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtils {

/*
	static
	{
		try {
			Class.forName("com.sqlserver.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
*/

	public static Connection getConnection() throws SQLException{
		
		Connection connection;
		
		String serverName 	="//localhost";
		String databaseName	="Coupons";
		String databaseUser	="sa";
		String databasePswd	="sa";
		
		String conUrl = "jdbc:sqlserver:" + serverName + "; databaseName=" + databaseName + "; user=" + databaseUser + "; password=" + databasePswd + ";";
		connection = DriverManager.getConnection(conUrl);
		
		
 		return connection;
	}

	public static void closeConnection(Connection connection)
	{
		try 
		{
			if ( connection != null )
			{
				connection.close();
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Write to log that we have a resource leak
		}
	}
	
	public static void closePreparedStatement(PreparedStatement preparedStatement)
	{	
		try 
		{
			if ( preparedStatement != null )
			{
				preparedStatement.close();
			}
		} 
		catch (SQLException e) 
		{
			// Write to log that we have a resource leak
			e.printStackTrace();
		}
	}

	public static void closeResultSet( ResultSet resultSet) 
	{
		try 
		{
			if ( resultSet != null )
			{
				resultSet.close();
			}
		} 
		catch ( SQLException e ) 
		{
			// Write to log that we have a resource leak
			e.printStackTrace();
		}

	}

	
	
	public static void closeResources(Connection connection)
	{
		closeConnection(connection);
	}

	public static void closeResources(PreparedStatement preparedStatement) 
	{
		closePreparedStatement	( preparedStatement );	
	}

	
	public static void closeResources(Connection connection, PreparedStatement preparedStatement)
	{	
		closeConnection         ( connection        );
		closePreparedStatement	( preparedStatement );	
	}

	public static void closeResources(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) 
	{
		closeConnection         ( connection        );
		closePreparedStatement	( preparedStatement );	
		closeResultSet			( resultSet			);	
	}

	public static void closeResources(PreparedStatement preparedStatement, ResultSet resultSet) 
	{
		closePreparedStatement	( preparedStatement );	
		closeResultSet			( resultSet			);	
	}

}

