package org.coupons.handlers.admin;

import org.coupons.facade.AdminFacade;
import org.coupons.security.AuthHelper;
import org.jose4j.json.internal.json_simple.JSONObject;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;


public class AdminRemoveCompanyHandlerDelete implements HttpHandler {

	@SuppressWarnings("unchecked")
	@Override
	public void handleRequest(HttpServerExchange exchange) throws Exception {
		exchange.getRequestHeaders().add(new HttpString("Content-Type"), "application/json");

		final String[] parts =  AuthHelper.extractToken(exchange);
		final String payload = parts[1];

		final String companyId = exchange.getQueryParameters().get("id").getFirst();
		AdminFacade.deleteCompany(companyId, payload);

		final JSONObject json = new JSONObject();
		json.put("deleted", true);

		exchange.getResponseSender().send(json.toJSONString());
	}

}
