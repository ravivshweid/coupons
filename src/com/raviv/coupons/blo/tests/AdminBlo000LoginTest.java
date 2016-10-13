package com.raviv.coupons.blo.tests;

import com.raviv.coupons.blo.AdminBlo;
import com.raviv.coupons.exceptions.ApplicationException;

public class AdminBlo000LoginTest {

	public static void main(String[] args) throws ApplicationException 
	{
		AdminBlo adminBlo = new AdminBlo();
		
		adminBlo.login("admin", "1234");

	}

}
