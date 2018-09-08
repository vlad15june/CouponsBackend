package org.coupons.pojo;

import java.util.List;

public class Customer extends User {

	private String firstName;
	private String lastName;
	private List<Coupon> coupons;

	public Customer() {
		setRole(Role.CUSTOMER);
	}

	public Customer(String firstName, String lastName, String email, String password) {
		setEmail(email);
		setFirstName(firstName);
		setLastName(lastName);
		setPassword(password);
		setRole(Role.CUSTOMER);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Coupon> getCoupons() {
		return coupons;
	}

	public void setCoupons(List<Coupon> coupons) {
		this.coupons = coupons;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((coupons == null) ? 0 : coupons.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		if (coupons == null) {
			if (other.coupons != null)
				return false;
		} else if (!coupons.equals(other.coupons))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Customer [firstName=" + firstName + ", lastName=" + lastName + ", coupons=" + coupons + ", getId()="
				+ getUserId() + ", getEmail()=" + getEmail() + ", getPassword()=" + getPassword() + ", getRole()="
				+ getRole() + "]";
	}
}