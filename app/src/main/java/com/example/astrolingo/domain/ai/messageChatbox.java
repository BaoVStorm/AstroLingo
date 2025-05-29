package com.example.astrolingo.domain.ai;

public class messageChatbox {
    String message;
    boolean isUser;

    public messageChatbox(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }
}
