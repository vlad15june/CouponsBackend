package org.coupons.dbo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.coupons.pojo.Company;
import org.coupons.util.Algorithms;
import org.coupons.util.ConnectionPool;
import org.coupons.util.Hasher;


public final class CompanyDAO {

	public static Company addCompany(final Company company) throws SQLException {

		final String password = Hasher.hashEncode(company.getPassword().getBytes(), Algorithms.SHA256);

		final String call = "{CALL `couponSystem`.`addCompany`(?, ?, ?)}";

		try (final Connection connection = ConnectionPool.getConnection();
			final CallableStatement callableStatement = connection.prepareCall(call)) {

			callableStatement.setString(1, company.getEmail());
			callableStatement.setString(2, password);
			callableStatement.setString(3, company.getName());

			final ResultSet resultSet = callableStatement.executeQuery();

			if (resultSet.next()) {
				company.setUserId(resultSet.getString("@id"));
				company.setPassword(password);
			}
		}

		return company;
	}

	public static Company updateCompany(final Company company) throws SQLException {

		final String password = Hasher.hashEncode(company.getPassword().getBytes(), Algorithms.SHA256);

		final String sql = "update USERS set EMAIL = ?, PASSWORD = ? where USER_ID = ?";

		try (final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, company.getEmail());
			preparedStatement.setString(2, password);
			preparedStatement.setString(3, company.getUserId());
			preparedStatement.executeUpdate();

			return getOneCompany(company.getUserId());
		}
	}

	public static void deleteCompany(final String companyId) throws SQLException {

		final String call = "{CALL `couponSystem`.`deleteCompany`(?)}";

		try (final Connection connection = ConnectionPool.getConnection();
			final CallableStatement callableStatement = connection.prepareCall(call)) {

			callableStatement.setString(1, companyId);
			callableStatement.executeUpdate();
		}
	}

	public static List<Company> getAllCompanies() throws SQLException {

		final String sql = "SELECT com.NAME, user.USER_ID, user.EMAIL, user.PASSWORD, user.ROLE "
		+ "FROM couponSystem.COMPANIES as com left join couponSystem.USERS as user "
		+ "on user.USER_ID = com.COMPANY_ID";

		final List<Company> companies = new ArrayList<>();

		try (final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			final ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				final Company company = new Company();
				company.setEmail(resultSet.getString("EMAIL"));
				company.setName(resultSet.getString("NAME"));
				company.setPassword(resultSet.getString("PASSWORD"));
				company.setUserId(resultSet.getString("USER_ID"));
				company.setCoupons(CouponDAO.getAllOwnedCoupons(company.getUserId()));
				companies.add(company);
			}
			return companies;
		}
	}

	public static Company getOneCompany(final String companyId) throws SQLException {

		final String sql = "SELECT com.NAME, user.USER_ID, user.EMAIL, user.PASSWORD, user.ROLE "
		+ "FROM couponSystem.COMPANIES as com left join couponSystem.USERS as user "
		+ "on user.USER_ID = com.COMPANY_ID where USER_ID = ?";

		try (final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, companyId);
			final ResultSet resultSet = preparedStatement.executeQuery();

			final Company company = new Company();

			if (resultSet.next()) {
				company.setEmail(resultSet.getString("EMAIL"));
				company.setName(resultSet.getString("NAME"));
				company.setPassword(resultSet.getString("PASSWORD"));
				company.setUserId(resultSet.getString("USER_ID"));
				company.setCoupons(CouponDAO.getAllOwnedCoupons(companyId));
			}
			return company;
		}
	}

}
