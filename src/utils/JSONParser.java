package utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JSONParser {
    private static final Logger logger = Logger.getLogger(JSONParser.class.getName());
    
    /**
     * Safely extracts a string from a JSONObject
     * 
     * @param json The JSONObject to extract from
     * @param key The key to extract
     * @return The string value or null if not found
     */
    public static String getString(JSONObject json, String key) {
        try {
            if (json.has(key) && !json.isNull(key)) {
                return json.getString(key);
            }
        } catch (JSONException e) {
            logger.log(Level.WARNING, "Error extracting string for key: " + key, e);
        }
        return null;
    }
    
    /**
     * Safely extracts a JSONObject from a JSONObject
     * 
     * @param json The JSONObject to extract from
     * @param key The key to extract
     * @return The JSONObject value or null if not found
     */
    public static JSONObject getObject(JSONObject json, String key) {
        try {
            if (json.has(key) && !json.isNull(key)) {
                return json.getJSONObject(key);
            }
        } catch (JSONException e) {
            logger.log(Level.WARNING, "Error extracting object for key: " + key, e);
        }
        return null;
    }
    
    /**
     * Safely extracts a JSONArray from a JSONObject
     * 
     * @param json The JSONObject to extract from
     * @param key The key to extract
     * @return The JSONArray value or null if not found
     */
    public static JSONArray getArray(JSONObject json, String key) {
        try {
            if (json.has(key) && !json.isNull(key)) {
                return json.getJSONArray(key);
            }
        } catch (JSONException e) {
            logger.log(Level.WARNING, "Error extracting array for key: " + key, e);
        }
        return null;
    }
    
    /**
     * Extracts a list of strings from a JSONArray
     * 
     * @param array The JSONArray containing strings
     * @return A list of strings
     */
    public static List<String> getStringList(JSONArray array) {
        List<String> result = new ArrayList<>();
        try {
            for (int i = 0; i < array.length(); i++) {
                result.add(array.getString(i));
            }
        } catch (JSONException e) {
            logger.log(Level.WARNING, "Error extracting string list from array", e);
        }
        return result;
    }
}
