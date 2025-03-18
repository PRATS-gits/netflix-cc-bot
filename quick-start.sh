#!/bin/bash

echo "Building and starting Netflix Support Chat application..."

# Create build directory structure
mkdir -p build/WEB-INF/classes
mkdir -p build/WEB-INF/lib
mkdir -p logs

# Check if JAR files exist and have content
if [ ! -s "WEB-INF/lib/json-20231013.jar" ] || [ ! -s "WEB-INF/lib/log4j-1.2.17.jar" ] || [ ! -s "WEB-INF/lib/javax.servlet-api-4.0.1.jar" ] || [ ! -s "WEB-INF/lib/tomcat-embed-core-9.0.65.jar" ] || [ ! -s "WEB-INF/lib/tomcat-embed-jasper-9.0.65.jar" ]; then
    echo "One or more JAR files are missing or empty. Running download-jars.sh..."
    chmod +x download-jars.sh
    ./download-jars.sh
fi

# Copy required JAR files
cp WEB-INF/lib/* build/WEB-INF/lib/

# Compile Java source files with better error handling
echo "Compiling Java files..."
javac -d build/WEB-INF/classes -cp "WEB-INF/lib/*:src" src/com/netflixsupport/*.java src/utils/*.java
if [ $? -ne 0 ]; then
    echo "Compilation failed. Please check the error messages above."
    exit 1
fi

# Copy web resources
echo "Copying web resources..."
cp -r WebContent/* build/
cp -r WEB-INF/views build/WEB-INF/
cp WEB-INF/web.xml build/WEB-INF/
cp config/log4j.properties build/WEB-INF/classes/
cp .env build/WEB-INF/classes/

# Check if running in a container
if [ -f /.dockerenv ] || [ -f /run/.containerenv ] || grep -q docker /proc/1/cgroup 2>/dev/null; then
    echo ""
    echo "===== DETECTED DEV CONTAINER ENVIRONMENT ====="
    echo "Make sure port 8080 is forwarded to your host machine."
    echo "In VS Code: Check the Ports tab in the lower panel."
    echo "If port 8080 is not listed, add it manually by clicking the '+' button."
    echo "=============================================="
    echo ""
fi

# Run the embedded server with better error handling
echo "Starting embedded Tomcat server..."
java -cp "build/WEB-INF/classes:build/WEB-INF/lib/*" com.netflixsupport.EmbeddedServer
if [ $? -ne 0 ]; then
    echo "Failed to start the server. Please check the error messages above."
    exit 1
fi
