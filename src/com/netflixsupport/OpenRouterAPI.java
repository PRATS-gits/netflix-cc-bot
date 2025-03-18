package com.netflixsupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;

public class OpenRouterAPI {
    private static final Logger logger = Logger.getLogger(OpenRouterAPI.class.getName());
    private String apiKey;
    private String aiModel;
    private double temperature;
    private int maxTokens;
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    
    public OpenRouterAPI() {
        loadConfiguration();
    }
    
    private void loadConfiguration() {
        try {
            // Try multiple locations for .env file
            File envFile = new File(".env");
            if (!envFile.exists()) {
                envFile = new File("../.env");
                if (!envFile.exists()) {
                    envFile = new File(System.getProperty("user.dir") + "/.env");
                }
            }
            
            // Load configuration from .env file or environment variables
            Properties properties = new Properties();
            if (envFile.exists()) {
                FileInputStream input = new FileInputStream(envFile);
                properties.load(input);
                input.close();
                logger.info("Loaded configuration from: " + envFile.getAbsolutePath());
            } else {
                logger.warning("No .env file found, will try system environment variables");
            }
            
            // Get API key
            apiKey = properties.getProperty("OPENROUTER_API_KEY");
            if (apiKey == null) {
                apiKey = System.getenv("OPENROUTER_API_KEY");
            }
            
            // Get AI model
            aiModel = properties.getProperty("AI_MODEL");
            if (aiModel == null) {
                aiModel = System.getenv("AI_MODEL");
                if (aiModel == null) {
                    aiModel = "openai/gpt-3.5-turbo"; // Default model
                }
            }
            
            // Get temperature
            String tempStr = properties.getProperty("TEMPERATURE");
            if (tempStr == null) {
                tempStr = System.getenv("TEMPERATURE");
            }
            try {
                temperature = tempStr != null ? Double.parseDouble(tempStr) : 0.7;
            } catch (NumberFormatException e) {
                temperature = 0.7; // Default temperature
            }
            
            // Get max tokens
            String tokenStr = properties.getProperty("MAX_TOKENS");
            if (tokenStr == null) {
                tokenStr = System.getenv("MAX_TOKENS");
            }
            try {
                maxTokens = tokenStr != null ? Integer.parseInt(tokenStr) : 300;
            } catch (NumberFormatException e) {
                maxTokens = 300; // Default max tokens
            }
            
            if (apiKey == null) {
                logger.severe("OpenRouter API key not found. Please set OPENROUTER_API_KEY in .env file or as environment variable");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading configuration", e);
        }
    }
    
    public String getAIResponse(String userMessage) throws IOException {
        return getAIResponseWithContext(userMessage, null);
    }
    
    public String getAIResponseWithContext(String userMessage, String context) throws IOException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", aiModel);
        
        JSONArray messagesArray = new JSONArray();
        
        // System instructions
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        
        String systemContent = "You are a helpful Netflix India customer support assistant. " +
            "You help users with information about Netflix pricing, the latest shows, movies, and other Netflix-related questions. " +
            "Be friendly and concise in your responses. " +
            "Current date: " + java.time.LocalDate.now();
        
        if (context != null && !context.isEmpty()) {
            systemContent += "\n\nHere is some additional context to help you answer: " + context;
        }
        
        systemMessage.put("content", systemContent);
        messagesArray.put(systemMessage);
        
        // User message
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", userMessage);
        messagesArray.put(userMsg);
        
        requestBody.put("messages", messagesArray);
        requestBody.put("temperature", temperature);
        requestBody.put("max_tokens", maxTokens);
        
        // Make the API call
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        connection.setRequestProperty("HTTP-Referer", "https://netflix.com");
        connection.setDoOutput(true);
        
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            }
        } else {
            logger.severe("Error calling OpenRouter API. HTTP response code: " + responseCode);
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                logger.severe("Error response: " + response.toString());
            }
            return "I'm sorry, I'm having trouble connecting to my knowledge source right now. Please try again later.";
        }
    }
}