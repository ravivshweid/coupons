package com.raviv.coupons.tests.system;

import com.raviv.coupons.beans.Coupon;
import com.raviv.coupons.blo.CompanyBlo;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;
import com.raviv.coupons.utils.YyyyMmDd;

public class SystemCompanyBlo230updateCouponTest {

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
		 * Update coupon
		 */
		companyBlo.updateCoupon(  new Coupon ( 3 , new YyyyMmDd("20171231") , 90)   );

	}

}
