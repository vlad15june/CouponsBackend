package org.coupons.handlers.company;

import java.util.List;

import org.coupons.facade.CompanyFacade;
import org.coupons.pojo.Coupon;
import org.coupons.security.AuthHelper;
import org.coupons.util.JSONConvertor;
import org.jose4j.json.internal.json_simple.JSONArray;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public class CompanyGetCouponsHandlerGet implements HttpHandler{

	@SuppressWarnings("unchecked")
	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		exchange.getRequestHeaders().add(new HttpString("Content-Type"), "application/json");

		final String[] parts =  AuthHelper.extractToken(exchange);
		final String payload = parts[1];
		
		final List<Coupon> coupons = CompanyFacade.getAllCoupons(payload);
		final JSONArray jsonArr = new JSONArray();
		
		for (Coupon coupon : coupons) {
			jsonArr.add(JSONConvertor.couponToJSON(coupon));
		}
		
		exchange.getResponseSender().send(jsonArr.toJSONString());
	}
	
}