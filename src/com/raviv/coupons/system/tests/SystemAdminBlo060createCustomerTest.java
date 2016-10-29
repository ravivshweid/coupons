package com.raviv.coupons.system.tests;

import com.raviv.coupons.beans.Customer;
import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.AdminBlo;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemAdminBlo060createCustomerTest {

	public static void main(String[] args) throws ApplicationException 
	{
		
		CouponsSystem couponsSystem = CouponsSystem.getInstance();
		
		AdminBlo adminBlo;
		
		adminBlo = (AdminBlo) couponsSystem.login("admin", "1234");
		
		//System.out.println(adminBlo);
		User user = new User( "Raviv Shweid" , "raviv9" , "unix11" );

		Customer customer = new Customer("CUSTOMER 4");
		
		adminBlo.createCustomer(user, customer);
		//System.out.println(user);
		//System.out.println(company);

	}

}
