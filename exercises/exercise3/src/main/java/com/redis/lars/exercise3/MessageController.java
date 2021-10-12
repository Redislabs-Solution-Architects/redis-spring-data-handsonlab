package com.redis.lars.exercise3;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final MessageCrudRepository repo;
    private final RedisTemplate<String, String> redisTemplate;

    public MessageController(MessageCrudRepository repo, RedisTemplate<String, String> redisTemplate) {
        this.repo = repo;
        this.redisTemplate = redisTemplate;
        Message hp = new Message("Hello", "World", "Good to see you");
        repo.save(hp);
        redisTemplate.opsForValue().set("Hello to you too", "world!");
        Map<String,String> map = new HashMap<>();
        map.put("welcomeText", "Hello");
        map.put("whoText", "World");
        redisTemplate.opsForHash().putAll("myhash2", map);
        redisTemplate.opsForZSet().add("mysortedset2", "two", 2.0);
        redisTemplate.opsForZSet().add("mysortedset2", "three", 3.0);
        redisTemplate.opsForZSet().add("mysortedset2", "one", 1.0);
    }

    @GetMapping
    public Message hello() {

        Optional<Message> response = repo.findById("Hello");
        return response.isPresent() ? response.get() : new Message("nothing", "found", "in repo");

    }

    @GetMapping("/string")
    public String getString()   {
        return redisTemplate.opsForValue().get("Hello to you too");
    }

    @GetMapping("/hash")
    public Map<String,String> getHash()   {
        HashOperations<String, String, String> hashOps = redisTemplate.opsForHash();
        return hashOps.entries("myhash2");
    }

    @GetMapping("/zset")
    public Set<String> getZSet()   {
        return redisTemplate.opsForZSet().rangeByScore("mysortedset2", 1.0, 3.0);
    }

}
