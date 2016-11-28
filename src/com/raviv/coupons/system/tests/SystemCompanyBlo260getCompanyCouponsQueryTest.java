package com.raviv.coupons.system.tests;

import com.raviv.coupons.blo.CompanyBlo;
import com.raviv.coupons.blo.DynamicQueryParameters;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemCompanyBlo260getCompanyCouponsQueryTest {

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
		 * Get company coupons with dynamicQueryParameters
		 */
		
		DynamicQueryParameters dynamicQueryParameters = new DynamicQueryParameters();

		//dynamicQueryParameters.add(DynamicQueryParameters.COUPON_TYPE_ID	, "3"			);
		dynamicQueryParameters.add(DynamicQueryParameters.FROM_PRICE		, "65"			);
		dynamicQueryParameters.add(DynamicQueryParameters.TO_PRICE			, "95"			);
		//dynamicQueryParameters.add(DynamicQueryParameters.FROM_DATE			, "20160602"	);
		dynamicQueryParameters.add(DynamicQueryParameters.TO_DATE  			, "20170601"	);
		
		companyBlo.getCouponsQuery( dynamicQueryParameters );
		
	}

}