package org.coupons.dbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.coupons.pojo.Category;
import org.coupons.pojo.Coupon;
import org.coupons.util.ConnectionPool;

public final class CouponDAO {

	public static List<Coupon> getAllCoupons() throws SQLException {
		
		final String sql = "select * from COUPONS";
		
		try (final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			final ResultSet resultSet = preparedStatement.executeQuery();
			final List<Coupon> coupons = new ArrayList<>();

			return fillCouponList(resultSet, coupons);
		}
	}
	
	public static List<Coupon> getPurchasedCoupons(final String customerId) throws SQLException {
		
		final String sql = "select coup.COUPON_ID, coup.COMPANY_ID, coup.CATEGORY, coup.TITLE, " +
				"coup.DESCRIPTION, coup.START_DATE, coup.EXPIRY_DATE, " + 
				"coup.AMOUNT, coup.PRICE, coup.IMAGE_URL " + 
				"from COUPONS as coup join PURCHASES as pur " + 
				"on coup.COUPON_ID = pur.COUPON_ID " + 
				"where pur.CUSTOMER_ID = ?";
		
		return executeGetAllQuery(sql, customerId);
	}
	
	public static List<Coupon> getAllOwnedCoupons(final String companyId) throws SQLException{
		
		final String sql = "select coup.COUPON_ID, coup.COMPANY_ID, coup.CATEGORY, coup.TITLE, " +
				"coup.DESCRIPTION, coup.START_DATE, coup.EXPIRY_DATE, " + 
				"coup.AMOUNT, coup.PRICE, coup.IMAGE_URL " + 
				"from COUPONS as coup join COMPANIES as comp " + 
				"on coup.COMPANY_ID = comp.COMPANY_ID " + 
				"where comp.COMPANY_ID = ?";
		
		return executeGetAllQuery(sql, companyId);
	}

