package org.coupons.util;

import org.coupons.pojo.Company;
import org.coupons.pojo.Coupon;
import org.coupons.pojo.Customer;
import org.coupons.pojo.User;
import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;


public class JSONConvertor {

	@SuppressWarnings("unchecked")
	public static JSONObject userToJSON(final User user) {
		final JSONObject json = new JSONObject();

		json.put("id", user.getUserId());
		json.put("email", user.getEmail());
		json.put("password", user.getPassword());
		json.put("role", user.getRole());

		return json;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject companyToJSON(final Company company) {
		final JSONObject json = new JSONObject();
		final JSONArray coupons = new JSONArray();

		for (Coupon coupon : company.getCoupons()) {
			coupons.add(couponToJSON(coupon));
		}

		json.put("name", company.getName());
		json.put("email", company.getEmail());
		json.put("role", company.getRole());
		json.put("userId", company.getUserId());
		json.put("coupons", coupons);

		return json;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject customerToJSON(final Customer customer) {
		final JSONObject json = new JSONObject();
		final JSONArray coupons = new JSONArray();

		for (Coupon coupon : customer.getCoupons()) {
			coupons.add(couponToJSON(coupon));
		}

		json.put("firstName", customer.getFirstName());
		json.put("lastName", customer.getLastName());
		json.put("email", customer.getEmail());
		json.put("role", customer.getRole());
		json.put("userId", customer.getUserId());
		json.put("coupons", coupons);

		return json;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject couponToJSON(final Coupon coupon) {
		final JSONObject json = new JSONObject();

		json.put("amount", coupon.getAmount());
		json.put("category", coupon.getCategory().name());
		json.put("companyId", coupon.getCompanyId());
		json.put("couponId", coupon.getCouponId());
		json.put("description", coupon.getDescription());
		json.put("expiryDate", coupon.getExpiryDate());
		json.put("imageUrl", coupon.getImageUrl());
		json.put("price", coupon.getPrice());
		json.put("startDate", coupon.getStartDate());
		json.put("title", coupon.getTitle());

		return json;
	}

}
