package org.coupons.security.pojo;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TokenContainer {

	private String signedRefreshToken;
	private String signedAccessToken;
	private long accessTokenExpirationDate;

	public TokenContainer(String signedRefreshToken, String signedAccessToken, long accessTokenExpirationDate) {
		super();
		this.signedRefreshToken = signedRefreshToken;
		this.signedAccessToken = signedAccessToken;
		this.accessTokenExpirationDate = Instant.now().plus(accessTokenExpirationDate, ChronoUnit.MINUTES)
				.toEpochMilli();
	}

	public String toJsonString() throws JsonProcessingException {
	
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String jsonString = gson.toJson(this);

		return jsonString;
	}

	public String getSignedRefreshToken() {
		return signedRefreshToken;
	}

	public void setSignedRefreshToken(String signedRefreshToken) {
		this.signedRefreshToken = signedRefreshToken;
	}

	public String getSignedAccessToken() {
		return signedAccessToken;
	}

	public void setSignedAccessToken(String signedAccessToken) {
		this.signedAccessToken = signedAccessToken;
	}

	public long getAccessTokenExpirationDate() {
		return accessTokenExpirationDate;
	}

	public void setAccessTokenExpirationDate(long accessTokenExpirationDate) {
		this.accessTokenExpirationDate = accessTokenExpirationDate;
	}

	@Override
	public String toString() {
		return "TokenContainer [signedRefreshToken=" + signedRefreshToken + ", signedAccessToken=" + signedAccessToken
				+ ", accessTokenExpirationDate=" + accessTokenExpirationDate + "]";
	}

}
