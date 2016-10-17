package com.raviv.coupons.system.tests;

import com.raviv.coupons.beans.Customer;
import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.AdminBlo;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemAdminBlo060createCustomer {

	public static void main(String[] args) throws ApplicationException 
	{
		
		CouponsSystem couponsSystem = CouponsSystem.getInstance();
		
		AdminBlo adminBlo;
		
		adminBlo = (AdminBlo) couponsSystem.login("admin", "1234");
		
		
		//System.out.println(adminBlo);
		User user = new User();		
		user.setUserName( "Raviv Shweid" );
		user.setLoginName( "raviv8" );
		user.setLoginPassword( "unix11" );
		//System.out.println(user);

		Customer customer = new Customer();
		customer.setCustomerName("CUSTOMER 4");
		//System.out.println(company);
		
		adminBlo.createCustomer(user, customer);
		//System.out.println(user);
		//System.out.println(company);
		

	}

}
