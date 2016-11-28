package com.raviv.coupons.system.tests;

import com.raviv.coupons.beans.Company;
import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.AdminBlo;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemAdminBlo010createCompanyTest {

	public static void main(String[] args) throws ApplicationException 
	{
		
		/**
		 *  login as admin and get adminBlo
		 */
		CouponsSystem 	couponsSystem = CouponsSystem.getInstance();
		AdminBlo 		adminBlo;
		try
		{
			adminBlo = (AdminBlo) couponsSystem.login("admin", "1234");
		}
		catch (Exception e)
		{
			throw new ApplicationException(ErrorType.LOGIN_ERROR, e , "Login failed" );
		}
		
		User 	user 	= new User		( "Raviv Shweid"  , "raviv8", "unix11" 			);
		Company	company	= new Company	( "Company test 8", "raviv.shweid@gmail.com"	);
		
		adminBlo.createCompany(user, company);
		
	}

}
