package com.raviv.coupons.tests.system;

import com.raviv.coupons.blo.CustomerBlo;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemCustomerBlo300LoginTest {

	public static void main(String[] args) throws ApplicationException 
	{
		/**
		 *  login as company and get companyBlo
		 */
		CouponsSystem 	couponsSystem = CouponsSystem.getInstance();
		CustomerBlo 	customerBlo;
		try
		{
			customerBlo = 	(CustomerBlo) couponsSystem.login( "cust3" , "1234" );
		}
		catch (Exception e)
		{
			throw new ApplicationException(ErrorType.LOGIN_ERROR, e , "Login failed" );
		}
		
		/**
		 *  login success
		 */		
		System.out.println(customerBlo);

	}

}
