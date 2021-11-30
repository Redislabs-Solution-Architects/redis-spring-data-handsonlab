package com.redis.lars.exercise5;

import java.security.Principal;

import com.redis.lars.exercise5.bootstrap.Config;
import com.redis.lars.exercise5.bootstrap.Config.StompConfig;
import com.redis.lars.exercise5.bootstrap.TimeSeriesCommands;
import com.redis.lars.exercise5.domain.Balance;
import com.redis.lars.exercise5.domain.BiggestSpenders;
import com.redislabs.lettusearch.RediSearchCommands;
import com.redislabs.lettusearch.SearchResults;
import com.redislabs.lettusearch.StatefulRediSearchConnection;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin
public class TransactionOverviewController {

    private static final String ACCOUNT_INDEX = "transaction_account_idx";
    private static final String SEARCH_INDEX = "transaction_description_idx";
    private static final String BALANCE_TS = "balance_ts_";
    private static final String SORTED_SET_KEY = "bigspenders_";

    private final Config config;
    private final StatefulRediSearchConnection<String, String> srsc;
    private final StringRedisTemplate redis;
    private final TimeSeriesCommands tsc;

    public TransactionOverviewController(Config config, StatefulRediSearchConnection<String, String> srsc,
            TimeSeriesCommands tsc, StringRedisTemplate redis) {
        this.config = config;
        this.srsc = srsc;
        this.tsc = tsc;
        this.redis = redis;
    }

    @GetMapping("/config/stomp")
    public StompConfig stompConfig() {
        return config.getStomp();
    }

    @GetMapping("/balance")
    public Balance[] balance(Principal principal) {
        // Exercise 5: TimeSeries, account balance over time
        String balance_ts_key = BALANCE_TS + principal.getName();
        Balance[] balanceTs = null;
        return balanceTs;
    }

    @GetMapping("/biggestspenders")
    public BiggestSpenders biggestSpenders(Principal principal) {
        // Exercise 5: Sorted Set, biggest spenders on account
        String biggestSpendersKey = SORTED_SET_KEY + principal.getName();
        return new BiggestSpenders(0);
    }

    @GetMapping("/search")
    @SuppressWarnings("all")
    public SearchResults<String, String> searchTransactions(@RequestParam("term") String term) {
        // Exercise 5: Search transactions
        SearchResults<String, String> results = null;
        return results;
    }

    @GetMapping("/transactions")
    public SearchResults<String, String> listTransactions() {
        RediSearchCommands<String, String> commands = srsc.sync();
        SearchResults<String, String> results = commands.search(ACCOUNT_INDEX, "lars");
        return results;
    }

}
