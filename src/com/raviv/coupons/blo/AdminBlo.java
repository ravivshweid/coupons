package com.raviv.coupons.blo;

import com.raviv.coupons.beans.Company;
import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.interfaces.IClientBlo;
import com.raviv.coupons.dao.CompanysDao;
import com.raviv.coupons.dao.UsersDao;
import com.raviv.coupons.dao.utils.JdbcTransactionManager;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.enums.UserProfileType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.tests.PrintUtils;

/**
 * 
 * Admin business logic
 * 
 * @author raviv
 *
 */
public class AdminBlo implements IClientBlo {

	//private	CompanysDao				companysDao;
	//private CouponsDao		couponsDao		=	null;
	//private CustomersDao	customersDao	=	null;	
	private UsersDao				usersDao;
	
	private User 					loggedUser;

	public AdminBlo() throws ApplicationException
	{
		//this.companysDao 			= new CompanysDao();
		//this.couponsDao			= new CouponsDao();
		//this.customersDao			= new CustomersDao();
		this.usersDao 				= new UsersDao();
	}
	
	
	@Override
	public  IClientBlo login(String loginName, String loginPassword) throws ApplicationException 
	{
				
		UserProfileType  adminUserProfileType = UserProfileType.ADMIN;
		
		this.loggedUser = this.usersDao.getUserByLoginNameLoginPasswordUserProfileId( loginName, loginPassword, adminUserProfileType.getUserProfileId() );

		if ( this.loggedUser == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "Failed to get user with loginName : " + loginName + ",  loginPassword : " + loginPassword + ", and UserProfileType.ADMIN." );
		}

		PrintUtils.printHeader("User logged in");		
		System.out.println(loggedUser);

		return null;
	}

	private  void verifyLoggedUser() throws ApplicationException 
	{
		if ( this.loggedUser == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null, "User is not logged in." );			
		}		
	}
	
	public  void createCompany(User user, Company company) throws ApplicationException 
	{
		
		verifyLoggedUser();

		// =====================================================
		// Start transaction by creating JdbcTransactionManager
		// =====================================================		
		JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();

		// Inject transaction manager to DAO via constructors
		UsersDao 	usersDao 	= new UsersDao   ( jdbcTransactionManager );
		CompanysDao companysDao	= new CompanysDao( jdbcTransactionManager );

		try
		{
			// =====================================================
			// Create new user for the company
			// =====================================================
			
			// Prepare inputs
			user.setCreatedByUserId( this.loggedUser.getUserId() );
			user.setUpdatedByUserId( this.loggedUser.getUserId() );
			UserProfileType companyUserProfileType = UserProfileType.COMPANY;
			user.setUserProfileId( companyUserProfileType.getUserProfileId() );
			
			// Create the user
			usersDao.createUser(user);
		
			
			
			// =====================================================
			// Create new company with the new user
			// =====================================================
			
			// Prepare inputs
			int newUserId = user.getUserId();
			company.setUserId(newUserId);
			company.setCreatedByUserId( this.loggedUser.getUserId() );
			company.setUpdatedByUserId( this.loggedUser.getUserId() );
	
			// Create the company
			companysDao.createCompany(company);
			

			// =====================================================
			// Commit transaction
			// =====================================================

			jdbcTransactionManager.commit();
			
			PrintUtils.printHeader("createCompany created company");
			System.out.println(user);
			System.out.println(company);
			
		}
		catch (ApplicationException e)
		{
			// =====================================================
			// Rollback transaction
			// =====================================================

			jdbcTransactionManager.rollback();
			
			throw (e); 
			
		}
		finally
		{
			jdbcTransactionManager.closeConnection();
		}	
	}

	public  void updateCompany(Company inputCompany) throws ApplicationException 
	{
		
		verifyLoggedUser();

		
		
		// =====================================================
		// Start transaction by creating JdbcTransactionManager
		// =====================================================		
		JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();

		// Inject transaction manager to DAO via constructors
		CompanysDao companysDao	= new CompanysDao( jdbcTransactionManager );

		Company company;
		
		company = companysDao.getCompany( inputCompany.getCompanyId() );
		
		try
		{			
			// =====================================================
			// Update company
			// =====================================================
			
			// Prepare inputs
			company.setUpdatedByUserId( this.loggedUser.getUserId() );
			company.setCompanyEmail( inputCompany.getCompanyEmail() );

			company.setCompanyName( inputCompany.getCompanyName() );

			
			// Update the company
			companysDao.updateCompany(company);

			// =====================================================
			// Commit transaction
			// =====================================================

			jdbcTransactionManager.commit();
			
			PrintUtils.printHeader("updateCompany updated company");
			System.out.println(company);
			
		}
		catch (ApplicationException e)
		{
			// =====================================================
			// Rollback transaction
			// =====================================================

			jdbcTransactionManager.rollback();
			
			throw (e); 
			
		}
		finally
		{
			jdbcTransactionManager.closeConnection();
		}	
	}

	
}
