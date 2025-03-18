package com.netflixsupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class WebSearchService {
    private static final Logger logger = Logger.getLogger(WebSearchService.class.getName());
    private String googleApiKey;
    private String searchEngineId;
    private static final String GOOGLE_SEARCH_API_URL = "https://www.googleapis.com/customsearch/v1";
    
    public WebSearchService() {
        loadApiKeys();
    }
    
    private void loadApiKeys() {
        try {
            // Try multiple locations for .env file
            File envFile = new File(".env");
            if (!envFile.exists()) {
                envFile = new File("../.env");
                if (!envFile.exists()) {
                    envFile = new File(System.getProperty("user.dir") + "/.env");
                }
            }
            
            // Load API keys from .env file or environment variables
            Properties properties = new Properties();
            if (envFile.exists()) {
                FileInputStream input = new FileInputStream(envFile);
                properties.load(input);
                input.close();
                logger.info("Loaded API keys from: " + envFile.getAbsolutePath());
            } else {
                logger.warning("No .env file found, will try system environment variables");
            }
            
            googleApiKey = properties.getProperty("GOOGLE_API_KEY");
            searchEngineId = properties.getProperty("SEARCH_ENGINE_ID");
            
            // If not found in .env file, try from system environment
            if (googleApiKey == null) {
                googleApiKey = System.getenv("GOOGLE_API_KEY");
            }
            if (searchEngineId == null) {
                searchEngineId = System.getenv("SEARCH_ENGINE_ID");
            }
            
            if (googleApiKey == null || searchEngineId == null) {
                logger.warning("Google API key or Search Engine ID not found. Web search functionality will be limited.");
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading API keys", e);
        }
    }
    
    public String search(String query) {
        if (googleApiKey == null || searchEngineId == null) {
            logger.warning("Google API key or Search Engine ID is missing");
            return null;
        }
        
        try {
            // Modify the query to focus on Netflix information
            String netflixQuery = query;
            if (!query.toLowerCase().contains("netflix")) {
                netflixQuery = query + " netflix";
            }
            
            // Add keywords to improve search results
            String lowerQuery = query.toLowerCase();
            if (lowerQuery.contains("season") || lowerQuery.contains("episode") || lowerQuery.contains("series")) {
                if (!lowerQuery.contains("release date") && !lowerQuery.contains("premiere")) {
                    netflixQuery += " release date";
                }
                if (!lowerQuery.contains("latest")) {
                    netflixQuery += " latest";
                }
            }
            
            // Remove words that might confuse search
            netflixQuery = netflixQuery.replace("when is", "")
                                      .replace("do you know", "")
                                      .replace("could you tell me", "")
                                      .replace("can you tell me", "")
                                      .trim();
            
            logger.info("Modified search query: " + netflixQuery);
            
            String encodedQuery = URLEncoder.encode(netflixQuery, StandardCharsets.UTF_8.toString());
            String urlString = GOOGLE_SEARCH_API_URL +
                    "?q=" + encodedQuery +
                    "&key=" + googleApiKey +
                    "&cx=" + searchEngineId +
                    "&num=5"; // Get 5 results
            
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                
                String searchResult = extractRelevantInfo(response.toString(), query);
                return searchResult;
            } else {
                logger.warning("Google Search API request failed with response code: " + responseCode);
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {
                    StringBuilder errorResponse = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        errorResponse.append(responseLine.trim());
                    }
                    logger.warning("Error response: " + errorResponse.toString());
                }
                return null;
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during web search", e);
            return null;
        }
    }
    
    private String extractRelevantInfo(String searchResponse, String originalQuery) {
        try {
            JSONObject jsonResponse = new JSONObject(searchResponse);
            
            StringBuilder relevantInfo = new StringBuilder();
            relevantInfo.append("Recent information about: ").append(originalQuery).append("\n\n");
            
            // Extract search results
            if (jsonResponse.has("items")) {
                JSONArray items = jsonResponse.getJSONArray("items");
                int count = Math.min(3, items.length()); // Get top 3 results at most
                
                for (int i = 0; i < count; i++) {
                    JSONObject item = items.getJSONObject(i);
                    String title = item.getString("title");
                    relevantInfo.append("- Title: ").append(title).append("\n");
                    
                    if (item.has("snippet")) {
                        String snippet = item.getString("snippet");
                        // Highlight important information in the snippet
                        String highlightedSnippet = snippet;
                        if (snippet.toLowerCase().contains("release") || 
                            snippet.toLowerCase().contains("premiere") || 
                            snippet.toLowerCase().contains("season") || 
                            snippet.toLowerCase().contains("date")) {
                            highlightedSnippet = "IMPORTANT INFO: " + snippet;
                        }
                        relevantInfo.append("  Summary: ").append(highlightedSnippet).append("\n");
                    }
                    
                    if (item.has("link")) {
                        relevantInfo.append("  Source: ").append(item.getString("link")).append("\n");
                    }
                    
                    relevantInfo.append("\n");
                }
            } else {
                relevantInfo.append("No search results found for this query.\n");
            }
            
            logger.info("Extracted search information: " + relevantInfo.toString().substring(0, Math.min(100, relevantInfo.toString().length())) + "...");
            return relevantInfo.toString();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error extracting information from search results", e);
            return null;
        }
    }
}
