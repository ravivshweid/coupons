package com.raviv.coupons.system.tests;

import com.raviv.coupons.blo.AdminBlo;
import com.raviv.coupons.exceptions.ApplicationException;
import com.raviv.coupons.system.CouponsSystem;

public class SystemAdminBlo090getAllCustomersTest {

	public static void main(String[] args) throws ApplicationException 
	{
		
		CouponsSystem couponsSystem = CouponsSystem.getInstance();
		
		AdminBlo adminBlo;
		
		adminBlo = (AdminBlo) couponsSystem.login("admin", "1234");
		
		//System.out.println(adminBlo);
		
		adminBlo.getAllCustomers();

	}

}
