package com.raviv.coupons.beans;

public class Company extends InfraBean {

	private	long                     	companyId;
//	private	long                     	sysCreationDate;
//	private	long                     	sysUpdateDate;
//	private	int                      	createdByUserId;
//	private	int                      	updatedByUserId;
	private	int                      	userId;
	private	String                   	companyName;
	private	String                   	companyEmail;
	
	
	
	
	@Override
	public String toString() {
		return super.toString() + "Company\t[companyId=" + companyId + ", userId=" + userId + ", companyName=" + companyName + ", companyEmail=" + companyEmail + "]\n";
	}
	
	
	
	
	
	public long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyEmail() {
		return companyEmail;
	}
	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}
	
	
}
