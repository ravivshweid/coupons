package com.raviv.coupons.blo.interfaces;

import com.raviv.coupons.beans.User;
import com.raviv.coupons.exceptions.ApplicationException;

public interface IClientBlo {
	
	public IClientBlo login (String loginName, String loginPassword) throws ApplicationException;

	public IClientBlo login (User user) throws ApplicationException;

}
