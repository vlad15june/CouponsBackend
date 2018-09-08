package org.coupons.handlers.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.coupons.dbo.JwtDAO;
import org.coupons.dbo.UserDAO;
import org.coupons.security.AuthHelper;
import org.coupons.security.pojo.TokenContainer;
import org.coupons.util.HandlerUtil;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public class LoginHandlerPost implements HttpHandler {

	private static String EMAIL = "email";
	private static String PASSWORD = "password";

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		exchange.getRequestHeaders().add(new HttpString("Content-Type"), "application/json");

		Map<String, String> otherClaimsPayload = new HashMap<>();
		Map<String, String> otherClaimsHeader = new HashMap<>();

		JsonObject requestBody = HandlerUtil.extractPostRequestBody(exchange);
		JsonElement email = requestBody.get(EMAIL);
		JsonElement password = requestBody.get(PASSWORD);

		if (UserDAO.isUserExists(email, password)) {
			String jwtId = UUID.randomUUID().toString();
			otherClaimsHeader.put("jti", jwtId); // "jti" (JWT ID) Claim
			
			String userId = UserDAO.getUserId(email.getAsString().trim());
			otherClaimsPayload.put("userId", userId);
			
			TokenContainer tokenContainer = AuthHelper.jwtGenerator(email.getAsString(), otherClaimsHeader,
					otherClaimsPayload);

			JwtDAO.addToken(tokenContainer, jwtId);

			exchange.getResponseHeaders().add(new HttpString("Authorization"), tokenContainer.toJsonString());
			exchange.getResponseSender().send("Login Handler Works (POST)" + requestBody);
		} else {
			exchange.getResponseSender().send("Wrong credentials" + requestBody);
		}

	}
}
