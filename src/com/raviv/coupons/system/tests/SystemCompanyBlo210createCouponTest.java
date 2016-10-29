package com.raviv.coupons.system.tests;

import com.raviv.coupons.beans.Coupon;
import com.raviv.coupons.blo.CompanyBlo;
import com.raviv.coupons.enums.CouponType;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;
import com.raviv.coupons.utils.YyyyMmDd;

public class SystemCompanyBlo210createCouponTest {

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
			
		Coupon 	coupon;
		/**
		 *  Create 1st coupon
		 */
		coupon	= new Coupon (
								"coupon1 Title"
								, new YyyyMmDd ("20160101")
								, new YyyyMmDd ("20160601")
								, 100
								, CouponType.ENTERTAINMENT.getCouponType()
								, "coupon1 message"						
								, 70 
								, "1 imageFileName"	
							 ) ;
		
		companyBlo.createCoupon(coupon);

		/**
		 * 
		 *  Create 2nd coupon
		 */
		coupon	= new Coupon (
								"coupon2 Title"
								, new YyyyMmDd ("20160602")
								, new YyyyMmDd ("20161231")
								, 100
								, CouponType.HOLIDAY.getCouponType()
								, "coupon2 message"						
								, 80 
								, "2 imageFileName"	
							 ) ;
		companyBlo.createCoupon(coupon);


		/**
		 * 
		 *  Create 3rd coupon
		 */
		coupon	= new Coupon (
								"coupon3 Title"
								, new YyyyMmDd ("20170602")
								, new YyyyMmDd ("20171231")
								, 100
								, CouponType.HOLIDAY.getCouponType()
								, "coupon3 message"						
								, 90 
								, "3 imageFileName"	
							 ) ;
		companyBlo.createCoupon(coupon);

		
		
	}

}
