package org.coupons.jobs;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.coupons.dbo.CouponDAO;
import org.coupons.pojo.Coupon;

public class CouponExpirationDailyJob implements Runnable{
	
	private boolean quit = false;

	@Override
	public void run() {
		
		quit = false;
		
		final LocalTime startingPoint = LocalTime.of(04, 00, 00);
		final LocalTime endingPoint = LocalTime.of(04, 00, 05);
		LocalTime current;
		
		while(!quit) {
			try {
				Thread.sleep(1000);
				current = LocalTime.now();
				if(current.isAfter(startingPoint) && current.isBefore(endingPoint)) {
					deleteExpiredCoupons();
				}
			}catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	public void stop() {
		quit = true;
	}
	
	private void deleteExpiredCoupons() throws SQLException {
		
		List<Coupon> coupons = CouponDAO.getAllCoupons();
		
		for (Coupon coupon : coupons) {
			if(coupon.getExpiryDate().toLocalDate().isBefore(LocalDate.now()))
				CouponDAO.deleteCoupon(coupon);
		}
	}
	
}










