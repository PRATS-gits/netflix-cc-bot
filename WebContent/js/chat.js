document.addEventListener('DOMContentLoaded', () => {
    const chatForm = document.getElementById('chat-form');
    const userInput = document.getElementById('user-input');
    const chatMessages = document.getElementById('chat-messages');
    const typingIndicator = document.querySelector('.typing-indicator');
    
    // Focus input field when page loads
    userInput.focus();
    
    // Handle chat form submission
    chatForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const message = userInput.value.trim();
        
        if (message.length === 0) return;
        
        // Add user message to chat
        addMessage(message, 'user');
        userInput.value = '';
        
        // Scroll to bottom
        scrollToBottom();
        
        // Show typing indicator
        showTypingIndicator();
        
        // Send message to server via AJAX
        sendMessage(message);
    });
    
    // Add message to chat window
    function addMessage(text, sender) {
        const messageDiv = document.createElement('div');
        
        if (sender === 'user') {
            messageDiv.className = 'chat-bubble bg-blue-600 text-white p-4 rounded-lg max-w-3xl mx-auto';
            messageDiv.innerHTML = `
                <div class="flex items-center mb-2">
                    <span class="font-semibold">You</span>
                </div>
                <p>${text}</p>
            `;
        } else {
            messageDiv.className = 'chat-bubble bg-netflix-dark text-white p-4 rounded-lg max-w-3xl mx-auto';
            messageDiv.innerHTML = `
                <div class="flex items-center mb-2">
                    <svg viewBox="0 0 111 30" class="h-5 mr-2" fill="#E50914" xmlns="http://www.w3.org/2000/svg">
                        <path d="M105.062 14.28L111 30c-1.75-.25-3.499-.563-5.28-.845l-3.345-8.686-3.437 7.969c-1.687-.282-3.344-.376-5.031-.595l6.031-13.75L94.468 0h5.063l3.062 7.874L105.875 0h5.124l-5.937 14.28zM90.47 0h-4.594v27.25c1.5.094 3.062.156 4.594.343V0zm-8.563 26.937c-4.187-.281-8.375-.53-12.656-.625V0h4.687v21.875c2.688.062 5.375.28 7.969.405v4.657zM64.25 10.657v4.687h-6.406V26H53.22V0h13.125v4.687h-8.5v5.97h6.406zm-18.906-5.97V26.25c-1.563 0-3.156 0-4.688.062V4.687h-4.844V0h14.406v4.687h-4.874zM30.75 15.593c-2.062 0-4.5 0-6.25.095v6.968c2.75-.188 5.5-.406 8.281-.5v4.5l-12.968 1.032V0H32.78v4.687H24.5V11c1.813 0 4.594-.094 6.25-.094v4.688zM4.78 12.968v16.375C3.094 29.531 1.593 29.75 0 30V0h4.469l6.093 17.032V0h4.688v28.062c-1.656.282-3.344.376-5.125.625L4.78 12.968z"></path>
                    </svg>
                    <span class="font-semibold">Netflix Support</span>
                </div>
                <p>${formatResponse(text)}</p>
            `;
        }
        
        chatMessages.appendChild(messageDiv);
        scrollToBottom();
    }
    
    // Format the response text with proper line breaks
    function formatResponse(text) {
        return text
            .replace(/\n/g, '<br>')
            .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>'); // Basic markdown for bold text
    }
    
    // Scroll to the bottom of chat
    function scrollToBottom() {
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }
    
    // Show typing indicator
    function showTypingIndicator() {
        typingIndicator.classList.remove('hidden');
    }
    
    // Hide typing indicator
    function hideTypingIndicator() {
        typingIndicator.classList.add('hidden');
    }
    
    // Send message to server using AJAX
    function sendMessage(message) {
        const xhr = new XMLHttpRequest();
        xhr.open('POST', 'chat', true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        
        xhr.onload = function() {
            hideTypingIndicator();
            
            if (xhr.status === 200) {
                try {
                    const response = JSON.parse(xhr.responseText);
                    addMessage(response.message, 'bot');
                } catch (error) {
                    console.error('Error parsing response:', error);
                    addMessage('Sorry, I encountered an error. Please try again later.', 'bot');
                }
            } else {
                addMessage('Sorry, I encountered an error. Please try again later.', 'bot');
                console.error('Request failed. Status:', xhr.status);
            }
        };
        
        xhr.onerror = function() {
            hideTypingIndicator();
            addMessage('Network error occurred. Please check your connection.', 'bot');
        };
        
        xhr.send(`message=${encodeURIComponent(message)}`);
    }
});
