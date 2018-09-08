package org.coupons.util;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.networknt.body.BodyHandler;

import io.undertow.server.HttpServerExchange;

public final class HandlerUtil {

	private HandlerUtil() {
	}

	public static JsonObject extractPostRequestBody(final HttpServerExchange exchange) throws IOException {

		final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		final Map<?, ?> requestBodyMap = (Map<?, ?>) exchange.getAttachment(BodyHandler.REQUEST_BODY);
		final String jsonString = gson.toJson(requestBodyMap);
		final JsonObject jsonObject = JsonUtil.strToJsonObject(jsonString);
		
		return jsonObject;

	}
}
