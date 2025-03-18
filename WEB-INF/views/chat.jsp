<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Netflix Support Chat</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    colors: {
                        netflix: {
                            red: '#E50914',
                            black: '#141414',
                            dark: '#222222',
                            gray: '#737373',
                        }
                    }
                }
            }
        }
    </script>
    <style>
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
        }
        .chat-bubble {
            max-width: 80%;
            animation: fadeIn 0.3s ease-in-out;
        }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .typing-indicator span {
            animation: blink 1.4s infinite both;
        }
        .typing-indicator span:nth-child(2) { animation-delay: 0.2s; }
        .typing-indicator span:nth-child(3) { animation-delay: 0.4s; }
        @keyframes blink {
            0% { opacity: 0.1; }
            20% { opacity: 1; }
            100% { opacity: 0.1; }
        }
    </style>
</head>
<body class="bg-netflix-black text-white min-h-screen flex flex-col">
    <!-- Top Navigation Bar -->
    <header class="bg-netflix-black shadow-md border-b border-gray-800">
        <div class="container mx-auto px-4 py-3 flex justify-between items-center">
            <!-- Netflix Logo SVG -->
            <div class="flex items-center">
                <svg viewBox="0 0 111 30" class="h-8" fill="#E50914" xmlns="http://www.w3.org/2000/svg">
                    <path d="M105.062 14.28L111 30c-1.75-.25-3.499-.563-5.28-.845l-3.345-8.686-3.437 7.969c-1.687-.282-3.344-.376-5.031-.595l6.031-13.75L94.468 0h5.063l3.062 7.874L105.875 0h5.124l-5.937 14.28zM90.47 0h-4.594v27.25c1.5.094 3.062.156 4.594.343V0zm-8.563 26.937c-4.187-.281-8.375-.53-12.656-.625V0h4.687v21.875c2.688.062 5.375.28 7.969.405v4.657zM64.25 10.657v4.687h-6.406V26H53.22V0h13.125v4.687h-8.5v5.97h6.406zm-18.906-5.97V26.25c-1.563 0-3.156 0-4.688.062V4.687h-4.844V0h14.406v4.687h-4.874zM30.75 15.593c-2.062 0-4.5 0-6.25.095v6.968c2.75-.188 5.5-.406 8.281-.5v4.5l-12.968 1.032V0H32.78v4.687H24.5V11c1.813 0 4.594-.094 6.25-.094v4.688zM4.78 12.968v16.375C3.094 29.531 1.593 29.75 0 30V0h4.469l6.093 17.032V0h4.688v28.062c-1.656.282-3.344.376-5.125.625L4.78 12.968z"></path>
                </svg>
                <h1 class="ml-4 text-xl font-bold">Support Assistant</h1>
            </div>
            <div class="flex items-center">
                <a href="https://help.netflix.com" target="_blank" class="text-netflix-gray hover:text-white transition duration-200 mr-6">Help Center</a>
                <a href="https://netflix.com" class="bg-netflix-red text-white px-4 py-1 rounded hover:bg-red-700 transition duration-200">Go to Netflix</a>
            </div>
        </div>
    </header>

    <!-- Main Chat Interface -->
    <div class="flex-1 flex flex-col h-[calc(100vh-70px)]">
        <!-- Chat Messages Container -->
        <div id="chat-messages" class="flex-1 overflow-y-auto px-4 py-6 md:px-8 lg:px-16 space-y-6">
            <div class="chat-bubble bg-netflix-dark text-white p-4 rounded-lg max-w-3xl mx-auto">
                <div class="flex items-center mb-2">
                    <svg viewBox="0 0 111 30" class="h-5 mr-2" fill="#E50914" xmlns="http://www.w3.org/2000/svg">
                        <path d="M105.062 14.28L111 30c-1.75-.25-3.499-.563-5.28-.845l-3.345-8.686-3.437 7.969c-1.687-.282-3.344-.376-5.031-.595l6.031-13.75L94.468 0h5.063l3.062 7.874L105.875 0h5.124l-5.937 14.28zM90.47 0h-4.594v27.25c1.5.094 3.062.156 4.594.343V0zm-8.563 26.937c-4.187-.281-8.375-.53-12.656-.625V0h4.687v21.875c2.688.062 5.375.28 7.969.405v4.657zM64.25 10.657v4.687h-6.406V26H53.22V0h13.125v4.687h-8.5v5.97h6.406zm-18.906-5.97V26.25c-1.563 0-3.156 0-4.688.062V4.687h-4.844V0h14.406v4.687h-4.874zM30.75 15.593c-2.062 0-4.5 0-6.25.095v6.968c2.75-.188 5.5-.406 8.281-.5v4.5l-12.968 1.032V0H32.78v4.687H24.5V11c1.813 0 4.594-.094 6.25-.094v4.688zM4.78 12.968v16.375C3.094 29.531 1.593 29.75 0 30V0h4.469l6.093 17.032V0h4.688v28.062c-1.656.282-3.344.376-5.125.625L4.78 12.968z"></path>
                    </svg>
                    <span class="font-semibold">Netflix Support</span>
                </div>
                <p>Hello! I'm your Netflix support assistant. I can help you with information about Netflix pricing, the latest shows, movies, and other Netflix-related questions. How can I help you today?</p>
            </div>
        </div>
        
        <!-- Chat Input Area -->
        <div class="border-t border-gray-800 bg-netflix-black px-4 py-4 md:px-8 lg:px-16">
            <div class="typing-indicator hidden mb-2 text-netflix-gray text-sm ml-4">
                <span>●</span>
                <span>●</span>
                <span>●</span>
            </div>
            <div class="max-w-3xl mx-auto">
                <form id="chat-form" class="flex">
                    <input type="text" id="user-input" class="flex-1 bg-netflix-dark text-white rounded-l-lg px-4 py-3 focus:outline-none border border-gray-700 focus:border-netflix-red" placeholder="Ask me anything about Netflix...">
                    <button type="submit" class="bg-netflix-red text-white px-5 py-3 rounded-r-lg hover:bg-red-700 transition duration-200 flex items-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
                        </svg>
                    </button>
                </form>
                <p class="text-netflix-gray text-xs mt-2 text-center">Netflix Support Assistant is powered by AI and may produce inaccurate information.</p>
            </div>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/chat.js"></script>
</body>
</html>
