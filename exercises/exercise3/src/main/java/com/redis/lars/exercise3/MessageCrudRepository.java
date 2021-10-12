package com.redis.lars.exercise3;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageCrudRepository extends CrudRepository<Message, String> {
    
}
