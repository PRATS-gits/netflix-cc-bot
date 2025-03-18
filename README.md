# Netflix Customer Support Bot

## Overview
This project is a Web Lab practical implementation of an AI-powered Netflix Customer Support Bot. It uses modern web technologies including AJAX, Java Servlets, JSP, and integrates with AI APIs to provide intelligent responses to user queries about Netflix services.

## Features
- **AI-Powered Responses**: Utilizes OpenRouter API to generate intelligent, context-aware responses
- **Real-time Web Search**: Integrates with Google Custom Search to provide up-to-date information
- **Netflix-Themed UI**: Professional interface styled with Tailwind CSS that matches Netflix branding
- **Interactive Chat Experience**: Real-time chat experience with smooth animations and typing indicators
- **Responsive Design**: Works seamlessly across devices of all sizes

## Technologies Used
- **Frontend**: HTML, JavaScript (AJAX), Tailwind CSS
- **Backend**: Java Servlets, JSP
- **Server**: Apache Tomcat
- **APIs**:
  - OpenRouter API for AI-powered responses
  - Google Custom Search API for real-time information
  
## Demo
A static demo version of the application is hosted on GitHub Pages. Visit [Netflix Support Chat Demo](https://prats-gits.github.io/netflix-cc-bot/static-site/index.html) to try it out.

> **Note**: The demo version uses a simulated backend since GitHub Pages doesn't support Java server-side execution. For the full experience with real AI responses, deploy the application to a Java servlet container like Tomcat.

## Local Development Setup

### Prerequisites
- Java Development Kit (JDK) 11 or higher
- Apache Tomcat 9.0 or higher
- Git

### Installation Steps
1. Clone the repository:
   ```bash
   git clone https://github.com/PRATS-gits/netflix-cc-bot.git
   cd netflix-cc-bot
   ```

2. Configure API keys:
   - Create a `.env` file in the root directory
   - Add your API keys (See `.env.example` for the format)

3. Build the application:
   ```bash
   ./build.sh
   ```

4. Deploy to Tomcat:
   ```bash
   ./run.sh
