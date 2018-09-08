package org.coupons.dbo;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.coupons.pojo.Customer;
import org.coupons.pojo.Role;
import org.coupons.util.Algorithms;
import org.coupons.util.ConnectionPool;
import org.coupons.util.Hasher;


public final class CustomerDAO {

	public static Customer addCustomer(final Customer customer) throws SQLException {

		final String password = Hasher.hashEncode(customer.getPassword().getBytes(), Algorithms.SHA256);

		final String call = "CALL `couponSystem`.`addCustomer`(?, ?, ?, ?, ?)";

		try (final Connection connection = ConnectionPool.getConnection();
			final CallableStatement callableStatement = connection.prepareCall(call)) {

			callableStatement.setString(1, customer.getEmail());
			callableStatement.setString(2, password);
			callableStatement.setString(3, Role.CUSTOMER.name());
			callableStatement.setString(4, customer.getFirstName());
			callableStatement.setString(5, customer.getLastName());

			final ResultSet resultSet = callableStatement.executeQuery();

			if (resultSet.next()) {
				customer.setUserId(resultSet.getString("@id"));
				customer.setPassword(password);
			}
		}

		return customer;
	}

	public static Customer updateCustomer(final Customer customer) throws SQLException {

		final String password = Hasher.hashEncode(customer.getPassword().getBytes(), Algorithms.SHA256);

		final String call = "CALL `couponSystem`.`updateCustomer`(?, ?, ?, ?, ?)";

		try (final Connection connection = ConnectionPool.getConnection();
			final CallableStatement callableStatement = connection.prepareCall(call)) {

			callableStatement.setString(1, customer.getUserId());
			callableStatement.setString(2, customer.getEmail());
			callableStatement.setString(3, password);
			callableStatement.setString(4, customer.getFirstName());
			callableStatement.setString(5, customer.getLastName());

			callableStatement.executeUpdate();

			return getOneCustomer(customer.getUserId());
		}
	}

	public static void deleteCustomer(final String customerId) throws SQLException {

		final String call = "CALL `couponSystem`.`deleteCustomer`(?)";

		try (final Connection connection = ConnectionPool.getConnection();
			final CallableStatement callableStatement = connection.prepareCall(call)) {

			callableStatement.setString(1, customerId);

			callableStatement.executeUpdate();
		}
	}

	public static List<Customer> getAllCustomers() throws SQLException {

		final String sql = "select u.USER_ID, u.EMAIL, u.PASSWORD, c.FIRST_NAME, c.LAST_NAME "
		+ "from couponSystem.USERS as u right join couponSystem.CUSTOMERS as c "
		+ "on u.USER_ID = c.CUSTOMER_ID";

		try (final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			final ResultSet resultSet = preparedStatement.executeQuery();

			final List<Customer> customers = new ArrayList<>();

			while (resultSet.next()) {
				final Customer c = new Customer();
				c.setEmail(resultSet.getString("EMAIL"));
				c.setFirstName(resultSet.getString("FIRST_NAME"));
				c.setLastName(resultSet.getString("LAST_NAME"));
				c.setPassword(resultSet.getString("PASSWORD"));
				c.setUserId(resultSet.getString("USER_ID"));
				c.setCoupons(CouponDAO.getPurchasedCoupons(c.getUserId()));
				customers.add(c);
			}

			return customers;
		}
	}

	public static Customer getOneCustomer(final String customerId) throws SQLException {

		final String sql = "select u.USER_ID, u.EMAIL, u.PASSWORD, c.FIRST_NAME, c.LAST_NAME "
		+ "from couponSystem.USERS as u right join couponSystem.CUSTOMERS as c "
		+ "on u.USER_ID = c.CUSTOMER_ID " + "where USER_ID = ?";

		try (final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, customerId);

			final ResultSet resultSet = preparedStatement.executeQuery();

			final Customer c = new Customer();

			if (resultSet.next()) {
				c.setEmail(resultSet.getString("EMAIL"));
				c.setFirstName(resultSet.getString("FIRST_NAME"));
				c.setLastName(resultSet.getString("LAST_NAME"));
				c.setPassword(resultSet.getString("PASSWORD"));
				c.setUserId(resultSet.getString("USER_ID"));
				c.setCoupons(CouponDAO.getPurchasedCoupons(c.getUserId()));
			}

			return c;
		}
	}
}