	public static List<Coupon> getAllCouponsByCategory(final Category category) throws SQLException {
		
		final String sql = "select * from COUPONS where CATEGORY like ?";

		try (final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, category.name());

			final ResultSet resultSet = preparedStatement.executeQuery();
			final List<Coupon> coupons = new ArrayList<>();

			return fillCouponList(resultSet, coupons);
		}
	}
	
	public static List<Coupon> getPurchasedCouponsByCategory(final String customerId, final Category category) throws SQLException {
		
		final String sql = "select coup.COUPON_ID, coup.COMPANY_ID, coup.CATEGORY, coup.TITLE, " +
				"coup.DESCRIPTION, coup.START_DATE, coup.EXPIRY_DATE, " + 
				"coup.AMOUNT, coup.PRICE, coup.IMAGE_URL " + 
				"from COUPONS as coup join PURCHASES as pur " + 
				"on coup.COUPON_ID = pur.COUPON_ID " + 
				"where pur.CUSTOMER_ID = ? AND coup.CATEGORY = ?";
		
		return executeGetByCategoryQuery(sql, customerId, category);
	}
	
	public static List<Coupon> getOwnedCouponsByCategory(final String companyId, final Category category) throws SQLException {
		
		final String sql = "select coup.COUPON_ID, coup.COMPANY_ID, coup.CATEGORY, coup.TITLE, " +
				"coup.DESCRIPTION, coup.START_DATE, coup.EXPIRY_DATE, " + 
				"coup.AMOUNT, coup.PRICE, coup.IMAGE_URL " + 
				"from COUPONS as coup join COMPANIES as comp " + 
				"on coup.COMPANY_ID = comp.COMPANY_ID " + 
				"where comp.COMPANY_ID = ? AND coup.CATEGORY = ?";
		
		return executeGetByCategoryQuery(sql, companyId, category);
	}

	public static List<Coupon> getAllCouponsByPrice(final double price) throws SQLException {
		
		final String sql = "select * from COUPONS where PRICE <= ?";

		try (final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setDouble(1, price);

			final ResultSet resultSet = preparedStatement.executeQuery();
			final List<Coupon> coupons = new ArrayList<>();

			return fillCouponList(resultSet, coupons);
		}
	}
	
	public static List<Coupon> getPurchasedCouponsByPrice(final String customerId, final double price) throws SQLException {
		
		final String sql = "select coup.COUPON_ID, coup.COMPANY_ID, coup.CATEGORY, coup.TITLE, " +
				"coup.DESCRIPTION, coup.START_DATE, coup.EXPIRY_DATE, " + 
				"coup.AMOUNT, coup.PRICE, coup.IMAGE_URL " + 
				"from COUPONS as coup join PURCHASES as pur " + 
				"on coup.COUPON_ID = pur.COUPON_ID " + 
				"where pur.CUSTOMER_ID = ? AND coup.PRICE <= ?";
		
		return executeGetByPriceQuery(sql, customerId, price);
	}
	
	public static List<Coupon> getOwnedCouponsByPrice(final String companyId, final double price) throws SQLException {
		
		final String sql = "select coup.COUPON_ID, coup.COMPANY_ID, coup.CATEGORY, coup.TITLE, " +
				"coup.DESCRIPTION, coup.START_DATE, coup.EXPIRY_DATE, " + 
				"coup.AMOUNT, coup.PRICE, coup.IMAGE_URL " + 
				"from COUPONS as coup join COMPANIES as comp " + 
				"on coup.COMPANY_ID = comp.COMPANY_ID " + 
				"where comp.COMPANY_ID = ? AND coup.PRICE <= ?";
		
		return executeGetByPriceQuery(sql, companyId, price);
	}

	public static Coupon getOneCoupon(final String couponId) throws SQLException {
		
		final String sql = "select * from COUPONS where COUPON_ID = ?";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, couponId);
			final ResultSet resultSet = preparedStatement.executeQuery();
			
			final Coupon coup = new Coupon();
			
			if(resultSet.next()) {
				coup.setAmount(resultSet.getInt("AMOUNT"));
				coup.setCategory(Category.valueOf(resultSet.getString("CATEGORY")));
				coup.setCompanyId(resultSet.getString("COMPANY_ID"));
				coup.setCouponId(resultSet.getString("COUPON_ID"));
				coup.setDescription(resultSet.getString("DESCRIPTION"));
				coup.setExpiryDate(resultSet.getDate("EXPIRY_DATE"));
				coup.setImageUrl(resultSet.getString("IMAGE_URL"));
				coup.setPrice(resultSet.getDouble("PRICE"));
				coup.setStartDate(resultSet.getDate("START_DATE"));
				coup.setTitle(resultSet.getString("TITLE"));
				return coup;
			}
			
			return null;	
		}
	}
	
	public static Coupon addCoupon(final Coupon coupon) throws SQLException {
		
		final String sql = "CALL `couponSystem`.`addCoupon`(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, coupon.getCompanyId());
			preparedStatement.setString(2, coupon.getCategory().name());
			preparedStatement.setString(3, coupon.getTitle());
			preparedStatement.setString(4, coupon.getDescription());
			preparedStatement.setDate(5, coupon.getStartDate());
			preparedStatement.setDate(6, coupon.getExpiryDate());
			preparedStatement.setInt(7, coupon.getAmount());
			preparedStatement.setDouble(8, coupon.getPrice());
			preparedStatement.setString(9, coupon.getImageUrl());
			
			final ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				Coupon coup = new Coupon();
				coup.setAmount(resultSet.getInt("AMOUNT"));
				coup.setCategory(Category.valueOf(resultSet.getString("CATEGORY")));
				coup.setCompanyId(resultSet.getString("COMPANY_ID"));
				coup.setCouponId(resultSet.getString("COUPON_ID"));
				coup.setDescription(resultSet.getString("DESCRIPTION"));
				coup.setExpiryDate(resultSet.getDate("EXPIRY_DATE"));
				coup.setImageUrl(resultSet.getString("IMAGE_URL"));
				coup.setPrice(resultSet.getDouble("PRICE"));
				coup.setStartDate(resultSet.getDate("START_DATE"));
				coup.setTitle(resultSet.getString("TITLE"));
				return coup;
			}
			return null;
		}
	}

	public static void updateCoupon(final Coupon coupon) throws SQLException {
		
		final String sql = "update COUPONS " + 
				"set COMPANY_ID = ?, CATEGORY = ?, TITLE = ?, DESCRIPTION = ?, " + 
				"START_DATE = ?, EXPIRY_DATE = ?, AMOUNT = ?, PRICE = ?, IMAGE_URL = ? " + 
				"where COUPON_ID = ?";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, coupon.getCompanyId());
			preparedStatement.setString(2, coupon.getCategory().name());
			preparedStatement.setString(3, coupon.getTitle());
			preparedStatement.setString(4, coupon.getDescription());
			preparedStatement.setDate(5, coupon.getStartDate());
			preparedStatement.setDate(6, coupon.getExpiryDate());
			preparedStatement.setInt(7, coupon.getAmount());
			preparedStatement.setDouble(8, coupon.getPrice());
			preparedStatement.setString(9, coupon.getImageUrl());
			preparedStatement.setString(10, coupon.getCouponId());
			
			preparedStatement.executeUpdate();
		}
	}

	public static void deleteCoupon(final Coupon coupon) throws SQLException {
		
		final String sql = "delete from COUPONS where COUPON_ID = ? AND COMPANY_ID = ?";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, coupon.getCouponId());
			preparedStatement.setString(2, coupon.getCompanyId());
			
			preparedStatement.executeUpdate();
		}
		
	}

	public static void addPurchase(final String customerId, final Coupon coupon) throws SQLException{
		
		final String sql = "insert into PURCHASES " + 
				"values(?, ?, ?)";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, customerId);
			preparedStatement.setString(2, coupon.getCouponId());
			preparedStatement.setString(3, coupon.getCompanyId());
			
			preparedStatement.executeUpdate();
		}
	}

	public static void deletePurchaseByCustomer(final String customerId, final String couponId) throws SQLException {
		
		final String sql = "delete from PURCHASES where CUSTOMER_ID = ? and COUPON_ID = ?";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, customerId);
			preparedStatement.setString(2, couponId);
			
			preparedStatement.executeUpdate();
		}
	}
	
	public static void deletePurchaseByCompany(final String companyId, final String couponId) throws SQLException {
		
		final String sql = "delete from PURCHASES where COMPANY_ID = ? and COUPON_ID = ?";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, companyId);
			preparedStatement.setString(2, couponId);
			
			preparedStatement.executeUpdate();
		}
	}
	
	private static List<Coupon> executeGetAllQuery(final String sql, final String id) throws SQLException {
		
		final List<Coupon> coupons = new ArrayList<>();
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, id);
			final ResultSet resultSet = preparedStatement.executeQuery();
			
			return fillCouponList(resultSet, coupons);	
		}
	}
	
	private static List<Coupon> executeGetByCategoryQuery(final String sql, final String id, final Category category) throws SQLException {
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, id);
			preparedStatement.setString(2, category.name());
			
			final ResultSet resultSet = preparedStatement.executeQuery();
			final List<Coupon> coupons = new ArrayList<>();
			
			return fillCouponList(resultSet, coupons);
		}
	}
	
	private static List<Coupon> executeGetByPriceQuery(final String sql, final String id, final double price) throws SQLException {
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, id);
			preparedStatement.setDouble(2, price);
			
			final ResultSet resultSet = preparedStatement.executeQuery();
			final List<Coupon> coupons = new ArrayList<>();
			
			return fillCouponList(resultSet, coupons);
			
		}
	}

	private static List<Coupon> fillCouponList(final ResultSet resultSet, final List<Coupon> coupons) throws SQLException {
		
		while(resultSet.next()) {
			final Coupon coup = new Coupon();
			coup.setAmount(resultSet.getInt("AMOUNT"));
			coup.setCategory(Category.valueOf(resultSet.getString("CATEGORY")));
			coup.setCompanyId(resultSet.getString("COMPANY_ID"));
			coup.setCouponId(resultSet.getString("COUPON_ID"));
			coup.setDescription(resultSet.getString("DESCRIPTION"));
			coup.setExpiryDate(resultSet.getDate("EXPIRY_DATE"));
			coup.setImageUrl(resultSet.getString("IMAGE_URL"));
			coup.setPrice(resultSet.getDouble("PRICE"));
			coup.setStartDate(resultSet.getDate("START_DATE"));
			coup.setTitle(resultSet.getString("TITLE"));
			coupons.add(coup);
		}
		
		return coupons;
	}
	
}









