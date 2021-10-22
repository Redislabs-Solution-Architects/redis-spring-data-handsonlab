package com.redis.lars.exercise5.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash
public class BankAccount {

    @Id
    private Long id;
    private String account;
    private String accountHolder;
    private String accountType;
}
