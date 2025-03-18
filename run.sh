#!/bin/bash

echo "Starting Tomcat server with Netflix Support Chat application..."

# Set environment variables for API keys if needed
export OPENROUTER_API_KEY=$(grep OPENROUTER_API_KEY .env | cut -d '=' -f2)
export GOOGLE_API_KEY=$(grep GOOGLE_API_KEY .env | cut -d '=' -f2)
export SEARCH_ENGINE_ID=$(grep SEARCH_ENGINE_ID .env | cut -d '=' -f2)

# Assuming Tomcat is installed and in the PATH
TOMCAT_HOME=${CATALINA_HOME:-/usr/local/tomcat}

# Deploy the application
rm -rf $TOMCAT_HOME/webapps/NetflixChat
mkdir -p $TOMCAT_HOME/webapps/NetflixChat
cp -r build/* $TOMCAT_HOME/webapps/NetflixChat/

# Start Tomcat server
$TOMCAT_HOME/bin/catalina.sh run
