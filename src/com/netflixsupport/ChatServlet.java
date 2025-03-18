package com.netflixsupport;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ChatServlet.class.getName());
    
    private final OpenRouterAPI openRouterAPI = new OpenRouterAPI();
    private final WebSearchService webSearchService = new WebSearchService();
    
    public ChatServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {
        super.init();
        logger.info("ChatServlet initialized successfully");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String userMessage = request.getParameter("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            sendErrorResponse(response, "Message parameter cannot be empty");
            return;
        }
        
        logger.info("Received user message: " + userMessage);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JSONObject jsonResponse = new JSONObject();
        
        try {
            // ALWAYS get web search results for entertainment-related queries
            // This helps ensure we have the latest information about shows, movies, etc.
            String webSearchResult = null;
            boolean isEntertainmentQuery = isEntertainmentQuery(userMessage);
            boolean needsCurrentInformation = needsCurrentInformation(userMessage);
            
            if (isEntertainmentQuery || needsCurrentInformation) {
                logger.info("Entertainment or current info query detected: " + userMessage);
                webSearchResult = webSearchService.search(userMessage);
                logger.info("Web search performed with result length: " + 
                           (webSearchResult != null ? webSearchResult.length() : 0));
            }
            
            String aiResponse;
            if (webSearchResult != null && !webSearchResult.isEmpty()) {
                // Use the web search result as context for the AI response
                aiResponse = openRouterAPI.getAIResponseWithContext(userMessage, webSearchResult);
                logger.info("Generated AI response with web search context");
            } else {
                // Get a standard AI response
                aiResponse = openRouterAPI.getAIResponse(userMessage);
                
                // If the response indicates a knowledge gap, try web search as fallback
                if (indicatesKnowledgeGap(aiResponse)) {
                    logger.info("AI response indicates knowledge gap, trying web search fallback");
                    webSearchResult = webSearchService.search(userMessage);
                    
                    if (webSearchResult != null && !webSearchResult.isEmpty()) {
                        aiResponse = openRouterAPI.getAIResponseWithContext(userMessage, webSearchResult);
                    }
                }
            }
            
            jsonResponse.put("message", aiResponse);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing request", e);
            jsonResponse.put("message", "I'm sorry, I'm having trouble processing your request right now. Please try again later.");
        }
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to the chat JSP page if accessed via GET
        request.getRequestDispatcher("/WEB-INF/views/chat.jsp").forward(request, response);
    }
    
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("error", errorMessage);
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
        out.flush();
    }
    
    private boolean needsCurrentInformation(String query) {
        String lowerQuery = query.toLowerCase();
        return lowerQuery.contains("latest") || 
               lowerQuery.contains("recent") || 
               lowerQuery.contains("new") || 
               lowerQuery.contains("upcoming") ||
               lowerQuery.contains("price") || 
               lowerQuery.contains("cost") ||
               lowerQuery.contains("subscription") ||
               lowerQuery.contains("this year") ||
               lowerQuery.contains("this month") ||
               lowerQuery.contains("current") ||
               lowerQuery.contains("release date") ||
               lowerQuery.contains("when will") ||
               lowerQuery.contains("when is") ||
               lowerQuery.contains("schedule") ||
               lowerQuery.contains("coming out") ||
               lowerQuery.contains("premiere");
    }
    
    private boolean isEntertainmentQuery(String query) {
        String lowerQuery = query.toLowerCase();
        return lowerQuery.contains("show") ||
               lowerQuery.contains("series") ||
               lowerQuery.contains("season") ||
               lowerQuery.contains("movie") ||
               lowerQuery.contains("actor") ||
               lowerQuery.contains("actress") ||
               lowerQuery.contains("character") ||
               lowerQuery.contains("episode") ||
               lowerQuery.contains("cast") ||
               lowerQuery.contains("director") ||
               lowerQuery.contains("release") ||
               lowerQuery.contains("watch") ||
               lowerQuery.contains("stream") ||
               lowerQuery.contains("available") ||
               lowerQuery.contains("you") && (lowerQuery.contains("season") || lowerQuery.contains("show") || lowerQuery.contains("series")) ||
               lowerQuery.contains("stranger things") ||
               lowerQuery.contains("squid game") ||
               lowerQuery.contains("bridgerton") ||
               lowerQuery.contains("witcher") ||
               lowerQuery.contains("money heist") ||
               lowerQuery.contains("wednesday") ||
               lowerQuery.contains("ozark");
    }
    
    private boolean indicatesKnowledgeGap(String aiResponse) {
        String lowerResponse = aiResponse.toLowerCase();
        return lowerResponse.contains("i don't have that information") || 
               lowerResponse.contains("i don't have information") ||
               lowerResponse.contains("i'm not sure about") ||
               lowerResponse.contains("i don't have current information") ||
               lowerResponse.contains("my knowledge cutoff") ||
               lowerResponse.contains("may be outdated") ||
               lowerResponse.contains("i don't have access to") ||
               lowerResponse.contains("i can't provide") ||
               lowerResponse.contains("check netflix") ||
               lowerResponse.contains("official website") ||
               lowerResponse.contains("i don't know when");
    }
}
