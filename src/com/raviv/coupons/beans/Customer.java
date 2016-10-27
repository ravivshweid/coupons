package com.raviv.coupons.beans;

public class Customer extends InfraBean {

	private	long                     	customerId;
//	private	long                     	sysCreationDate;
//	private	long                     	sysUpdateDate;
//	private	int                      	createdByUserId;
//	private	int                      	updatedByUserId;
	private	int                      	userId;
	private	String                   	customerName;
	
	
	
	
	
	
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
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