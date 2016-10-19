package com.raviv.coupons.system.tests;

import com.raviv.coupons.blo.CompanyBlo;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemCompanyBlo200LoginTest {

	public static void main(String[] args) throws ApplicationException 
	{
		
		CouponsSystem 	couponsSystem = CouponsSystem.getInstance();
		
		CompanyBlo 		companyBlo;
		
		String usr = "comp1";
		String pwd = "1234";
		try
		{
			companyBlo = 	(CompanyBlo) couponsSystem.login( usr , pwd );
		}
		catch (Exception e)
		{
			throw new ApplicationException(ErrorType.GENERAL_ERROR, null
					, "Failed to get user with login name : " + usr + ",  login password : " + pwd );
		}
		System.out.println(companyBlo);

	}

}
