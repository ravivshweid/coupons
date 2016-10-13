package com.raviv.coupons.enums;

public enum ErrorType {
	
	CUSTOMER_NAME_ALREADY_EXISTS(1),
	COUPON_NAME_ALREADY_EXISTS(2),
	INVALID_EMAIL(3),
	GENERAL_ERROR(4),
	
	DAO_CREATE_ERROR(5),
	DAO_GET_ERROR(6),
	DAO_UPDATE_ERROR(7),
	DAO_DELETE_ERROR(8);

	
	private int internalErrorCode;
	
	ErrorType(int internalErrorCode){
		this.internalErrorCode = internalErrorCode;
	}
	
	public int getInternalErrorCode() {
		return internalErrorCode;
	}
	
}	
