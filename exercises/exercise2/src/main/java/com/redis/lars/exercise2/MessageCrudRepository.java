package com.redis.lars.exercise2;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageCrudRepository extends CrudRepository<Message, String> {
    
}
