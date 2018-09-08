package org.coupons.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import org.coupons.util.JsonUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class SecurityUtil {

	private static long CLOCK_SKEW_IN_SECONDS = 5L;
	
	private SecurityUtil() {}
	
	public static boolean expirationTimeCheck(final String payload) {
		byte[] decoded = Base64.getMimeDecoder().decode(payload);
		String payloadDecoded = new String(decoded);
		JsonObject payloadJson = JsonUtil.strToJsonObject(payloadDecoded);
		JsonElement expirationTimeInSeconds = payloadJson.get("exp");
		long nowTimeInSeconds = Instant.ofEpochSecond(0L).until(Instant.now(),
                ChronoUnit.SECONDS);
		
		long expTimeLong = expirationTimeInSeconds != null ? expirationTimeInSeconds.getAsLong() : -1L;
		
		return nowTimeInSeconds < expTimeLong + CLOCK_SKEW_IN_SECONDS;
	}
}
