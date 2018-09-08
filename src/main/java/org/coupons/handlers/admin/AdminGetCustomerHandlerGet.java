package org.coupons.handlers.admin;

import org.coupons.facade.AdminFacade;
import org.coupons.security.AuthHelper;
import org.coupons.util.JSONConvertor;
import org.jose4j.json.internal.json_simple.JSONObject;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;


public class AdminGetCustomerHandlerGet implements HttpHandler {

	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		exchange.getRequestHeaders().add(new HttpString("Content-Type"), "application/json");
		
		final String[] parts =  AuthHelper.extractToken(exchange);
		final String payload = parts[1];

		final String id = exchange.getQueryParameters().get("customerId").getFirst();
		final JSONObject json = JSONConvertor.customerToJSON(AdminFacade.getCustomer(id, payload));

		exchange.getResponseSender().send(json.toJSONString());
	}

}
