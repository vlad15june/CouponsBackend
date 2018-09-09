package org.coupons.handlers.admin;

import java.io.IOException;
import java.util.Map;

import org.coupons.facade.AdminFacade;
import org.coupons.pojo.Company;
import org.coupons.security.AuthHelper;
import org.coupons.util.Constants;
import org.coupons.util.JSONConvertor;
import org.jose4j.json.internal.json_simple.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.body.BodyHandler;
import com.networknt.exception.ApiException;
import com.networknt.status.Status;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;


public class AdminCreateCompanyHandlerPost implements HttpHandler {

	@Override
	public void handleRequest(final HttpServerExchange exchange) throws IOException {
		exchange.getRequestHeaders().add(new HttpString("Content-Type"), "application/json");

		final String[] parts =  AuthHelper.extractToken(exchange);
		final String payload = parts[1];
		
		final ObjectMapper mapper = new ObjectMapper();

		final Map<?, ?> s = (Map<?, ?>) exchange.getAttachment(BodyHandler.REQUEST_BODY);
		String json;
		
//		try {
			
			json = mapper.writeValueAsString(s);
			final Company company = mapper.readValue(json, Company.class);
			throw new IOException("IOException");
			//TODO: open it
			// final JSONObject jsonObj = JSONConvertor.companyToJSON(AdminFacade.addCompany(company, payload));
			//exchange.getResponseSender().send(jsonObj.toJSONString());		
//		}catch ( IOException e) {
//			throw new ApiException(new Status(Constants.STATUS_MISSING_AUTH_TOKEN));
//		} 


		
		//TODO: remove it
//		exchange.getResponseSender().send("AdminCreateCompanyHandlerPost");
	}

}
