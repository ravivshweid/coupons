package com.raviv.coupons.beans;

import java.util.List;

public class Customer extends InfraBean {

	private	long                     	customerId;
//	private	long                     	sysCreationDate;
//	private	long                     	sysUpdateDate;
//	private	int                      	createdByUserId;
//	private	int                      	updatedByUserId;
	private	int                      	userId;
	private	String                   	customerName;
	
	private List<Coupon>				coupons;
	
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Customer(String customerName) {
		super();
		setCustomerName( customerName );
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public String toString() {
		return super.toString() + "Customer\t[customerId=" + customerId + ", userId=" + userId + ", customerName=" + customerName + "]\n";
	}
	
	
	
	
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
}