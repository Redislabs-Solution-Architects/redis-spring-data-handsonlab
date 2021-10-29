package com.redis.lars.exercise5;

import java.time.Duration;

import com.redis.lars.exercise5.bootstrap.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer.StreamMessageListenerContainerOptions;
import org.springframework.data.redis.stream.Subscription;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

@Component
public class BankTransactionForwarder
        implements InitializingBean, DisposableBean, StreamListener<String, MapRecord<String, String, String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankTransactionForwarder.class);
    private static final String TRANSACTIONS_STREAM = "transactions_lars";

    private final Config config;
    private final StringRedisTemplate redis;
    private final SimpMessageSendingOperations smso;
    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> container;
    private Subscription subscription;

    public BankTransactionForwarder(Config config, StringRedisTemplate redis, SimpMessageSendingOperations smso) {
        this.config = config;
        this.redis = redis;
        this.smso = smso;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.container = StreamMessageListenerContainer.create(redis.getConnectionFactory(),
                StreamMessageListenerContainerOptions.builder().pollTimeout(Duration.ofMillis(1000)).build());
        container.start();
        this.subscription = container.receive(StreamOffset.latest(TRANSACTIONS_STREAM), this);
        subscription.await(Duration.ofSeconds(10));
    }

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        LOGGER.info("Message received from stream: {}", message);
        // Stream message to websocket connection topic
        smso.convertAndSend(config.getStomp().getTransactionsTopic(), message.getValue());
        LOGGER.info("Websocket message: {}", message.getValue());
    }

    @Override
    public void destroy() throws Exception {
        if (subscription != null) {
            subscription.cancel();
        }
        if (container != null) {
            container.stop();
        }
    }

}
