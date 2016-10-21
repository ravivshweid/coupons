package com.raviv.coupons.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import com.raviv.coupons.beans.Coupon;
import com.raviv.coupons.dao.interfaces.ICouponsDao;
import com.raviv.coupons.dao.utils.JdbcTransactionManager;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;

public class CouponsDao extends InfraDao implements ICouponsDao {

	public 			CouponsDao() 
	{
		super();
	}
	
	public 			CouponsDao(JdbcTransactionManager jdbcTransactionManager) {
		super(jdbcTransactionManager);
	}

	@Override
	public void 	createCoupon(Coupon coupon) throws ApplicationException {
	
		PreparedStatement 	preparedStatement	= null;
		Connection 			connection 			= null;
		ResultSet 			generatedKeys		= null;


		try 
		{
			// Getting a connection from the connections manager or Transaction manager
			connection = super.getConnection();

			// Creating a statement object which holds the SQL we're about to execute
			String sql;
			
			sql = "INSERT INTO COUPONS (";
			sql += "	 CREATED_BY_USER_ID";
			sql += "	,UPDATED_BY_USER_ID";
			sql += "	,COMPANY_ID";
			sql += "	,COUPON_TITLE";
			sql += "	,COUPON_START_DATE";
			sql += "	,COUPON_END_DATE";
			sql += "	,COUPONS_IN_STOCK";
			sql += "	,COUPON_TYPE_ID";
			sql += "	,COUPON_MESSAGE";
			sql += "	,COUPON_PRICE";
			sql += "	,IMAGE_FILE_NAME";
			sql += ")";
			sql += "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			
			preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

			// Replacing question marks with the values inside  the bean.
			preparedStatement.setInt		(1 ,	coupon.getCreatedByUserId() );
			preparedStatement.setInt		(2 ,	coupon.getUpdatedByUserId() );
			preparedStatement.setLong		(3 ,	coupon.getCompanyId() );
			preparedStatement.setString		(4 ,	coupon.getCouponTitle() );
			preparedStatement.setTimestamp	(5 ,	new Timestamp ( coupon.getCouponStartDate() ) );
			preparedStatement.setTimestamp	(6 ,	new Timestamp ( coupon.getCouponEndDate() )   );
			preparedStatement.setInt		(7 ,	coupon.getCouponsInStock() );
			preparedStatement.setInt		(8 ,	coupon.getCouponTypeId() );
			preparedStatement.setString		(9 ,	coupon.getCouponMessage() );
			preparedStatement.setDouble 	(10,	coupon.getCouponPrice() );
			preparedStatement.setString		(11,	coupon.getImageFileName() );


			// executeUpdate is a method used in order to : insert, delete, update (not get)
			preparedStatement.executeUpdate();

			// Handle generated keys...
			generatedKeys = preparedStatement.getGeneratedKeys();
	        if ( generatedKeys.next() ) 
	        {
	        	int couponId = generatedKeys.getInt("GENERATED_KEYS");
	        	
	        	coupon.setCouponId( couponId );
	        }
	        
	        
		} 
		catch (SQLException e) 
		{
			//e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "Failed to create coupon due to :" + e.getMessage() );
		} 
		finally 
		{
			
			if ( super.isJdbcTransactionManagerInUse() )
			{
				// Transaction manager will close the connection later.
				super.connectionPoolManager.closeResources(preparedStatement, generatedKeys);
			}
			else
			{
				// We do not have transaction manager.
				super.connectionPoolManager.closeResources(connection, preparedStatement, generatedKeys);
			}				

			
		}
		
	}
	
