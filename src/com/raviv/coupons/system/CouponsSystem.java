package com.raviv.coupons.system;

import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.AdminBlo;
import com.raviv.coupons.blo.CompanyBlo;
import com.raviv.coupons.blo.CustomerBlo;
import com.raviv.coupons.blo.interfaces.IClientBlo;
import com.raviv.coupons.dao.UsersDao;
import com.raviv.coupons.dao.utils.ConnectionPoolManager;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.enums.UserProfileType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.threads.DeleteExpiredCouponsScheduler;
import com.raviv.coupons.utils.PrintUtils;

public class CouponsSystem 
{
	
	private static CouponsSystem singleToneInstance;
	
	private UsersDao usersDao;
	
	private static DeleteExpiredCouponsScheduler deleteExpiredCouponsScheduler;
	
	static 
	{ 
		singleToneInstance = new CouponsSystem();
		
		deleteExpiredCouponsScheduler = new DeleteExpiredCouponsScheduler();
		
		// =====================================================
		// Start the delete expired coupons thread
		// =====================================================
		deleteExpiredCouponsScheduler.start();
	}

	/**
	 * System shut down
	 * @throws ApplicationException 
	 * */
	public void shutDown() throws ApplicationException
	{
		PrintUtils.printHeader("CouponsSystem : shutDown()" );
		// =====================================================
		// Shut down delete expired coupons thread
		// =====================================================
		deleteExpiredCouponsScheduler.shutDown();

		// =====================================================
		// Close all connection pool connections
		// =====================================================
		ConnectionPoolManager.getInstance().closeAllConnections();
	}


	/**
	 * returns an instance of the ConnectionPoolManager
	 * 
	 * @return an instance for the CouponsSystem singleton
	 * */
	public static CouponsSystem 	getInstance()  
	{
		return singleToneInstance;
	}

	/**
	 * private contractor, single tone class
	 */
	private CouponsSystem()
	{
		usersDao = new UsersDao();
	}
	
	public  IClientBlo login(String loginName, String loginPassword) throws ApplicationException 
	{

		//==================================================
		// 1. Get user from data layer
		//==================================================
		User 		loggedUser;				
		loggedUser = this.usersDao.getUserByLoginNameLoginPassword( loginName, loginPassword );
		if ( loggedUser == null )
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "User not found. loginName : " + loginName + ",  loginPassword : " + loginPassword  );
		}
		PrintUtils.printHeader("CouponsSystem : User logged in");		
		System.out.println(loggedUser);

		
		//===================================================================================
		// 2. According to user profile id       : ( ADMIN   , COMPANY    or CUSTOMER   )
		//    Instantiate relevant client object : ( AdminBlo, CompanyBlo or CustomerBlo)
		//===================================================================================
		
		IClientBlo 	clientBlo = null;

		UserProfileType  adminUserProfileType = UserProfileType.ADMIN;
		UserProfileType  companyUserProfileType = UserProfileType.COMPANY;
		UserProfileType  customerUserProfileType = UserProfileType.CUSTOMER;
		
		int userProfileId = loggedUser.getUserProfileId();

		if      ( userProfileId == adminUserProfileType.getUserProfileId() )
		{
				  clientBlo = new AdminBlo();
		}
		else if ( userProfileId == companyUserProfileType.getUserProfileId() )
		{
				  clientBlo = new CompanyBlo();			
		}
		else if ( userProfileId == customerUserProfileType.getUserProfileId() )
		{
				  clientBlo = new CustomerBlo();
		}
		else
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null, "Invalid userProfileId : " + userProfileId );		
		}

		//===================================================================================
		// 3. perform login with user instance.
		//    Instantiate relevant client object : ( AdminBlo, CompanyBlo or CustomerBlo)
		//===================================================================================
		clientBlo.login(loggedUser);
		
		//===================================================================================
		// 4. return client instance
		//===================================================================================
		return clientBlo;
	}


}
