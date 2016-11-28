package com.raviv.coupons.blo;

import java.util.List;

import com.raviv.coupons.beans.Company;
import com.raviv.coupons.beans.Coupon;
import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.interfaces.IClientBlo;
import com.raviv.coupons.dao.CompanysDao;
import com.raviv.coupons.dao.CouponsDao;
import com.raviv.coupons.dao.UsersDao;
import com.raviv.coupons.dao.interfaces.ICompanysDao;
import com.raviv.coupons.dao.interfaces.ICouponsDao;
import com.raviv.coupons.dao.utils.JdbcTransactionManager;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.enums.UserProfileType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.utils.PrintUtils;

/**
 * 
 * Company business logic
 * 
 * @author raviv
 *
 */
public class CompanyBlo implements IClientBlo {

	
	private	CompanysDao				companysDao;
	private CouponsDao				couponsDao;
	//private CustomersDao			customersDao;	
	private UsersDao				usersDao;

	private User 					loggedUser;
	private Company					company;

	private	boolean					isGetCompanyCouponsFromDao; /*  This is used to improve performance */
	
	public CompanyBlo() throws ApplicationException
	{
		this.companysDao 				= new CompanysDao();
		this.couponsDao					= new CouponsDao();
		//this.customersDao				= new CustomersDao();
		this.usersDao 					= new UsersDao();
		this.isGetCompanyCouponsFromDao	= true;
	}
	
	@Override
	public  IClientBlo 		login(String loginName, String loginPassword) throws ApplicationException 
	{
		//==============================================
		// Get user details
		//==============================================
		UserProfileType  companyUserProfileType = UserProfileType.COMPANY;		
		this.loggedUser = this.usersDao.getUserByLoginNameLoginPasswordUserProfileId( loginName, loginPassword, companyUserProfileType.getUserProfileId() );
		if ( this.loggedUser == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "Failed to get user with loginName : " + loginName + ",  loginPassword : " + loginPassword + ", and UserProfileType.COMPANY." );
		}
		PrintUtils.printHeader("CompanyBlo: User logged in");		
		System.out.println(loggedUser);

		//==============================================
		// Get company details with the user id
		//==============================================		
		setCompanyWithLoggedUser();
		
