package org.coupons.util;

import java.security.MessageDigest;

import javax.xml.bind.DatatypeConverter;

public class Hasher {
	
	//USED WITH SHA256 FOR PROJECT COUPONS
	public static String hashEncode(final byte[] bytes, final Algorithms algorithm) {
		String hashValue = "";
		try {
			final MessageDigest messageDigest = MessageDigest.getInstance(algorithm.getAlgorithm());
			messageDigest.update(bytes);
			final byte[] diggestedBytes = messageDigest.digest();
			hashValue = DatatypeConverter.printHexBinary(diggestedBytes).toLowerCase();
		} catch (Exception e) {}
		return hashValue;
	}
	
}