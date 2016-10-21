package com.raviv.coupons.system.tests;

import java.sql.Timestamp;

import com.raviv.coupons.beans.Coupon;
import com.raviv.coupons.blo.CompanyBlo;
import com.raviv.coupons.enums.CouponType;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemCompanyBlo210createCouponTest {

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
		//System.out.println(companyBlo);
				
		
		Coupon 	coupon 		= new Coupon();

		coupon.setCouponTitle("couponTitle");		

		CouponType  couponType = CouponType.ENTERTAINMENT;
		int couponTypeId = couponType.getCouponType();
		coupon.setCouponTypeId(couponTypeId);

		
		Timestamp ts = Timestamp.valueOf(String.format("%04d-%02d-%02d 00:00:00", 2016 , 10 ,  21 ));
		long couponStartDate = ts.getTime();
		coupon.setCouponStartDate(couponStartDate);
		
		ts = Timestamp.valueOf(String.format("%04d-%02d-%02d 00:00:00", 2016 , 12 ,  31 ));
		long couponEndDate = ts.getTime();
		coupon.setCouponEndDate(couponEndDate);

		
		coupon.setCouponMessage("couponMessage");
		coupon.setCouponPrice(75);
		coupon.setCouponsInStock(100);
		coupon.setImageFileName("imageFileName");
		
		
		//System.out.println(company);
		
		companyBlo.createCoupon(coupon);

		//System.out.println(user);
		//System.out.println(company);

	}

}
