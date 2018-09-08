package org.coupons.facade;

import java.sql.SQLException;
import java.util.List;

import org.coupons.dbo.CompanyDAO;
import org.coupons.dbo.CouponDAO;
import org.coupons.pojo.Category;
import org.coupons.pojo.Company;
import org.coupons.pojo.Coupon;
import org.coupons.pojo.Role;
import org.coupons.security.AuthHelper;

public class CompanyFacade {
	
	public static Coupon addCoupon(final Coupon coupon, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.COMPANY.name().equalsIgnoreCase(userRole))
			return null;
		
		final List<Coupon> coupons = CouponDAO.getAllOwnedCoupons(userId);
		
		for (Coupon c : coupons) {
			if(c.getTitle().equalsIgnoreCase(coupon.getTitle()))
				return null;
		}
		
		return CouponDAO.addCoupon(coupon);
	}
	
	public static void updateCoupon(final Coupon coupon, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.COMPANY.name().equalsIgnoreCase(userRole))
			return;
		
		if(!coupon.getCompanyId().equals(userId))
			return;
		
		CouponDAO.updateCoupon(coupon);
	}
	
	public static void deleteCoupon(final Coupon coupon, final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.COMPANY.name().equalsIgnoreCase(userRole))
			return;
		
		if(!coupon.getCompanyId().equals(userId))
			return;
		
		CouponDAO.deleteCoupon(coupon);
	}
	
	public static List<Coupon> getAllCoupons(final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.COMPANY.name().equalsIgnoreCase(userRole))
			return null;
		
		return CouponDAO.getAllOwnedCoupons(userId);
	}
	
	public static List<Coupon> getAllCouponsByCategory(final String payload, final Category category) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.COMPANY.name().equalsIgnoreCase(userRole))
			return null;
		
		return CouponDAO.getOwnedCouponsByCategory(userId, category);
	}
	
	public static List<Coupon> getAllCouponsByPrice(final String payload, final double price) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.COMPANY.name().equalsIgnoreCase(userRole))
			return null;
		
		return CouponDAO.getOwnedCouponsByPrice(userId, price);
	}
	
	public static Company getSelfDetails(final String payload) throws SQLException {
		final String userRole = AuthHelper.extractValueFromToken(payload, "role");
		final String userId = AuthHelper.extractValueFromToken(payload, "userId");
		
		if(!Role.COMPANY.name().equalsIgnoreCase(userRole))
			return null;
		
		return CompanyDAO.getOneCompany(userId);
	}
	
}








