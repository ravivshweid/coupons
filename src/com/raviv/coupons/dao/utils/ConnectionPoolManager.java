package com.raviv.coupons.dao.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ConnectionPoolManager {

	
	static Object mutex = new Object();
	
	//private static JdbcConnectionPoolManager jdbcConnectionPoolManager = new JdbcConnectionPoolManager();
	private static List<Connection> connectionsPool = new LinkedList <Connection>();

	private static ConnectionPoolManager singleToneinstance;
	
	static 	{ singleToneinstance = new ConnectionPoolManager(); }

	/**
	 * private contractor, single tone class
	 */
	private ConnectionPoolManager(){}

	/**
	 * returns an instance of the ConnectionPoolManager
	 * 
	 * @throws Exception
	 * @return an instance for the ConnectionPool singleton
	 * */
	public static ConnectionPoolManager 	getInstance()  {
		return singleToneinstance;
	}
		
	
	public	Connection	getConnection() throws SQLException{
		Connection connection;
		
		synchronized (mutex)
		{
			if ( connectionsPool.isEmpty() )
			{
				//  connectionsPool is empty, lets create a new connection ...
				String conUrl = "jdbc:sqlserver://localhost; databaseName=Coupons; user=sa; password=sa;";
				connection = DriverManager.getConnection(conUrl);
		 		return connection;
			}
		}
		
		//  connectionsPool is not empty, lets take one and use it ...
		connection = connectionsPool.get(0);
		connectionsPool.remove(0);
		return connection;

			
	} // end getConnection

	public	void	returnConnection(Connection connection) {	
		if ( connection != null )
		{
			connectionsPool.add(connection);
		}
	} // end returnConnection

	public	void	closeAllConnections() throws SQLException {	
		
		for (Connection con : connectionsPool)
		{
			if (con != null ) 
			{
				if ( con.isValid(0) )
				{
					con.close();
				}
			}
		}
		
		connectionsPool.clear();
		
	} // end closeAllConnections

	/*
	private  void closeConnection(Connection connection)
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
		*/
	
	private void closePreparedStatement(PreparedStatement preparedStatement)
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

	private void closeResultSet( ResultSet resultSet) 
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

	public  void closeResources( Connection connection)
	{
		returnConnection(connection);
	}

	public  void closeResources( PreparedStatement preparedStatement) 
	{
		closePreparedStatement	( preparedStatement );	
	}

	public  void closeResources( Connection connection, PreparedStatement preparedStatement)
	{	
		returnConnection        ( connection        );
		closePreparedStatement	( preparedStatement );	
	}

	public  void closeResources( Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) 
	{
		returnConnection        ( connection        );
		closePreparedStatement	( preparedStatement );	
		closeResultSet			( resultSet			);	
	}

	public  void closeResources( PreparedStatement preparedStatement, ResultSet resultSet) 
	{
		closePreparedStatement	( preparedStatement );	
		closeResultSet			( resultSet			);	
	}

}
