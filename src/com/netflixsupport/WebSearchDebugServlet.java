package com.netflixsupport;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Debug servlet to test web search functionality directly
 */
@WebServlet("/debug/search")
public class WebSearchDebugServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final WebSearchService webSearchService = new WebSearchService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String query = request.getParameter("q");
        if (query == null || query.trim().isEmpty()) {
            response.setContentType("text/plain");
            response.getWriter().println("Please provide a 'q' parameter with your search query");
            return;
        }
        
        String searchResult = webSearchService.search(query);
        
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        if (searchResult != null) {
            out.println("Web search result for: " + query);
            out.println("----------------------------------------");
            out.println(searchResult);
        } else {
            out.println("No results found or error occurred during search for: " + query);
        }
    }
}
