package org.coupons.util;

public enum Algorithms {
	
	SHA256("SHA-256"),
	SHA512("SHA-512"),
	MD2("MD2"),
	MD5("MD5");
	
	private String alg;
	
	Algorithms(String alg) {
		this.alg = alg;
	}
	
	public String getAlgorithm() {
		return this.alg;
	}
	
}