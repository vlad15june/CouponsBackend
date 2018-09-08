package org.coupons.handlers.customer;

import java.util.Map;

import org.coupons.facade.CustomerFacade;
import org.coupons.pojo.Coupon;
import org.coupons.security.AuthHelper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public class CustomerPurchaseCouponHandlerPost implements HttpHandler{

	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {
		exchange.getRequestHeaders().add(new HttpString("Content-Type"), "application/json");
		
		final String[] parts =  AuthHelper.extractToken(exchange);
		final String payload = parts[1];
		
		final ObjectMapper mapper = new ObjectMapper();

		final Map<?, ?> s = (Map<?, ?>) exchange.getAttachment(BodyHandler.REQUEST_BODY);
		final String json = mapper.writeValueAsString(s);
		final Coupon coupon = mapper.readValue(json, Coupon.class);
		
		CustomerFacade.purchaseCoupon(payload, coupon);
		
		exchange.getResponseSender().send("Coupon has been purchased");
	}
}
