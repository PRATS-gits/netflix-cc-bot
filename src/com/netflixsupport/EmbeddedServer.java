package com.netflixsupport;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;

public class EmbeddedServer {
    public static void main(String[] args) throws LifecycleException, IOException {
        // Create Tomcat instance
        Tomcat tomcat = new Tomcat();
        
        // Explicitly set the hostname to 0.0.0.0 to make it accessible from outside the container
        String hostName = "0.0.0.0";
        int port = 8080;
        
        // Set port and create connector
        tomcat.setPort(port);
        Connector connector = new Connector();
        connector.setPort(port);
        connector.setProperty("address", hostName);
        tomcat.getService().addConnector(connector);
        
        // Set base directory
        String baseDir = new File(".").getAbsolutePath();
        tomcat.setBaseDir(baseDir);
        
        // Create context
        String contextPath = "/NetflixChat";
        String docBase = new File("build").getAbsolutePath();
        Context context = tomcat.addWebapp(contextPath, docBase);
        
        // Add servlet mappings
        Tomcat.addServlet(context, "ChatServlet", new ChatServlet());
        context.addServletMappingDecoded("/chat", "ChatServlet");
        
        // Start server
        tomcat.start();
        
        System.out.println("===========================================================");
        System.out.println("Server started on " + hostName + ":" + port);
        System.out.println("Visit http://localhost:" + port + contextPath + " to access the application");
        System.out.println("If accessing from Windows host browser (outside the container),");
        System.out.println("make sure port " + port + " is forwarded in the Dev Container configuration.");
        System.out.println("===========================================================");
        
        // Keep server running
        tomcat.getServer().await();
    }
}
