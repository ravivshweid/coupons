package com.raviv.coupons.blo;

import java.util.List;
import com.raviv.coupons.beans.Coupon;
import com.raviv.coupons.beans.Customer;
import com.raviv.coupons.beans.CustomerCoupon;
import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.interfaces.IClientBlo;
import com.raviv.coupons.dao.CouponsDao;
import com.raviv.coupons.dao.CustomerCouponDao;
import com.raviv.coupons.dao.CustomersDao;
import com.raviv.coupons.dao.UsersDao;
import com.raviv.coupons.dao.interfaces.ICouponsDao;
import com.raviv.coupons.dao.interfaces.ICustomerCouponDao;
import com.raviv.coupons.dao.utils.JdbcTransactionManager;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.enums.UserProfileType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.utils.PrintUtils;

/**
 * 
 * Customer business logic
 * 
 * @author raviv
 *
 */
public class CustomerBlo implements IClientBlo {

	
	private	CustomerCouponDao		customerCouponDao;
	private CouponsDao				couponsDao;
	private CustomersDao			customersDao;	
	private UsersDao				usersDao;

	private User 					loggedUser;
	private Customer				customer;

	private	boolean					isGetCustomerCouponsFromDao; /*  This is used to improve performance */
	
	public CustomerBlo() throws ApplicationException
	{
		//this.companysDao 				= new CompanysDao();
		
		this.couponsDao					= new CouponsDao();
		this.customersDao				= new CustomersDao();
		this.usersDao 					= new UsersDao();
		this.customerCouponDao			= new CustomerCouponDao();
		this.isGetCustomerCouponsFromDao= true;
	}
	
	@Override
	public  IClientBlo 			login(String loginName, String loginPassword) throws ApplicationException 
	{
		//==============================================
		// Get user details
		//==============================================
		this.loggedUser = this.usersDao.getUserByLoginNameLoginPasswordUserProfileId( loginName, loginPassword, UserProfileType.CUSTOMER.getUserProfileId() );
		if ( this.loggedUser == null )
		{
			throw new ApplicationException(ErrorType.LOGIN_ERROR, null
					, "Failed to get user with loginName : " + loginName + ",  loginPassword : " + loginPassword + ", and UserProfileType.COMPANY." );
		}
		PrintUtils.printHeader("CustomerBlo: User logged in");		
		System.out.println(loggedUser);

		//==============================================
		// Get customer details with the user id
		//==============================================		
		setCustomerWithLoggedUser();
		
		return this;
	}
	
	@Override
	public  IClientBlo 			login(User user) throws ApplicationException
	{
				
		if ( user == null ) {
			throw new ApplicationException(ErrorType.LOGIN_ERROR, null , "login with null User." );		
		}
		
		verifyUserProfileId (user);
		
		this.loggedUser = user;

		PrintUtils.printHeader("CustomerBLo: User logged in");		
		System.out.println(loggedUser);
		
		//==============================================
		// Get company details by the user id
		//==============================================		
		setCustomerWithLoggedUser();

		return (CustomerBlo) this;
	}

