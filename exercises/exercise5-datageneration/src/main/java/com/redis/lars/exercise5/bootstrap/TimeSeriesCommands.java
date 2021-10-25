package com.redis.lars.exercise5.bootstrap;

import java.util.Map;

import io.lettuce.core.dynamic.Commands;
import io.lettuce.core.dynamic.annotation.Command;

public interface TimeSeriesCommands extends Commands {

    @Command("TS.CREATE :key RETENTION :retentionTime ")
    void create(String key, long retentionTime);

    @Command("TS.ADD :key * :value")
    void add(String key, double value);

    @Command("TS.RANGE :key :from :to")
    Map<String, String> range(String key, long from, long to);

}