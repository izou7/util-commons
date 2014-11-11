package cn.zy.commons.util.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON convert utility class.<br>
 * This class ordered methods to format an Object to a JSON, or parse a JSON as
 * an Object.
 * 
 * @author zy
 * @version 2.0.0
 * @since 1.0.0
 */
public class JsonConverter {

	/** Get logger. */
	private static final Logger logger = LoggerFactory
			.getLogger(JsonConverter.class);

	/** Object mapper for convert JSON. */
	private static ObjectMapper objectMapper = new ObjectMapper();

	/** get gson for convert json */
	private static Gson gson = new Gson();

	private static Gson gsonExpose = new GsonBuilder()
			.excludeFieldsWithoutExposeAnnotation().create();

	/**
	 * Format to JSON string.<br>
	 * This method cannot support raw type class.
	 * 
	 * @param obj
	 *            The object to format.
	 * @return Format JSON.
	 */
	public static String format(Object obj) {

		// Check null.
		if (obj == null) {
			logger.warn("Formating null object to JSON.");
			return null;
		}

		// Format to JSON.
		try {
			return gson.toJson(obj);
			// return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("JSON format Failed. Object Class is: "
					+ obj.getClass());
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * Format to JSON string. Exclude mark "@Expose"
	 * 
	 * @param obj
	 * @return
	 */
	public static String formatByExpose(Object obj) {
		if (obj == null) {
			logger.warn("Formating null object to JSON");
			return null;
		}
		// Format to JSON.
		try {
			return gsonExpose.toJson(obj);
			// return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("JSON format Failed. Object Class is: "
					+ obj.getClass());
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * Parse JSON string to Object.<br>
	 * This method cannot support raw type class.
	 * 
	 * @param json
	 *            The JSON to parse.
	 * @param clazz
	 *            Parse Class
	 * @return Parse result. An instance of 'clazz' filled with JSON data.
	 */
	public static <T> T parse(String json, Class<T> clazz) {

		// Check null.
		if (StringUtils.isBlank(json)) {
			logger.warn("JSON is NULL! Parsing [null] to [" + clazz.getName()
					+ "].");
			return null;
		}
		try {

			// convert JSON to T.
			// return objectMapper.readValue(json, clazz);
			return gson.fromJson(json, clazz);

		} catch (Exception e) {
			logger.error("Parse JSON failed. Parsing JSON [" + json
					+ "] to Class [" + clazz.getName() + "] failed.");
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Parse JSON string to Object.<br>
	 * 	is impl by jackson
	 * 
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T parseByJackson(String json, Class<T> clazz) {

		// Check null.
		if (StringUtils.isBlank(json)) {
			logger.warn("JSON is NULL! Parsing [null] to [" + clazz.getName()
					+ "].");
			return null;
		}
		try {

			// convert JSON to T.
			return objectMapper.readValue(json, clazz);
			// return gson.fromJson(json, clazz);

		} catch (Exception e) {
			logger.error("Parse JSON failed. Parsing JSON [" + json
					+ "] to Class [" + clazz.getName() + "] failed.");
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Parse JSON to List&lt;T&gt;
	 * 
	 * @param json
	 *            The JSON to parse.
	 * @param clazz
	 *            The list type.
	 * @return Parse result. An ArrayList&lt;T&gt; filled with JSON data.
	 */
	public static <T> List<T> asList(String json, Class<T> clazz) {

		// Check null.
		if (StringUtils.isBlank(json)) {
			logger.warn("JSON is NULL! Parsing [null] to [List<"
					+ clazz.getName() + ">].");
		}

		try {

			// Convert JSON to List.
			List<?> list = objectMapper.readValue(json, List.class);
			List<T> result = new ArrayList<T>(list.size());

			// Cast type.
			for (Object obj : list) {
				result.add(objectMapper.convertValue(obj, clazz));
			}

			return result;

		} catch (Exception e) {
			logger.error("Parse JSON failed. Parsing JSON [" + json
					+ "] to Class [List<" + clazz.getName() + ">] failed.");
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Parse JSON to Map&lt;K, V&gt;
	 * 
	 * @param json
	 *            The JSON to parse.
	 * @param keyClass
	 *            The key type of map.
	 * @param valueClass
	 *            The value type of map.
	 * @return Parse result. An HashMap&lt;T&gt; filled with JSON data.
	 */
	public static <K, V> Map<K, V> asMap(String json, Class<K> keyClass,
			Class<V> valueClass) {

		if (StringUtils.isBlank(json)) {
			logger.warn("JSON is NULL! Parsing [null] to [Map<" + keyClass
					+ ", " + valueClass + ">]");
		}

		try {

			// Convert JSON to Map.
			Map<?, ?> map = objectMapper.readValue(json, Map.class);
			Map<K, V> result = new HashMap<K, V>();

			// Cast type.
			for (Entry<?, ?> entry : map.entrySet()) {
				result.put(objectMapper.convertValue(entry.getKey(), keyClass),
						objectMapper.convertValue(entry.getValue(), valueClass));
			}

			return result;

		} catch (Exception e) {
			logger.error("Parse JSON failed. Parsing JSON [" + json
					+ "] to Class [Map<" + keyClass.getName() + ", "
					+ valueClass.getName() + ">] failed.");
			logger.error(e.getMessage(), e);
		}

		return null;
	}
	public static void main(String[] args) {
		String test = "{\"name\":\"zy\",\"num\":10}";
		Map map = parse(test,Map.class);
		System.out.println(map);
	}
}