package org.coupons.util;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPool {

	public static final String UUID_GENERATE = "unhex(replace(uuid(),'-',''))";

	private static final HikariConfig cfg = new HikariConfig();
	private static HikariDataSource ds;
	
	static {
		cfg.setJdbcUrl("jdbc:mysql://localhost:3306/couponSystem?useSSL=false");
        cfg.setUsername("nick");
        cfg.setPassword("Nick292019");
        cfg.setDriverClassName("com.mysql.cj.jdbc.Driver");
        cfg.addDataSourceProperty("cachePrepStmts", "true");
        cfg.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource(cfg);
	}

	public static Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

}