	@Override
	public Coupon 	getCoupon(long couponId) throws ApplicationException 
	{
		Connection 			connection 			= null;
		PreparedStatement 	preparedStatement 	= null;		
		ResultSet 			resultSet 			= null;
		Coupon 				returnObj 			= new Coupon();

		// getting the specific entry by the input id
		// storing it into resultSet
		try 
		{
			// Getting a connection from the connections manager or Transaction manager
			connection = super.getConnection();
			
			String sql = "SELECT * FROM COUPONS WHERE COUPON_ID = ?";
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setLong(1, couponId);
			
			// execute query
			resultSet = preparedStatement.executeQuery();
	
			// Not found ...
			if ( !resultSet.next() ) { return null; } 
			
			
			//extract bean from result Set
			returnObj.setCouponId       	( resultSet.getLong      ( "COUPON_ID" ) );
			returnObj.setSysCreationDate	( resultSet.getTimestamp ( "SYS_CREATION_DATE" ).getTime() );
			returnObj.setSysUpdateDate  	( resultSet.getTimestamp ( "SYS_UPDATE_DATE" ).getTime() );
			returnObj.setCreatedByUserId	( resultSet.getInt       ( "CREATED_BY_USER_ID" ) );
			returnObj.setUpdatedByUserId	( resultSet.getInt       ( "UPDATED_BY_USER_ID" ) );
			returnObj.setCompanyId      	( resultSet.getLong      ( "COMPANY_ID" ) );
			returnObj.setCouponTitle    	( resultSet.getString    ( "COUPON_TITLE" ) );
			returnObj.setCouponStartDate	( resultSet.getTimestamp ( "COUPON_START_DATE" ).getTime() );
			returnObj.setCouponEndDate  	( resultSet.getTimestamp ( "COUPON_END_DATE" ).getTime() );
			returnObj.setCouponsInStock 	( resultSet.getInt       ( "COUPONS_IN_STOCK" ) );
			returnObj.setCouponTypeId   	( resultSet.getInt       ( "COUPON_TYPE_ID" ) );
			returnObj.setCouponMessage  	( resultSet.getString    ( "COUPON_MESSAGE" ) );
			returnObj.setCouponPrice    	( resultSet.getDouble    ( "COUPON_PRICE" ) );
			returnObj.setImageFileName  	( resultSet.getString    ( "IMAGE_FILE_NAME" ) );
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "Failed to get coupon with id : " + couponId + " " + e.getMessage());
		} 
		finally 
		{			
			if ( super.isJdbcTransactionManagerInUse() )
			{
				// Transaction manager will close the connection later.
				super.connectionPoolManager.closeResources(preparedStatement, resultSet);
			}
			else
			{
				// We do not have transaction manager.
				super.connectionPoolManager.closeResources(connection, preparedStatement, resultSet);
			}	
		}
		
		return returnObj;
}

	@Override
	public void 	updateCoupon(Coupon coupon) throws ApplicationException 
	{
		PreparedStatement 	preparedStatement	= null;
		Connection 			connection 			= null;

		try 
		{
			// Getting a connection from the connections manager or Transaction manager
			connection = super.getConnection();

			// Creating a statement object which holds the SQL we're about to execute
			String sql;
			
			sql = "UPDATE COUPONS SET ";
			sql += "	 SYS_UPDATE_DATE           = ?";
			sql += "	,UPDATED_BY_USER_ID        = ? ";
			sql += "	,COMPANY_ID                = ? ";
			sql += "	,COUPON_TITLE              = ? ";
			sql += "	,COUPON_START_DATE         = ? ";
			sql += "	,COUPON_END_DATE           = ? ";
			sql += "	,COUPONS_IN_STOCK          = ? ";
			sql += "	,COUPON_TYPE_ID            = ? ";
			sql += "	,COUPON_MESSAGE            = ? ";
			sql += "	,COUPON_PRICE              = ? ";
			sql += "	,IMAGE_FILE_NAME           = ? ";
			sql += "WHERE";
			sql += "      	COUPON_ID = ? ";
			
			preparedStatement = connection.prepareStatement(sql);

			// Replacing question marks with the values inside  the bean.
			preparedStatement.setTimestamp	(1 ,	new Timestamp ( System.currentTimeMillis() ) );
			preparedStatement.setInt		(2 ,	coupon.getUpdatedByUserId() );
			preparedStatement.setLong		(3 ,	coupon.getCompanyId() );
			preparedStatement.setString		(4 ,	coupon.getCouponTitle() );
			preparedStatement.setTimestamp	(5 ,	new Timestamp ( coupon.getCouponStartDate() ) );
			preparedStatement.setTimestamp	(6 ,	new Timestamp ( coupon.getCouponEndDate() ) );
			preparedStatement.setInt		(7 ,	coupon.getCouponsInStock() );
			preparedStatement.setInt		(8 ,	coupon.getCouponTypeId() );
			preparedStatement.setString		(9 ,	coupon.getCouponMessage() );
			preparedStatement.setDouble		(10,	coupon.getCouponPrice() );
			preparedStatement.setString		(11,	coupon.getImageFileName() );
			preparedStatement.setLong		(12,	coupon.getCouponId() );

			// executeUpdate is a method used in order to : insert, delete, update (not get)
			preparedStatement.executeUpdate();	        
	        
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			throw new ApplicationException(ErrorType.GENERAL_ERROR, e, "Failed to create coupon due to :" + e.getMessage() );
		} 
		finally 
		{
			if ( super.isJdbcTransactionManagerInUse() )
			{
				// Transaction manager will close the connection later.
				super.connectionPoolManager.closeResources(preparedStatement);
			}
			else
			{
				// We do not have transaction manager.
				super.connectionPoolManager.closeResources(connection, preparedStatement);
			}	
		}
		
	}

	@Override
	public void 	deleteCoupon(long coupon) {
		// TODO Auto-generated method stub
		
	}

}
