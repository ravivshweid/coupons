package com.raviv.coupons.blo;

import java.util.List;

import com.raviv.coupons.beans.Company;
import com.raviv.coupons.beans.Customer;
import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.interfaces.IClientBlo;
import com.raviv.coupons.dao.CompanysDao;
import com.raviv.coupons.dao.CustomersDao;
import com.raviv.coupons.dao.UsersDao;
import com.raviv.coupons.dao.interfaces.ICompanysDao;
import com.raviv.coupons.dao.interfaces.ICustomersDao;
import com.raviv.coupons.dao.interfaces.IUsersDao;
import com.raviv.coupons.dao.utils.JdbcTransactionManager;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.enums.UserProfileType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.utils.PrintUtils;

/**
 * 
 * Admin business logic
 * 
 * @author raviv
 *
 */
public class AdminBlo implements IClientBlo {

	private	CompanysDao				companysDao;
	//private CouponsDao		couponsDao		=	null;
	private CustomersDao			customersDao;	
	private UsersDao				usersDao;

	private User 					loggedUser;

	public AdminBlo() throws ApplicationException
	{
		this.companysDao 			= new CompanysDao();
		//this.couponsDao			= new CouponsDao();
		this.customersDao			= new CustomersDao();
		this.usersDao 				= new UsersDao();
	}
	
	@Override
	public  IClientBlo 		login(String loginName, String loginPassword) throws ApplicationException 
	{
				
		UserProfileType  adminUserProfileType = UserProfileType.ADMIN;
		
		this.loggedUser = this.usersDao.getUserByLoginNameLoginPasswordUserProfileId( loginName, loginPassword, adminUserProfileType.getUserProfileId() );

		if ( this.loggedUser == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "Failed to get user with loginName : " + loginName + ",  loginPassword : " + loginPassword + ", and UserProfileType.ADMIN." );
		}

		PrintUtils.printHeader("AdminBlo: User logged in");		
		System.out.println(loggedUser);

		return this;
	}
	
	@Override
	public  IClientBlo 		login(User user) throws ApplicationException 
	{
				
		if ( user == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null , "login with null user" );		
		}
		
		verifyAdminUserProfileId (user);
		
		this.loggedUser = user;

		PrintUtils.printHeader("AdminBlo: User logged in");		
		System.out.println(loggedUser);

		return this;
	}

	public  void 			verifyAdminUserProfileId(User user) throws ApplicationException 
	{
		int userProfileId = user.getUserProfileId();

		UserProfileType  adminUserProfileType = UserProfileType.ADMIN;
		if      ( userProfileId != adminUserProfileType.getUserProfileId() )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "User profile ID must be ADMIN. input  userProfileId: " + userProfileId );		

		}		
	}

	private  void 			verifyLoggedUser() throws ApplicationException 
	{
		if ( this.loggedUser == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null, "User is not logged in." );			
		}		
		
		verifyAdminUserProfileId (this.loggedUser);
	}
	
	public  void 			createCompany(User user, Company company) throws ApplicationException 
	{
		
		verifyLoggedUser();

		// =====================================================
		// Start transaction by creating JdbcTransactionManager
		// =====================================================		
		JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();

		// Inject transaction manager to DAO via constructors
		IUsersDao 		usersDao 	= new UsersDao   ( jdbcTransactionManager );
		ICompanysDao	companysDao	= new CompanysDao( jdbcTransactionManager );

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

	public  void 			updateCompany(Company inputCompany) throws ApplicationException 
	{
		
		verifyLoggedUser();

		
		
		// =====================================================
		// Start transaction by creating JdbcTransactionManager
		// =====================================================		
		JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();

		// Inject transaction manager to DAO via constructors
		ICompanysDao companysDao	= new CompanysDao( jdbcTransactionManager );

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

	public  List<Company>	getAllCompanys() throws ApplicationException 
	{		
		verifyLoggedUser();
		
		List<Company> companysList;
		companysList = companysDao.getAllCompanys();
		
		for ( Company company : companysList )
		{
			System.out.println(company);
		}
		
		return companysList;
	}

	public  Company 		getCompany(long companyId) throws ApplicationException 
	{		
		verifyLoggedUser();
		
		Company company;
		company = companysDao.getCompany(companyId);
		
		if ( company == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null, "Company not found, companyId : " + companyId );			
		}
		
		System.out.println(company);
		
		return company;
	}

	public  void 			createCustomer(User user, Customer customer) throws ApplicationException 
	{
		
		verifyLoggedUser();

		// =====================================================
		// Start transaction by creating JdbcTransactionManager
		// =====================================================		
		JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();

		// Inject transaction manager to DAO via constructors
		IUsersDao 		usersDao 		= new UsersDao   	( jdbcTransactionManager );
		ICustomersDao	customersDao	= new CustomersDao	( jdbcTransactionManager );

		try
		{
			// =====================================================
			// Create new user for the new customer
			// =====================================================
			
			// Prepare inputs
			user.setCreatedByUserId( this.loggedUser.getUserId() );
			user.setUpdatedByUserId( this.loggedUser.getUserId() );
			UserProfileType customerUserProfileType = UserProfileType.CUSTOMER;
			user.setUserProfileId( customerUserProfileType.getUserProfileId() );
			
			// Create the user
			usersDao.createUser(user);
		
			// =====================================================
			// Create new customer with the new user
			// =====================================================
			
			// Prepare inputs
			int newUserId = user.getUserId();
			customer.setUserId(newUserId);
			customer.setCreatedByUserId( this.loggedUser.getUserId() );
			customer.setUpdatedByUserId( this.loggedUser.getUserId() );
	
			// Create the customer
			customersDao.createCustomer(customer);
			

			// =====================================================
			// Commit transaction
			// =====================================================

			jdbcTransactionManager.commit();
			
			PrintUtils.printHeader("createCustomer created customer");
			System.out.println(user);
			System.out.println(customer);
			
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
	}// createCustomer

	public  List<Customer>	getAllCustomers() throws ApplicationException 
	{		
		verifyLoggedUser();
		
		List<Customer> customersList;
		customersList = customersDao.getAllCustomers();
		
		for ( Customer customer : customersList )
		{
			System.out.println(customer);
		}
		
		return customersList;
	}

	public  Customer 		getCustomer(long customerId) throws ApplicationException 
	{		
		verifyLoggedUser();
		
		Customer customer;
		customer = customersDao.getCustomer(customerId);
		
		if ( customer == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null, "Customer not found, customerId : " + customerId );			
		}
		
		System.out.println(customer);
		
		return customer;
	}


}
