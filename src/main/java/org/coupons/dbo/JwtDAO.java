package org.coupons.dbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.coupons.security.pojo.TokenContainer;
import org.coupons.util.ConnectionPool;
import org.coupons.util.Constants;

public class JwtDAO {
	
	public static void addToken(final TokenContainer container, final String jwtId) throws SQLException {
		
		final String refreshToken = container.getSignedRefreshToken();
		final Instant refreshExperationTime = Instant.now().plus(Constants.REFRESH_DURATION_IN_MINUTES, ChronoUnit.MINUTES);
		final long refreshExperationTimeInSeconds = Instant.ofEpochSecond(0L).until(refreshExperationTime, ChronoUnit.SECONDS);
		
		final String sql = "insert into JWT values(?,?,?)";
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, jwtId);
			preparedStatement.setString(2, refreshToken);
			preparedStatement.setLong(3, refreshExperationTimeInSeconds);
			
			preparedStatement.executeUpdate();
		}	
	}

	public static String getToken(final String jwtId) throws SQLException {
		
		final String sql = "select * from JWT where TOKEN_ID like ?";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, jwtId);
			final ResultSet resultSet = preparedStatement.executeQuery();
			
			if(resultSet.next()) {
				return resultSet.getString("REFRESH_TOKEN");
			}else
				return "";
		}
	}
	
	public static void deleteToken(final String jwtId) throws SQLException {
		
		final String sql = "delete from JWT where TOKEN_ID = ?";
		
		try(final Connection connection = ConnectionPool.getConnection();
			final PreparedStatement preparedStatement = connection.prepareStatement(sql)){
			
			preparedStatement.setString(1, jwtId);
			preparedStatement.executeUpdate();
		}
	}
}