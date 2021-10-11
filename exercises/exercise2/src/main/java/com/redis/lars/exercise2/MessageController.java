package com.redis.lars.exercise2;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final MessageCrudRepository repo;

    public MessageController(MessageCrudRepository repo) {
        this.repo = repo;
        Message hp = new Message("Hello", "World", "Good to see you");
        repo.save(hp);
    }

    @GetMapping
    public Message hello() {

        Optional<Message> response = repo.findById("Hello");
        return response.isPresent() ? response.get() : new Message("nothing", "found", "in repo");

    }

}
