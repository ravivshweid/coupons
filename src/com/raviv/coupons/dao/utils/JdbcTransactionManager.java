package com.raviv.coupons.dao.utils;

import java.sql.Connection;
import java.sql.SQLException;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.tests.PrintUtils;

public class JdbcTransactionManager  {
	
	private Connection 			connection;
	
	public Connection getConnection() throws ApplicationException {
		
		if ( this.connection == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null , "TransactionManager getConnection: connection is null." );			
		}
		
		return connection;
	}

	public JdbcTransactionManager() throws ApplicationException 
	{
		try 
		{
			// Getting a connection from the connections manager (getConnection is a static method)
			this.connection = JdbcUtils.getConnection();
			this.connection.setAutoCommit(false);    // commit will executed on demand
		} 
		catch (SQLException e) 
		{
			//e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "TransactionManager, Failed to get connection :" + e.getMessage() );
		}	
	}
	
	public void commit() throws ApplicationException
	{
		try 
		{
			this.connection.commit();
			closeConnection();
		} 
		catch (SQLException e) 
		{
			//e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "TransactionManager, Failed to commit transaction :" + e.getMessage() );			
		}
	}
	
	public void rollback() throws ApplicationException
	{		
		PrintUtils.printHeader("ROLLBACK TRANSACTION");
		
		try 
		{
			this.connection.rollback();
			closeConnection();
		} 
		catch (SQLException e) 
		{
			//e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "TransactionManager, Failed to rollback transaction :" + e.getMessage() );			
		}
	}

	public void closeConnection()
	{
		// Close connection with JdbcUtils.closeResources
		JdbcUtils.closeResources( this.connection );
		connection = null;
	}

}
