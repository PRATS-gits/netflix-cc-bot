#!/bin/bash

echo "Downloading required JAR files..."

mkdir -p WEB-INF/lib

# Download JSON library
echo "Downloading JSON library..."
curl -L "https://repo1.maven.org/maven2/org/json/json/20231013/json-20231013.jar" -o "WEB-INF/lib/json-20231013.jar"

# Download Log4j
echo "Downloading Log4j..."
curl -L "https://repo1.maven.org/maven2/log4j/log4j/1.2.17/log4j-1.2.17.jar" -o "WEB-INF/lib/log4j-1.2.17.jar"

# Download Servlet API
echo "Downloading Servlet API..."
curl -L "https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar" -o "WEB-INF/lib/javax.servlet-api-4.0.1.jar"

# Download Tomcat Embedded Core
echo "Downloading Tomcat Embedded Core..."
curl -L "https://repo1.maven.org/maven2/org/apache/tomcat/embed/tomcat-embed-core/9.0.65/tomcat-embed-core-9.0.65.jar" -o "WEB-INF/lib/tomcat-embed-core-9.0.65.jar"

# Download Tomcat Embedded Jasper
echo "Downloading Tomcat Embedded Jasper..."
curl -L "https://repo1.maven.org/maven2/org/apache/tomcat/embed/tomcat-embed-jasper/9.0.65/tomcat-embed-jasper-9.0.65.jar" -o "WEB-INF/lib/tomcat-embed-jasper-9.0.65.jar"

# Download additional dependencies for Tomcat Embedded
echo "Downloading additional dependencies..."
curl -L "https://repo1.maven.org/maven2/org/apache/tomcat/embed/tomcat-embed-el/9.0.65/tomcat-embed-el-9.0.65.jar" -o "WEB-INF/lib/tomcat-embed-el-9.0.65.jar"
curl -L "https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-annotations-api/9.0.65/tomcat-annotations-api-9.0.65.jar" -o "WEB-INF/lib/tomcat-annotations-api-9.0.65.jar"
curl -L "https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-api/9.0.65/tomcat-api-9.0.65.jar" -o "WEB-INF/lib/tomcat-api-9.0.65.jar"
curl -L "https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-util/9.0.65/tomcat-util-9.0.65.jar" -o "WEB-INF/lib/tomcat-util-9.0.65.jar"
curl -L "https://repo1.maven.org/maven2/org/apache/tomcat/tomcat-util-scan/9.0.65/tomcat-util-scan-9.0.65.jar" -o "WEB-INF/lib/tomcat-util-scan-9.0.65.jar"
curl -L "https://repo1.maven.org/maven2/org/eclipse/jdt/ecj/3.18.0/ecj-3.18.0.jar" -o "WEB-INF/lib/ecj-3.18.0.jar"

# Check if downloads were successful
echo "Checking downloaded files..."
ls -lh WEB-INF/lib/

echo "All required JAR files downloaded."
