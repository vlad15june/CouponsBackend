package org.coupons.pojo;

public enum Role {
	
	ADMIN("admin"),
	COMPANY("company"),
	CUSTOMER("customer");
	
	private String role;

	Role(String role){
		this.role = role;
	}
	
	public String getRole() {
		return role;
	}
	
}