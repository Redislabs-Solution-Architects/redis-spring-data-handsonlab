package com.redis.lars.exercise5;

import java.security.Principal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.redis.lars.exercise5.bootstrap.Config;
import com.redis.lars.exercise5.bootstrap.Config.StompConfig;
import com.redis.lars.exercise5.bootstrap.TimeSeriesCommands;
import com.redis.lars.exercise5.domain.Balance;
import com.redis.lars.exercise5.domain.BiggestSpenders;
import com.redislabs.lettusearch.RediSearchCommands;
import com.redislabs.lettusearch.SearchOptions;
import com.redislabs.lettusearch.SearchOptions.Highlight;
import com.redislabs.lettusearch.SearchOptions.Highlight.Tag;
import com.redislabs.lettusearch.SearchResults;
import com.redislabs.lettusearch.StatefulRediSearchConnection;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
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
    private static final String BALANCE_TS = "balance_ts";
    private static final String SORTED_SET_KEY = "bigspenders";

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
        Map<String, String> tsValues = tsc.range(balance_ts_key, System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 7),
                System.currentTimeMillis());
        Balance[] balanceTs = new Balance[tsValues.size()];
        int i = 0;

        for (Entry<String, String> entry : tsValues.entrySet()) {
            Object keyString = entry.getKey();
            Object valueString = entry.getValue();
            balanceTs[i] = new Balance(keyString, valueString);
            i++;
        }
        return balanceTs;
    }

    @GetMapping("/biggestspenders")
    public BiggestSpenders biggestSpenders(Principal principal) {
        // Exercise 5: Sorted Set, biggest spenders on account
        String biggestSpendersKey = SORTED_SET_KEY + principal.getName();
        Set<TypedTuple<String>> range = redis.opsForZSet().rangeByScoreWithScores(biggestSpendersKey, 0,
                Double.MAX_VALUE);
        if (range.size() > 0) {
            BiggestSpenders biggestSpenders = new BiggestSpenders(range.size());
            int i = 0;
            for (TypedTuple<String> typedTuple : range) {
                biggestSpenders.getSeries()[i] = Math.floor(typedTuple.getScore() * 100) / 100;
                biggestSpenders.getLabels()[i] = typedTuple.getValue();
                i++;
            }
            return biggestSpenders;
        } else {
            return new BiggestSpenders(0);
        }
    }

    @GetMapping("/search")
    @SuppressWarnings("all")
    public SearchResults<String, String> searchTransactions(@RequestParam("term") String term) {
        // Exercise 5: Search transactions
        RediSearchCommands<String, String> commands = srsc.sync();

        SearchOptions options = SearchOptions
                .builder().highlight(Highlight.builder().field("description").field("fromAccountName")
                        .field("transactionType").tag(Tag.builder().open("<mark>").close("</mark>").build()).build())
                .build();

        SearchResults<String, String> results = commands.search(SEARCH_INDEX, term, options);
        return results;
    }

    @GetMapping("/transactions")
    public SearchResults<String, String> listTransactions() {
        RediSearchCommands<String, String> commands = srsc.sync();
        SearchResults<String, String> results = commands.search(ACCOUNT_INDEX, "lars");
        return results;
    }

}
