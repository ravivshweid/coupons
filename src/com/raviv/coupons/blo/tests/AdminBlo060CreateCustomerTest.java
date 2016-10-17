package com.raviv.coupons.blo.tests;

import com.raviv.coupons.beans.Customer;
import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.AdminBlo;
import com.raviv.coupons.exceptions.ApplicationException;

public class AdminBlo060CreateCustomerTest {

	public static void main(String[] args) throws ApplicationException 
	{
		AdminBlo adminBlo = new AdminBlo();
		adminBlo.login("admin", "1234");

		User user = new User();		
		user.setUserName( "Raviv Shweid" );
		user.setLoginName( "raviv7" );
		user.setLoginPassword( "unix11" );
		//System.out.println(user);

		Customer customer = new Customer();
		customer.setCustomerName("CUSTOMER 3");
		//System.out.println(company);
		
		adminBlo.createCustomer(user, customer);
		//System.out.println(user);
		//System.out.println(company);

		
	}

}
