package com.raviv.coupons.system.tests;

import com.raviv.coupons.blo.CustomerBlo;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemCustomerBlo320getCustomerCouponsTest {

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
		 * Get customer coupons
		 */
		customerBlo.getCustomerCoupons();

	}

}
