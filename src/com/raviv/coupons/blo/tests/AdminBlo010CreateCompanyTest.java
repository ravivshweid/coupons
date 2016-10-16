package com.raviv.coupons.blo.tests;

import com.raviv.coupons.beans.Company;
import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.AdminBlo;
import com.raviv.coupons.exceptions.ApplicationException;

public class AdminBlo010CreateCompanyTest {

	public static void main(String[] args) throws ApplicationException 
	{
		AdminBlo adminBlo = new AdminBlo();
		adminBlo.login("admin", "1234");

		User user = new User();		
		user.setUserName( "Raviv Shweid" );
		user.setLoginName( "raviv7" );
		user.setLoginPassword( "unix11" );
		//System.out.println(user);

		Company company = new Company();
		company.setCompanyName("Company test 6");
		company.setCompanyEmail("raviv.shweid@gmail.com");
		//System.out.println(company);
		
		adminBlo.createCompany(user, company);

		//System.out.println(user);
		//System.out.println(company);

		
	}

}
