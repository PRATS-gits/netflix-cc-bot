#!/bin/bash

echo "Creating build directory structure..."
mkdir -p build/WEB-INF/classes
mkdir -p build/WEB-INF/lib

echo "Compiling Java source files..."
javac -d build/WEB-INF/classes -cp "WEB-INF/lib/*:src" src/com/netflixsupport/*.java src/utils/*.java

echo "Copying web resources..."
cp -r WebContent/* build/
cp -r WEB-INF/views build/WEB-INF/
cp WEB-INF/web.xml build/WEB-INF/
cp -r WEB-INF/lib build/WEB-INF/
cp config/log4j.properties build/WEB-INF/classes/
cp .env build/WEB-INF/classes/

echo "Build complete. Files are in the 'build' directory."
