package org.coupons.handlers.company;

import java.util.Map;

import org.coupons.facade.CompanyFacade;
import org.coupons.pojo.Coupon;
import org.coupons.security.AuthHelper;
import org.coupons.util.JSONConvertor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public class CompanyUpdateCouponHandlerPut implements HttpHandler{

	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {
		exchange.getRequestHeaders().add(new HttpString("Content-Type"), "application/json");
		
		final String[] parts =  AuthHelper.extractToken(exchange);
		final String payload = parts[1];
		
		final ObjectMapper mapper = new ObjectMapper();

		final Map<?, ?> s = (Map<?, ?>) exchange.getAttachment(BodyHandler.REQUEST_BODY);
		final String json = mapper.writeValueAsString(s);
		final Coupon coupon = mapper.readValue(json, Coupon.class);
		
		CompanyFacade.updateCoupon(coupon, payload);
		final String jsonObj = JSONConvertor.couponToJSON(coupon).toJSONString();
		
		exchange.getResponseSender().send(jsonObj);
	}

}