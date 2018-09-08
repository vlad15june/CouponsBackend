package org.coupons.util;

public final class StrUtil {

	private StrUtil() {
	}

	public static boolean isEmpty(final String str) {

		return str == null || str.length() == 0;
	}
}
