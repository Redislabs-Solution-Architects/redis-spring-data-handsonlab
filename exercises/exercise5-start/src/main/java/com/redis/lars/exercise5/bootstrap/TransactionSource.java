package com.redis.lars.exercise5.bootstrap;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSource {

    private String fromAccountName;
    private String description;
    private String type;

}
