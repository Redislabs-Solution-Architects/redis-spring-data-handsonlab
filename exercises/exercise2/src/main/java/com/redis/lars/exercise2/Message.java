package com.redis.lars.exercise2;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash
public class Message {

    @Id
    private String id;
    private String welcomeText;
    private String messageText;

    public Message(String id, String welcomeText, String messageText)  {
        this.id = id;
        this.welcomeText = welcomeText;
        this.messageText = messageText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWelcomeText() {
        return welcomeText;
    }

    public void setWelcomeText(String welcomeText) {
        this.welcomeText = welcomeText;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    
    
}
