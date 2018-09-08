package org.coupons.handlers.admin;

import java.util.List;

import org.coupons.facade.AdminFacade;
import org.coupons.pojo.Customer;
import org.coupons.security.AuthHelper;
import org.coupons.util.JSONConvertor;
import org.jose4j.json.internal.json_simple.JSONArray;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;


public class AdminGetCustomersHandlerGet implements HttpHandler {

	@SuppressWarnings("unchecked")
	@Override
	public void handleRequest(final HttpServerExchange exchange) throws Exception {
		exchange.getRequestHeaders().add(new HttpString("Content-Type"), "application/json");
		
		final String[] parts =  AuthHelper.extractToken(exchange);
		final String payload = parts[1];

		final List<Customer> customers = AdminFacade.getAllCustomers(payload);
		final JSONArray json = new JSONArray();

		for (Customer customer : customers) {
			json.add(JSONConvertor.customerToJSON(customer));
		}

		exchange.getResponseSender().send(json.toJSONString());
	}

}
