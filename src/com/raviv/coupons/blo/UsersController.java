package com.raviv.coupons.blo;

import com.raviv.coupons.beans.User;
import com.raviv.coupons.dao.UsersDao;
import com.raviv.coupons.enums.ErrorType;
import com.raviv.coupons.exceptions.ApplicationException;

public class UsersController {
	
	UsersDao usersDao;
	
	public UsersController()
	{
		usersDao = new UsersDao();
	}
	
	public void createUser(User user) throws ApplicationException {
		
		validateCreateUser(user);
		
		usersDao.createUser(user);
	}
	

	public void validateCreateUser(User user) throws ApplicationException {

		if ( user.getUserName() == null )
		{
			throw new ApplicationException (ErrorType.GENERAL_ERROR , null , "validateCreateUser error: getUserName is null");
		}	
	
	}

	

}
