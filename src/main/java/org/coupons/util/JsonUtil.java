package org.coupons.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public final class JsonUtil {

	private JsonUtil() {
	}

	public static JsonObject strToJsonObject(final String jsonString) {

		final Gson gson = new Gson();
		JsonObject jsonObject = null;
		try {
			jsonObject = gson.fromJson(jsonString, JsonObject.class);
		}catch(Exception e) {
			//TODO: write log JsonSyntaxException
		}
		return jsonObject;
	}
}
