package org.coupons.security;

import org.coupons.util.Constants;

import com.networknt.handler.Handler;
import com.networknt.security.JwtVerifyHandler;

import io.undertow.server.HttpServerExchange;

public class CustomJwtVerifyHandler extends JwtVerifyHandler {

	static final String STATUS_MISSING_AUTH_TOKEN = "ERR10002";
	
	public void handleRequest(final HttpServerExchange exchange) throws Exception {

		final boolean isLoginRequest = Constants.LOGIN_END_POINT.equals(exchange.getRequestPath());
		
		final boolean isRefreshRequest = Constants.REFRESH_END_POINT.equals(exchange.getRequestPath());

		final boolean isPostMethod = Constants.POST_HTTP_METHOD
				.equalsIgnoreCase(exchange.getRequestMethod().toString());

		if (isLoginRequest && isPostMethod) {
			
			// Only for login request
			Handler.next(exchange, super.getNext());
			
		} else if(isRefreshRequest && isPostMethod){
			// Only for refresh request
			
			// Refresh token verification
			if(AuthHelper.verifyToken(exchange, "refresh")) {
				
				Handler.next(exchange, super.getNext());
			}else {
				setExchangeStatus(exchange, STATUS_MISSING_AUTH_TOKEN);
			}
			
		} else {
			
			// JWT verification
			if(AuthHelper.verifyToken(exchange, "access")) {
				
				Handler.next(exchange, super.getNext());
			}else {
				setExchangeStatus(exchange, STATUS_MISSING_AUTH_TOKEN);
			}
			
		}

	}
	
}