		return this;
	}
	
	@Override
	public  IClientBlo 		login(User user) throws ApplicationException 
	{
				
		if ( user == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null , "login with null user" );		
		}
		
		verifyUserProfileId (user);
		
		this.loggedUser = user;

		PrintUtils.printHeader("CompanyBlo: User logged in");		
		System.out.println(loggedUser);
		
		//==============================================
		// Get company details with the user id
		//==============================================		
		setCompanyWithLoggedUser();

		return (CompanyBlo) this;
	}

	private  void 			verifyUserProfileId(User user) throws ApplicationException 
	{
		int userProfileId = user.getUserProfileId();

		UserProfileType  companyUserProfileType = UserProfileType.COMPANY;
		if      ( userProfileId != companyUserProfileType.getUserProfileId() )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "User profile ID must be COMPANY. input  userProfileId: " + userProfileId );		

		}		
	}

	private  void 			verifyLoggedUser() throws ApplicationException 
	{
		if ( this.loggedUser == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null, "User is not logged in." );			
		}		
		
		verifyUserProfileId (this.loggedUser);

		if ( this.company == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null, "Company is not logged in." );			
		}		

	}
	
	public  void 			createCoupon( Coupon coupon ) throws ApplicationException 
	{
		
		verifyLoggedUser();

		// =====================================================
		// Start transaction by creating JdbcTransactionManager
		// =====================================================		
		JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();

		// Inject transaction manager to DAO via constructors
		ICouponsDao		couponsDao	= new CouponsDao ( jdbcTransactionManager );

		try
		{				
			// =====================================================
			// Create new company with the new user
			// =====================================================
			
			coupon.setCreatedByUserId( this.loggedUser.getUserId() );
			coupon.setUpdatedByUserId( this.loggedUser.getUserId() );
			coupon.setCompanyId		 ( this.company.getCompanyId() );

			// Create the company
			couponsDao.createCoupon(coupon);
			
			// =====================================================
			// Commit transaction
			// =====================================================

			jdbcTransactionManager.commit();
		
			this.isGetCompanyCouponsFromDao = true;
			
			PrintUtils.printHeader("createCoupon created coupon");
			System.out.println(coupon);
		
			
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

	public  void 			updateCoupon(Coupon inputCoupon) throws ApplicationException 
	{
		
		verifyLoggedUser();

		// =====================================================
		// Start transaction by creating JdbcTransactionManager
		// =====================================================		
		JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();

		// Inject transaction manager to DAO via constructors
		ICouponsDao couponsDao	= new CouponsDao( jdbcTransactionManager );
		
		try
		{
			// =====================================================
			// Get coupon from data layer
			// =====================================================			
			Coupon coupon;
			coupon = couponsDao.getCoupon( inputCoupon.getCouponId() );

			// =====================================================
			// Set coupon: end date , price
			// =====================================================
			
			// Prepare inputs
			coupon.setUpdatedByUserId( this.loggedUser.getUserId()    );
			
			coupon.setCouponEndDate  ( inputCoupon.getCouponEndDate() );
			coupon.setCouponPrice    ( inputCoupon.getCouponPrice()   );

			
			// =====================================================
			// Update coupon in data layer
			// =====================================================			
			couponsDao.updateCoupon(coupon);

			// =====================================================
			// Commit transaction
			// =====================================================

			jdbcTransactionManager.commit();
			
			this.isGetCompanyCouponsFromDao = true;
			
			PrintUtils.printHeader("updateCoupon updated coupon");
			System.out.println(coupon);
			
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

	private  void 			setCompanyWithLoggedUser() throws ApplicationException 
	{		
		//==============================================
		// Get company details with the user id
		//==============================================		
		int userId   = this.loggedUser.getUserId();
		this.company = this.companysDao.getCompanyByUserId(userId);
		if ( this.company == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "Failed to set company with userId : " + userId );
		}
		PrintUtils.printHeader("CompanyBlo: Company logged in");		
		System.out.println(company);					
	}

	public  Company 		getCompany() throws ApplicationException 
	{				
		verifyLoggedUser();
		
		return this.company;
	}

	public  List<Coupon>	getAllCoupons() throws ApplicationException 
	{		
		verifyLoggedUser();
		
		PrintUtils.printHeader ("CompanyBlo: getAllCoupons");
		
		List<Coupon> couponsList;
				
		if ( this.isGetCompanyCouponsFromDao == true )
		{
			//Lets go to data layer ...
			this.setCompanyCoupons();
			// Lets raise a flag and say there is no need to use data layer next time, for better performance
			this.isGetCompanyCouponsFromDao = false;			
		}
		
		// Get the coupons from Company bean
		couponsList = company.getCoupons();
				
		for ( Coupon coupon : couponsList )
		{
			System.out.println(coupon);
		}
		
		return couponsList;
	}

	public  List<Coupon>	getCouponsQuery( DynamicQueryParameters dynamicQueryParameters) throws ApplicationException 
	{		
		verifyLoggedUser();
		
		PrintUtils.printHeader ("CompanyBlo: getCouponsQuery");
		
		long companyId 	= company.getCompanyId();
		List<Coupon> couponsList 	= couponsDao.getCouponsByCompanyIdAndDynamicFilter( companyId , dynamicQueryParameters);

		for ( Coupon coupon : couponsList )
		{
			System.out.println(coupon);
		}
	
		return couponsList;
	}

	private  void			setCompanyCoupons() throws ApplicationException 
	{		
		verifyLoggedUser();
		
		long companyId 	= company.getCompanyId();
		List<Coupon> couponsList 	= couponsDao.getCouponsByCompanyId(companyId);
		//set coupons list in the bean
		company.setCoupons(couponsList);
	}

	public  void 			deleteCoupon(long couponId) throws ApplicationException 
	{		
		verifyLoggedUser();

		// =====================================================
		// Start transaction by creating JdbcTransactionManager
		// =====================================================		
		JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();

		// Inject transaction manager to DAO via constructors
		ICouponsDao couponsDao	= new CouponsDao( jdbcTransactionManager );
		
		try
		{
			// =====================================================
			// Delete coupon and related customer coupons
			// CUSTOMER_COUPON has FK to COUPONS using coupon id, with delete Cascade
			// =====================================================			
			
			couponsDao.deleteCoupon(couponId);
			
			// =====================================================
			// Commit transaction
			// =====================================================

			jdbcTransactionManager.commit();
			
			this.isGetCompanyCouponsFromDao = true;
			
			PrintUtils.printHeader("deleteCoupon deleted couponId : " + couponId );
			
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
