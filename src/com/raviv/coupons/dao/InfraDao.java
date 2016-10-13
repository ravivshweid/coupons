package com.raviv.coupons.dao;

import java.sql.Connection;
import java.sql.SQLException;
import com.raviv.coupons.dao.utils.JdbcTransactionManager;
import com.raviv.coupons.dao.utils.JdbcUtils;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;

public class InfraDao {

	protected JdbcTransactionManager jdbcTransactionManager;

	protected Connection getConnection() throws ApplicationException
	{
		if ( isJdbcTransactionManagerInUse() )
		{
			// Getting a connection from the Transaction manager
			return this.jdbcTransactionManager.getConnection();
		}
		else
		{
			// Getting a connection from the connections manager (getConnection is a static method)
			try 
			{
				return JdbcUtils.getConnection();
			} 
			catch (SQLException e) 
			{
				//e.printStackTrace();
				throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "JdbcUtils, Failed to get connection :" + e.getMessage() );
			}
		}
	
	}

	protected boolean isJdbcTransactionManagerInUse()
	{
		if ( this.jdbcTransactionManager == null )
		{
			return false;
		}
	
		return true;
	}
	
	public 		InfraDao() {
		super();
		this.jdbcTransactionManager = null;
	}
	
	public 		InfraDao(JdbcTransactionManager jdbcTransactionManager) {
		super();
		this.jdbcTransactionManager = jdbcTransactionManager;
	}
	
}
