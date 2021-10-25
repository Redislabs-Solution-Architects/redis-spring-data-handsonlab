package com.redis.lars.exercise5.bootstrap;

import com.redis.lars.exercise5.domain.BankTransaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTransactionRepository extends CrudRepository<BankTransaction, String>{
    
}