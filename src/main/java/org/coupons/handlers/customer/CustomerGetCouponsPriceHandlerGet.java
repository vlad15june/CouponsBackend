package org.coupons.handlers.customer;

import java.util.List;

import org.coupons.facade.CustomerFacade;
import org.coupons.pojo.Coupon;
import org.coupons.security.AuthHelper;
import org.coupons.util.JSONConvertor;
import org.jose4j.json.internal.json_simple.JSONArray;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;

public class CustomerGetCouponsPriceHandlerGet implements HttpHandler{

	@SuppressWarnings("unchecked")
	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {
		exchange.getRequestHeaders().add(new HttpString("Content-Type"), "application/json");

		final String[] parts =  AuthHelper.extractToken(exchange);
		final String payload = parts[1];
		
		final double price = Double.parseDouble(exchange.getQueryParameters().get("price").getFirst());
		
		final List<Coupon> coupons = CustomerFacade.getAllCouponsByPrice(payload, price);
		final JSONArray jsonArr = new JSONArray();
		
		for (Coupon coupon : coupons) {
			jsonArr.add(JSONConvertor.couponToJSON(coupon));
		}
		
		exchange.getResponseSender().send(jsonArr.toJSONString());
	}

}