	private	void 				verifyUserProfileId(User user) throws ApplicationException 
	{
		if ( user == null)
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "User input is null" );					
		}
		
		int userProfileId = user.getUserProfileId();

		if      ( userProfileId != UserProfileType.CUSTOMER.getUserProfileId() )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "User profile ID must be CUSTOMER. input  userProfileId: " + userProfileId );		

		}		
	}

	private	void 				verifyLogin() throws ApplicationException 
	{
		if ( this.loggedUser == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null, "User is not logged in." );			
		}		
		
		verifyUserProfileId (this.loggedUser);

		if ( this.customer == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null, "Customer is not logged in." );			
		}		

	}
	
	private	void 				setCustomerWithLoggedUser() throws ApplicationException 
	{		
		//==============================================
		// Get customer details by the user id
		//==============================================		
		int userId		= this.loggedUser.getUserId();
		this.customer	= this.customersDao.getCustomerByUserId(userId);
		if ( this.customer == null )
		{
			throw new ApplicationException(ErrorType.DAO_GET_ERROR, null
					, "Failed to set company with userId : " + userId );
		}
		PrintUtils.printHeader("CustomerBLo: getCustomerByUserId success");		
		System.out.println( this.customer );					
	}

	public	synchronized void	buyCoupon( long couponId ) throws ApplicationException 
	{
		verifyLogin();

		// =====================================================
		// Get coupon from data layer
		// =====================================================
		ICouponsDao	couponsDao			= new CouponsDao();
		Coupon		coupon = couponsDao.getCoupon(couponId);
		if ( coupon == null )
		{
			throw new ApplicationException(ErrorType.DAO_GET_ERROR, null , "Coupon not found, couponId : " + couponId);
		}
		
		// =====================================================
		// Check if we have coupons in stock
		// =====================================================
		int			couponsInStock = coupon.getCouponsInStock();
		if ( couponsInStock <= 0 ) 
		{
			throw new ApplicationException(ErrorType.COUPON_OUT_OF_STOCK_ERROR, null
					, "CustomerBlo : buyCoupon Failed. coupon is out of stock, couponId : " + couponId  );
		}
		
		// =====================================================
		// Check if customer was buying this coupon already.
		// =====================================================
		ICustomerCouponDao		customerCouponDao;
		customerCouponDao	= new CustomerCouponDao ();
		
		CustomerCoupon	customerCoupon = customerCouponDao.getCustomerCoupon( this.customer.getCustomerId(), couponId );
		if ( customerCoupon != null ) 
		{
			throw new ApplicationException(ErrorType.DUPLICATE_COUPON_FOR_CUSTOMER, null
					, "CustomerBlo : buyCoupon Failed. Duplicate coupon for customer, couponId : " + couponId  );
		}
		
		
		
		// =====================================================
		// Start transaction by creating JdbcTransactionManager
		// =====================================================		
		JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager();

		// Inject transaction manager to DAO via constructors
		customerCouponDao	= new CustomerCouponDao ( jdbcTransactionManager );

		try
		{				
			// =====================================================
			// Create new customer coupon with the new coupon id
			// =====================================================
			
			customerCoupon = new CustomerCoupon ( customer.getCustomerId(), couponId );
			
			customerCoupon.setCreatedByUserId( this.loggedUser.getUserId() );
			customerCoupon.setUpdatedByUserId( this.loggedUser.getUserId() );

			// Create the customer coupon
			customerCouponDao.createCustomerCoupon(customerCoupon);

			// =====================================================
			// Update coupons, take one coupon away from stock
			// =====================================================
			couponsInStock--;
			coupon.setCouponsInStock(couponsInStock);
			coupon.setUpdatedByUserId( this.loggedUser.getUserId() );

			// Update coupon
			couponsDao.updateCoupon(coupon);
			
			// =====================================================
			// Commit transaction
			// =====================================================

			jdbcTransactionManager.commit();
		
			this.isGetCustomerCouponsFromDao = true;
			
			PrintUtils.printHeader("CustomerBlo : buyCoupon success.");
			//System.out.println(coupon);
		
			
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
	
	public  Customer 			getCustomer() throws ApplicationException 
	{				
		verifyLogin();
		
		return this.customer;
	}

	public  List<CustomerCoupon>getCustomerCoupons() throws ApplicationException 
	{		
		verifyLogin();
		
		PrintUtils.printHeader ("CustomerBLo: getAllCoupons");
		
		List<CustomerCoupon>	customerCoupons;
				
		if ( this.isGetCustomerCouponsFromDao == true )
		{
			//Lets go to data layer ...
			this.setCustomerCoupons();
			// Lets raise a flag and say there is no need to use data layer next time, for better performance
			this.isGetCustomerCouponsFromDao = false;			
		}
		
		// Get the coupons from customer bean
		customerCoupons = customer.getCustomerCoupons();
				
		for ( CustomerCoupon customerCoupon : customerCoupons )
		{
			System.out.println(customerCoupon);
		}
		
		return customerCoupons;
	}

	public  List<CustomerCoupon>getCustomerCouponsQuery( DynamicQueryParameters dynamicQueryParameters ) throws ApplicationException 
	{		
		verifyLogin();
		
		PrintUtils.printHeader ("CustomerBLo: getCustomerCouponsQuery");

		long customerId 		= customer.getCustomerId();
		List<CustomerCoupon>	customerCoupons= customerCouponDao.getCustomerCouponsByCustomerIdAndDynamicFilter( customerId , dynamicQueryParameters);
										
		for ( CustomerCoupon customerCoupon : customerCoupons )
		{
			System.out.println(customerCoupon);
		}
		
		return customerCoupons;
	}

	public  List<Coupon>		getCouponsQuery( DynamicQueryParameters dynamicQueryParameters) throws ApplicationException 
	{		
		verifyLogin();
		
		PrintUtils.printHeader ("CustomerBLo: getCouponsQuery");
		
		long companyId 	= customer.getCustomerId();
		List<Coupon> couponsList 	= couponsDao.getCouponsByCompanyIdAndDynamicFilter( companyId , dynamicQueryParameters);

		for ( Coupon coupon : couponsList )
		{
			System.out.println(coupon);
		}
	
		return couponsList;
	}

	private  void				setCustomerCoupons() throws ApplicationException 
	{		
		verifyLogin();
		
		long 					customerId		= customer.getCustomerId();
		List<CustomerCoupon>	customerCoupons	= customerCouponDao.getCustomerCouponsByCustomerId(customerId);
		//set coupons list in the bean
		customer.setCustomerCoupons(customerCoupons);
	}

}
