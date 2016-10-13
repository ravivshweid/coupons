package com.raviv.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.raviv.coupons.beans.Company;
import com.raviv.coupons.dao.interfaces.ICompanysDao;
import com.raviv.coupons.dao.utils.JdbcTransactionManager;
import com.raviv.coupons.dao.utils.JdbcUtils;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;

public class CompanysDao extends InfraDao implements ICompanysDao {
	
	public CompanysDao() 
	{
		super();
	}
	
	public CompanysDao(JdbcTransactionManager jdbcTransactionManager) {
		super(jdbcTransactionManager);
	}

	@Override
	public void createCompany(Company company) throws ApplicationException {
		
		PreparedStatement 	preparedStatement	= null;
		Connection 			connection 			= null;
		ResultSet 			generatedKeys		= null;

		try 
		{
			// Getting a connection from the connections manager or Transaction manager
			connection = super.getConnection();
				
			// Creating a statement object which holds the SQL we're about to execute
			String sql;
			
			sql = "INSERT INTO COMPANYS (";
			sql += "	 CREATED_BY_USER_ID";
			sql += "	,UPDATED_BY_USER_ID";
			sql += "	,USER_ID";
			sql += "	,COMPANY_NAME";
			sql += "	,COMPANY_EMAIL";
			sql += ")";
			sql += "VALUES (?, ?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

			// Replacing question mark with the value inside bean
			preparedStatement.setInt	(1 ,	company.getCreatedByUserId() );
			preparedStatement.setInt	(2 ,	company.getUpdatedByUserId() );
			preparedStatement.setInt	(3 ,	company.getUserId() );
			preparedStatement.setString	(4 ,	company.getCompanyName() );
			preparedStatement.setString	(5 ,	company.getCompanyEmail() );
			
			//preparedStatement.setString(2, coupon.getCouponType().getName());


			// executeUpdate is a method used in order to : insert, delete, update (not get)
			preparedStatement.executeUpdate();

			// Handle generated keys...
			generatedKeys = preparedStatement.getGeneratedKeys();			
	        if ( generatedKeys.next() ) 
	        {
	        	long identityId = generatedKeys.getLong("GENERATED_KEYS");
	        	company.setCompanyId( identityId );
	        }
	        else 
	        {
	            throw new SQLException("Creating coupon failed, no ID obtained.");
	        }
	        
		} 
		catch (SQLException e) 
		{
			//e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "Failed to create company due to :" + e.getMessage() );
		} 
		finally 
		{
			if ( super.isJdbcTransactionManagerInUse() )
			{	
				// We will close connection with Transaction manager
				JdbcUtils.closeResources(preparedStatement, generatedKeys);
			}
			else
			{
				JdbcUtils.closeResources(connection, preparedStatement, generatedKeys);
			}
		}
	}
	
	@Override
	public Company getCompany(long companyId) throws ApplicationException {
		Connection 			connection 			= null;
		PreparedStatement 	preparedStatement 	= null;		
		ResultSet 			resultSet 			= null;
		Company 			returnObj 			= new Company();

	// getting the specific entry by the input id
	// storing it into resultSet
		try 
		{
			// Getting a connection from the connections manager or Transaction manager
			connection = super.getConnection();
			
			String sql = "SELECT * FROM COMPANYS WHERE COMPANY_ID = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, companyId);
			
			// execute query
			resultSet = preparedStatement.executeQuery();
	
			// Not found ...
			if ( !resultSet.next() ) { return null; } 
			
			
			//extract bean from result Set			
			returnObj.setCompanyId      	( resultSet.getLong      ( "COMPANY_ID" ) );
			returnObj.setSysCreationDate	( resultSet.getTimestamp ( "SYS_CREATION_DATE" ).getTime() );
			returnObj.setSysUpdateDate  	( resultSet.getTimestamp ( "SYS_UPDATE_DATE" ).getTime() );
			returnObj.setCreatedByUserId	( resultSet.getInt       ( "CREATED_BY_USER_ID" ) );
			returnObj.setUpdatedByUserId	( resultSet.getInt       ( "UPDATED_BY_USER_ID" ) );
			returnObj.setUserId         	( resultSet.getInt       ( "USER_ID" ) );
			returnObj.setCompanyName    	( resultSet.getString    ( "COMPANY_NAME" ) );
			returnObj.setCompanyEmail   	( resultSet.getString    ( "COMPANY_EMAIL" ) );
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "Failed to get coupon with id : " + companyId + " " + e.getMessage());
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
	public void updateCompany(Company company) throws ApplicationException {
		PreparedStatement 	preparedStatement	= null;
		Connection 			connection 			= null;

		try 
		{
			// Getting a connection from the connections manager or Transaction manager
			connection = super.getConnection();

			// Creating a statement object which holds the SQL we're about to execute
			String sql;
			
			sql = "UPDATE COMPANYS SET ";
			sql += "	 SYS_UPDATE_DATE           = ?";
			sql += "	,UPDATED_BY_USER_ID        = ? ";
			sql += "	,USER_ID                   = ? ";
			sql += "	,COMPANY_NAME              = ? ";
			sql += "	,COMPANY_EMAIL             = ? ";
			sql += "WHERE";
			sql += "      	COMPANY_ID = ? ";
			
			preparedStatement = connection.prepareStatement(sql);

			// Replacing question mark with the value inside bean
			preparedStatement.setTimestamp	(1 ,	new Timestamp ( System.currentTimeMillis() ) );
			preparedStatement.setInt		(2 ,	company.getUpdatedByUserId() );
			preparedStatement.setInt		(3 ,	company.getUserId() );
			preparedStatement.setString		(4 ,	company.getCompanyName() );
			preparedStatement.setString		(5 ,	company.getCompanyEmail() );
			preparedStatement.setLong		(6 ,	company.getCompanyId() );
			

			// executeUpdate is a method used in order to : insert, delete, update (not get)
			preparedStatement.executeUpdate();
	        
		} 
		catch (SQLException e) 
		{
			//e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "Failed to update company due to :" + e.getMessage() );
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
		
	}

	@Override
	public void deleteCompany(long companyId) {
		// TODO Auto-generated method stub
		
	}

}
