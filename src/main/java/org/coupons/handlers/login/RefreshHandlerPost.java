package org.coupons.handlers.login;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.coupons.dbo.JwtDAO;
import org.coupons.security.AuthHelper;
import org.coupons.security.pojo.TokenContainer;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public class RefreshHandlerPost  implements HttpHandler{

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		
		final String[] parts = AuthHelper.extractToken(exchange);
		final String header = parts[0];
		final String payload = parts[1];
		
		final String jwtId = AuthHelper.extractValueFromToken(header, "jti");
		String token = JwtDAO.getToken(jwtId).trim();
		
		if((header + "." + payload + "." + parts[2]).equals(token)) {

			final String email = AuthHelper.extractValueFromToken(payload, "email");
			final String newJwtId = UUID.randomUUID().toString();
			final Map<String, String> otherClaimsHeader = new HashMap<>();
			
			otherClaimsHeader.put("jti", newJwtId);
			
			final Map<String, String> otherClaimsPayload = new HashMap<>();
			final TokenContainer tokenContainer = AuthHelper.jwtGenerator(email, otherClaimsHeader, otherClaimsPayload);
			
			JwtDAO.deleteToken(jwtId);
			JwtDAO.addToken(tokenContainer, newJwtId);
			
			exchange.getResponseHeaders().add(new HttpString("Authorization"), tokenContainer.toJsonString());
			exchange.getResponseSender().send("Refresh Handler Works (POST)");
		}else {
			exchange.getResponseSender().send("Wrong refresh token");
		}
	}

}
