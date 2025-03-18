#!/bin/bash

echo "Creating required directories..."

# Create main directories
mkdir -p WebContent/js
mkdir -p WebContent/assets
mkdir -p WEB-INF/views
mkdir -p WEB-INF/lib
mkdir -p src/com/netflixsupport
mkdir -p src/utils
mkdir -p config
mkdir -p logs
mkdir -p build

echo "Required directories have been created."
echo "NOTE: You need to manually add the required JAR files to WEB-INF/lib directory."
