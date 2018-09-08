package org.coupons.facade;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.coupons.dbo.CouponDAO;
import org.coupons.dbo.CustomerDAO;
import org.coupons.pojo.Category;
import org.coupons.pojo.Coupon;
import org.coupons.pojo.Customer;
import org.coupons.pojo.Role;
import org.coupons.security.AuthHelper;


public class CustomerFacade {

	public static void purchaseCoupon(final String payload, Coupon coupon) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.CUSTOMER.name().equalsIgnoreCase(userRole))
			return;
		
		coupon = CouponDAO.getOneCoupon(coupon.getCouponId());

		if (coupon.getAmount() > 0 && coupon.getExpiryDate().toLocalDate().isBefore(LocalDate.now())) {
			
			coupon.setAmount(coupon.getAmount() - 1);
			CouponDAO.addPurchase(userId, coupon);
			CouponDAO.updateCoupon(coupon);
			
		} else {
			if (coupon.getAmount() < 1)
				return;
			if (coupon.getExpiryDate().toLocalDate().isBefore(LocalDate.now()))
				return;
		}
	}

	public static List<Coupon> getAllCoupons(final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(!Role.CUSTOMER.name().equalsIgnoreCase(userRole))
			return null;
		return CouponDAO.getAllCoupons();
	}

	public static List<Coupon> getAllCouponsByCategory(final String payload, final Category category) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(!Role.CUSTOMER.name().equalsIgnoreCase(userRole))
			return null;
		return CouponDAO.getAllCouponsByCategory(category);
	}
	
	public static List<Coupon> getAllCouponsByPrice(final String payload, final double price) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		
		if(!Role.CUSTOMER.name().equalsIgnoreCase(userRole))
			return null;
		return CouponDAO.getAllCouponsByPrice(price);
	}
	
	public static List<Coupon> getAllPurchasedCoupons(final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.CUSTOMER.name().equalsIgnoreCase(userRole))
			return null;
		return CouponDAO.getPurchasedCoupons(userId);
	}

	public static List<Coupon> getCouponsByCategory(final String payload, final Category category) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.CUSTOMER.name().equalsIgnoreCase(userRole))
			return null;
		return CouponDAO.getPurchasedCouponsByCategory(userId, category);
	}

	public static List<Coupon> getCouponsByPrice(final String payload, final double price) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.CUSTOMER.name().equalsIgnoreCase(userRole))
			return null;
		return CouponDAO.getPurchasedCouponsByPrice(userId, price);
	}

	public static Customer getSelfDetails(final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.CUSTOMER.name().equalsIgnoreCase(userRole))
			return null;
		return CustomerDAO.getOneCustomer(userId);
	}

}
