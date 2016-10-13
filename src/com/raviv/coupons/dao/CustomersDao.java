package com.raviv.coupons.dao;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.raviv.coupons.beans.Customer;
import com.raviv.coupons.dao.interfaces.ICustomersDao;
import com.raviv.coupons.dao.utils.JdbcTransactionManager;
import com.raviv.coupons.dao.utils.JdbcUtils;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;

public class CustomersDao extends InfraDao implements ICustomersDao {

	public CustomersDao() 
	{
		super();
	}
	
	public CustomersDao(JdbcTransactionManager jdbcTransactionManager) {
		super(jdbcTransactionManager);
	}

	@Override
	public void createCustomer(Customer customer) throws ApplicationException {
	
		PreparedStatement 	preparedStatement	= null;
		Connection 			connection 			= null;
		ResultSet 			generatedKeys		= null;
		try 
		{
			// Getting a connection from the connections manager or Transaction manager
			connection = super.getConnection();


			// Creating a statement object which holds the SQL we're about to execute
			String sql;
			
			sql = "INSERT INTO CUSTOMERS (";
			sql += "	 CREATED_BY_USER_ID";
			sql += "	,UPDATED_BY_USER_ID";
			sql += "	,USER_ID";
			sql += "	,CUSTOMER_NAME";
			sql += ")";
			sql += "VALUES (?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

			// Replacing question marks with the values inside  the bean.
			preparedStatement.setInt	(1 ,	customer.getCreatedByUserId() );
			preparedStatement.setInt	(2 ,	customer.getUpdatedByUserId() );
			preparedStatement.setInt	(3 ,	customer.getUserId() );
			preparedStatement.setString	(4 ,	customer.getCustomerName() );


			// executeUpdate is a method used in order to : insert, delete, update (not get)
			preparedStatement.executeUpdate();

			// Handle generated keys...
			generatedKeys = preparedStatement.getGeneratedKeys();
	        if ( generatedKeys.next() ) 
	        {
	        	int customerId = generatedKeys.getInt("GENERATED_KEYS");
	        	
	        	customer.setCustomerId( customerId );
	        }
	        
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "Failed to create customer due to :" + e.getMessage() );
		} 
		finally 
		{
			if ( super.isJdbcTransactionManagerInUse() )
			{
				// Transaction manager will close the connection later.
				JdbcUtils.closeResources(preparedStatement, generatedKeys);
			}
			else
			{
				// We do not have transaction manager.
				JdbcUtils.closeResources(connection, preparedStatement, generatedKeys);
			}	
		}
		
	}
	
	@Override
	public Customer getCustomer(long customerId) throws ApplicationException 
	{
		Connection 			connection 			= null;
		PreparedStatement 	preparedStatement 	= null;		
		ResultSet 			resultSet 			= null;
		Customer 			returnObj 			= new Customer();

		// getting the specific entry by the input id
		// storing it into resultSet
		try 
		{
			// Getting a connection from the connections manager or Transaction manager
			connection = super.getConnection();
			
			String sql = "SELECT * FROM CUSTOMERS WHERE CUSTOMER_ID = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, customerId);
			
			// execute query
			resultSet = preparedStatement.executeQuery();
	
			// Not found ...
			if ( !resultSet.next() ) { return null; } 
			
			//extract bean from result Set
			returnObj.setCustomerId     	( resultSet.getLong      ( "CUSTOMER_ID" ) );
			returnObj.setSysCreationDate	( resultSet.getTimestamp ( "SYS_CREATION_DATE" ).getTime() );
			returnObj.setSysUpdateDate  	( resultSet.getTimestamp ( "SYS_UPDATE_DATE" ).getTime() );
			returnObj.setCreatedByUserId	( resultSet.getInt       ( "CREATED_BY_USER_ID" ) );
			returnObj.setUpdatedByUserId	( resultSet.getInt       ( "UPDATED_BY_USER_ID" ) );
			returnObj.setUserId         	( resultSet.getInt       ( "USER_ID" ) );
			returnObj.setCustomerName   	( resultSet.getString    ( "CUSTOMER_NAME" ) );			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "Failed to get coupon with id : " + customerId + " " + e.getMessage());
		} 
		finally 
		{
			if ( super.isJdbcTransactionManagerInUse() )
			{
				// Transaction manager will close the connection later.
				JdbcUtils.closeResources(preparedStatement, resultSet);
			}
			else
			{
				// We do not have transaction manager.
				JdbcUtils.closeResources(connection, preparedStatement, resultSet);
			}	
		}
		
		return returnObj;
}

	@Override
	public void updateCustomer(Customer customer) throws ApplicationException 
	{	
		PreparedStatement 	preparedStatement	= null;
		Connection 			connection 			= null;

		try 
		{
			// Getting a connection from the connections manager or Transaction manager
			connection = super.getConnection();

			// Creating a statement object which holds the SQL we're about to execute
			String sql;
			
			sql = "UPDATE CUSTOMERS SET ";
			sql += "	 SYS_UPDATE_DATE           = ?";
			sql += "	,UPDATED_BY_USER_ID        = ? ";
			sql += "	,USER_ID                   = ? ";
			sql += "	,CUSTOMER_NAME             = ? ";
			sql += "WHERE";
			sql += "      	CUSTOMER_ID = ? ";
			
			preparedStatement = connection.prepareStatement(sql);

			// Replacing question marks with the beans values.
			preparedStatement.setTimestamp	(1 ,	 new Timestamp ( System.currentTimeMillis() ) );
			preparedStatement.setInt		(2 ,	customer.getUpdatedByUserId() );
			preparedStatement.setInt		(3 ,	customer.getUserId() );
			preparedStatement.setString		(4 ,	customer.getCustomerName() );
			preparedStatement.setLong		(5 ,	customer.getCustomerId() );


			// executeUpdate is a method used in order to : insert, delete, update (not get)
			preparedStatement.executeUpdate();	        
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "Failed to create customer due to :" + e.getMessage() );
		} 
		finally 
		{
			if ( super.isJdbcTransactionManagerInUse() )
			{
				// Transaction manager will close the connection later.
				JdbcUtils.closeResources(preparedStatement);
			}
			else
			{
				// We do not have transaction manager.
				JdbcUtils.closeResources(connection, preparedStatement);
			}
		}
	}// updateCustomer
	
	@Override
	public void deleteCustomer(long customerId) {
		// TODO Auto-generated method stub
		
	}

}
