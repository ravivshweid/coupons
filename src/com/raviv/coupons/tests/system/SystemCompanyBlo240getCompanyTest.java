package com.raviv.coupons.tests.system;

import com.raviv.coupons.blo.CompanyBlo;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemCompanyBlo240getCompanyTest {

	public static void main(String[] args) throws ApplicationException 
	{
		/**
		 *  login as company and get companyBlo
		 */
		CouponsSystem 	couponsSystem = CouponsSystem.getInstance();
		CompanyBlo 		companyBlo;
		try
		{
			companyBlo = 	(CompanyBlo) couponsSystem.login( "comp1" , "1234" );
		}
		catch (Exception e)
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "Login failed" );
		}	
		
		/**
		 * Get company coupons
		 */
		companyBlo.getAllCoupons();
		
	}

}
