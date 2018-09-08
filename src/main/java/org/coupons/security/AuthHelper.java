package org.coupons.security;

import static org.coupons.util.StrUtil.isEmpty;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.coupons.pojo.Role;
import org.coupons.security.pojo.TokenContainer;
import org.coupons.util.Constants;
import org.coupons.util.JsonUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;

public class AuthHelper {
	// TODO: put SIGNING_KEY to properties file
	private static final String SIGNING_KEY = "secret";

	public static String getTokenContainer(final String refreshToken, final String accessToken,
			final long accessTokenExpirationTime) throws Exception {

		String signedRefreshToken = Constants.REFRESH_TOKEN + signingTokenSH256(refreshToken);
		String signedAccessToken = Constants.BEARER_TOKEN + signingTokenSH256(accessToken);
		return new TokenContainer(signedRefreshToken, signedAccessToken, accessTokenExpirationTime).toJsonString();
	}

	public static String signingTokenSH256(String token) throws Exception {
		String hash = signHS256(token);

		return token + "." + hash;
	}

	private static String signHS256(String token) throws NoSuchAlgorithmException, InvalidKeyException {
		final String algName = Constants.SIGNING_ALGORITHM_FULL_NAME;
		Mac sha256_HMAC = Mac.getInstance(algName);
		SecretKeySpec secret_key = new SecretKeySpec(SIGNING_KEY.getBytes(), algName);
		sha256_HMAC.init(secret_key);

		String hash = Base64.getUrlEncoder().withoutPadding().encodeToString(sha256_HMAC.doFinal(token.getBytes()));
		return hash;
	}

	public static String createToken(JsonObject header, JsonObject payload) throws Exception {

		byte[] serializedHeader = header.toString().getBytes("UTF-8");
		String encodedHeader = Base64.getUrlEncoder().withoutPadding().encodeToString(serializedHeader);
		byte[] serializedPayload = payload.toString().getBytes("UTF-8");
		String encodedPayload = Base64.getUrlEncoder().withoutPadding().encodeToString(serializedPayload);
		return encodedHeader + "." + encodedPayload;
	}

	public static JsonObject createTokenHeader(Map<String, String> otherClaims, final String tokenContentType) {

		JsonObject header = new JsonObject();

		// type
		header.addProperty("typ", "JWT");

		// signature algorithm
		header.addProperty("alg", Constants.SIGNING_ALGORITHM_NAME);
		
		header.addProperty("cty", tokenContentType);
		
		Set<String> keys = otherClaims.keySet();
		for (String key : keys) {
			header.addProperty(key, otherClaims.get(key));
		}

		return header;
	}

	public static JsonObject createTokenPayload(String email, Role userRole, String iss, long durationMinutes,
			Map<String, String> otherClaims) {

		Instant accessExperationTime = Instant.now().plus(durationMinutes, ChronoUnit.MINUTES);
		long accessExperationTimeInSeconds = Instant.ofEpochSecond(0L).until(accessExperationTime, ChronoUnit.SECONDS);

		Set<String> keys = otherClaims.keySet();

		JsonObject payload = new JsonObject();

		payload.addProperty("email", email);
		payload.addProperty("role", userRole.name());
		// issuer
		payload.addProperty("iss", iss);
		// expiration time
		payload.addProperty("exp", accessExperationTimeInSeconds);
		// issued at
		payload.addProperty("iat", Instant.ofEpochSecond(0L).until(Instant.now(), ChronoUnit.SECONDS));

		for (String key : keys) {
			payload.addProperty(key, otherClaims.get(key));
		}

		return payload;

	}

	public static boolean verifyToken(final HttpServerExchange exchange, String tokenContentType) throws Exception {

		String[] spliteratedToken = extractToken(exchange);
		if (spliteratedToken == null) {
			return false;
		}
		
		String header = spliteratedToken[0];
		String payload = spliteratedToken[1];
		String signature = spliteratedToken[2];

		if (isEmpty(signature) || isEmpty(payload) || isEmpty(header)) {
			return false;
		}

		String cty = extractValueFromToken(header, "cty");
		if(!tokenContentType.equals(cty)) return false;
		
		if (!SecurityUtil.expirationTimeCheck(payload)) {
			return false;
		}
		
		String newSignature = signHS256(header + "." + payload);
		return signature.equals(newSignature);
	}

	public static TokenContainer jwtGenerator(final String userId, final Map<String, String> otherClaimsHeader,
			final Map<String, String> otherClaimsPayload) throws Exception {

		JsonObject accessTokenHeader = AuthHelper.createTokenHeader(otherClaimsHeader, "access");

		JsonObject accessTokenPayload = AuthHelper.createTokenPayload(userId, Role.ADMIN, Constants.JWT_ISSUER,
				Constants.ACCESS_DURATION_IN_MINUTES, otherClaimsPayload);

		JsonObject refreshTokenHeader = AuthHelper.createTokenHeader(otherClaimsHeader, "refresh");

		JsonObject refreshTokenPayload = AuthHelper.createTokenPayload(userId, Role.ADMIN, Constants.JWT_ISSUER,
				Constants.REFRESH_DURATION_IN_MINUTES, otherClaimsPayload);

		String accessToken = AuthHelper.createToken(accessTokenHeader, accessTokenPayload);
		String refreshToken = AuthHelper.createToken(refreshTokenHeader, refreshTokenPayload);

		String signedAccessToken = AuthHelper.signingTokenSH256(accessToken);
		String signedRefreshToken = AuthHelper.signingTokenSH256(refreshToken);

		TokenContainer tokenContainer = new TokenContainer(signedRefreshToken, signedAccessToken,
				Constants.ACCESS_DURATION_IN_MINUTES);
		return tokenContainer;
	}

	public static String extractValueFromToken(final String jwtPart, final String key) {
		byte[] decoded = Base64.getMimeDecoder().decode(jwtPart);
		String jwtPartDecoded = new String(decoded);
		JsonObject jsonObject = JsonUtil.strToJsonObject(jwtPartDecoded);
		if(jsonObject == null) return null;
		JsonElement jsonElement = jsonObject.get(key);
				
		return jsonElement == null ? null : jsonElement.getAsString(); 
	}

	public static String[] extractToken(final HttpServerExchange exchange) {
		HeaderValues headerValues = exchange.getRequestHeaders().get(Headers.AUTHORIZATION);

		if (headerValues == null) {
			return null;
		}

		String[] parts = headerValues.getLast().split(" ");
		if (parts.length != 2 || !Constants.BEARER_TOKEN.trim().equals(parts[0])) {
			return null;
		}

		String token = parts[1];
		String[] spliteratedToken = token.split("\\.");
		if (spliteratedToken.length != 3) {
			return null;
		}
		return spliteratedToken;
	}
}
