package com.raviv.coupons.system.tests;

import java.sql.Timestamp;

import com.raviv.coupons.beans.Coupon;
import com.raviv.coupons.blo.CompanyBlo;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemCompanyBlo230updateCouponTest {

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
		
		Coupon 	coupon 		= new Coupon();

		coupon.setCouponId(1);
		
		Timestamp ts;
		
		
		ts = Timestamp.valueOf(String.format("%04d-%02d-%02d 00:00:00", 2017 , 12 ,  31 ));
		long couponEndDate = ts.getTime();
		coupon.setCouponEndDate(couponEndDate);		
		coupon.setCouponPrice(77);		
		
		//System.out.println(company);
		
		companyBlo.updateCoupon(coupon);

		//System.out.println(user);
		//System.out.println(company);

	}

}
