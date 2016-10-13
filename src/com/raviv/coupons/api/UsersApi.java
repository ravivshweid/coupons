package com.raviv.coupons.api;

import com.raviv.coupons.beans.User;
import com.raviv.coupons.blo.UsersController;
import com.raviv.coupons.exceptions.ApplicationException;

public class UsersApi {

	UsersController usersControler;

	public UsersApi()
	{
		usersControler = new UsersController();
	}
	
	
public void createUser(User user) throws ApplicationException {

	usersControler.createUser(user);
		
	}
	

}
