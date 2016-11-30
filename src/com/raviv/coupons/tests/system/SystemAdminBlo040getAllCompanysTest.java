package com.raviv.coupons.tests.system;

import com.raviv.coupons.blo.AdminBlo;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemAdminBlo040getAllCompanysTest {

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
		
		
		adminBlo.getAllCompanys();

	}

}
