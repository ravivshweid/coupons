package com.raviv.coupons.tests.system;

import com.raviv.coupons.blo.CustomerBlo;
import com.raviv.coupons.blo.DynamicQueryParameters;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemCustomerBlo330getCustomerCouponsQueryTest {

	public static void main(String[] args) throws ApplicationException {
		/**
		 * login as company and get companyBlo
		 */
		CouponsSystem couponsSystem = CouponsSystem.getInstance();
		CustomerBlo customerBlo;
		try {
			customerBlo = (CustomerBlo) couponsSystem.login("cust2", "1234");
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.LOGIN_ERROR, e, "Login failed");
		}

		/**
		 * Get customer coupons with dynamicQueryParameters
		 */
		
		DynamicQueryParameters dynamicQueryParameters = new DynamicQueryParameters();

		dynamicQueryParameters.add(DynamicQueryParameters.COUPON_TYPE_ID	, "1"			);
		dynamicQueryParameters.add(DynamicQueryParameters.FROM_PRICE		, "85"			);
		dynamicQueryParameters.add(DynamicQueryParameters.TO_PRICE			, "95"			);

		
		customerBlo.getCustomerCouponsQuery(dynamicQueryParameters);

	}

}
