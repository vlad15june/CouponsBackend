package org.coupons.dbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.coupons.util.Algorithms;
import org.coupons.util.ConnectionPool;
import org.coupons.util.Hasher;
import org.coupons.util.StrUtil;

import com.google.gson.JsonElement;


public final class UserDAO {

	public static boolean isUserExists(final JsonElement email, final JsonElement password) throws SQLException {
		
		if(email == null || password == null)
			return false;
		
		final String strPass = password.getAsString();
		final String strEmail = email.getAsString();
		
		if(StrUtil.isEmpty(strEmail) || StrUtil.isEmpty(strPass))
			return false;

		final String hashedPass = Hasher.hashEncode(strPass.getBytes(), Algorithms.SHA256);

		final String sql = "select * from USERS where EMAIL like ? and PASSWORD like ?";

		try (final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, strEmail);
			preparedStatement.setString(2, hashedPass);
			final ResultSet resultset = preparedStatement.executeQuery();

			if (resultset.next())
				return true;
			else
				return false;

		}
	}

	public static String getUserId(final String email) throws SQLException {
		
		final String sql = "select USER_ID from USERS where EMAIL like ?";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, email);
			final ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				String id = resultSet.getString("USER_ID");
				return id;
			}
			return null;
		}
	}
}
