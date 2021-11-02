<img src="../img/redis-logo-full-color-rgb.png" height=100/>

# Exercise 5 - Putting it all together

In this exercise we will put everything that we've learned together as well as adding a few more interesting features. We'll be building a real application this time so let's get started! For this part we've provided you with a basic scaffolding application with a fully functional front-end which can be found [over here](exercise5-start/). This will be the starting point for this exercise. You will be building the backend to make the whole application fully functional. We also provided a separate app for test data generation that can be found [over here](excercise5-datageneration) Last, but not least, you can find a fully working solution [over here](exercise-5-solution).

## Architecture
Data generation app -> Generates bank transactions and puts them on a Redis Stream. It also populates a RediSearch index and creates a TimeSeries for the account balance and a Sorted Set for the 'biggest spenders' on the account. The transactions Stream is stored under a key called `transactions_lars` (as `lars` is the only user in the app at the moment), the TimeSeries under a key called `balance_ts_lars`, the Sorted Set under a key called `bigspenders_lars` and the search indices under a general key. 

The app that we're going to be working on will consume these data structures and show them in a UI (which has been provided).

You can view a picture of the overall architecture [over here](../img/architecture.png).

## Goals

* Build a backend for a mobile banking app
* With a login page
* A streaming overview of transactions
* A searchable transaction overview
* Displays your bank balance over time
* An overview of 'biggest spenders on your bank account

## Application setup

For this exercises, we've provided you with a ready-to-go app to generate bank transaction data into Redis for you, so you do not have to write this yourself. You can find this app [over here](exercise5-datageneration). Build the app using

```
./mvnw clean package
```

But don't run it just yet, we'll run it later after we've finished building our own app. The data generation app generates bank transactions every ten seconds and puts them on a Redis Stream. A Stream is another Redis data structure (essentially an append-only log) and we'll be consuming the Stream in our own app. You can read more about Redis Streams in the [documentation](https://redis.io/topics/streams-intro). The app also populates a RedisTimeSeries of the account balance, a Sorted Set for the biggest spenders and a search index for RediSearch. We will be consuming these from our app as well.

* Start with the basic application provided [over here](exercise5-start) as this already contains the front-end that we will be using to interact with our backend application. You can build and run the application using:

```
./mvnw clean package
./mvnw spring-boot:run
```

When you run the application you can login using the `lars/larsje` user/password combo. You should be able to login, but the following screen is still kind of empty. So let's stop the app for now, and then do something about that!

### Consuming a Redis Stream
* Consume the transactions Stream

A [Redis Stream](https://redis.io/topics/streams-intro) is an append-only log. The data generation app will put transaction on the Stream, and our application will subscribe to this Stream and will consume any income transactions and send them to the frontend over a Websocket connection.

* Add a new class called `BankTransactionForwarder` to the application that implements the `InitializingBean`, `DisposableBean`, and `StreamListener<String, MapRecord<String, String, String>>` interfaces. Also give it an `@Component` annotation so Spring will pick it up and autowire the dependencies.


The first two interfaces make us implement the `afterPropertiesSet()` method and the `destroy()` method which we will use for setup and cleanup of our subscription to the Redis Stream. The third one makes us implement the `onMessage(MapRecord<String, String, String> message)` which is the callback that will be executed whenever a new BankTransaction comes in via the Redis Stream.


* In the constructor of the `BankTransactionForwarder`, add three parameters to the method signature (which will be autowired): a `RedisTemplate`, a `SimpMessageSendingOperations` and a `Config` object (the `Config` object is provided in our application already and provides Stomp/Websocket configuration info from our `RestController` to the frontend). Store them in instance variables.
* In the `afterPropertiesSet()` method, create a new `StreamMessageListenerContainer<String, MapRecord<String, String, String>>` object and store it in an instance variable called `container`. To create the `StreamMessageListenerContainer`, use the following: `StreamMessageListenerContainer.create(redis.getConnectionFactory(), StreamMessageListenerContainerOptions.builder().pollTimeout(Duration.ofMillis(1000)).build())`.
* In the same method, start the newly created container using `container.start()`
* In the same method, create a new subscription to the Stream using `container.receive(StreamOffset.latest("transactions_lars"), this)` and store it in an instance variable called `subscription`
* In the same method, call `subscription.await(Duration.ofSeconds(10))` to wait until the subscription is active

We are now subscribed to the Redis Stream. Whenever a new message arrives on the Stream, the `onMessage(...)` method of this class will be called. Of course, we still need to implement this method, so let's do that now.

* In the `onMessage(...)` method, call the `convertAndSend(config.getStomp().getTransactionsTopic(), message.getValue())` on the `SimpMessageSendingOperations` you stored in the constructor.

* In the `destroy(...)` method, we can cancel the `Subscription` and stop the `Container` using 

```java
if (subscription != null) {
    subscription.cancel();
}

if (container != null) {
    container.stop();
}
```

And that's it for this part. What we've just done is create a MessageListener and subscribed to the Redis Stream of BankTransactions and, for each incoming BankTransaction, forward the BankTransaction to the Stomp/Websocket topic. Now let's see how that looks in our application. First: Run the data generation app in a terminal window. After that one is running, in a separate terminal window, build and run our app:

```
./mvnw clean package
./mvnw spring-boot-run
```

If all is well, both should now be running, so navigate to http://localhost:8080 and log in using `lars/larsje`. You should now see an overview of transactions and a few not-yet functional bits (because we will be enabling those next!). If all is well you should see new transactions popping in every ten seconds or so. If not, check your source code and see if you missed any annotations or other parts. Next, let's add the search feature! 

### Adding RediSearch capabilities

* Start by stopping both the data generation app and our banking app.
* In the existing `TransactionOverviewController` class we've provided most of the scaffolding for you so you can focus on building in the Redis and Spring Data bits.
* Inside the `searchTransactions(@RequestParam("term") String term)` method, we'll use RediSearch to search the transactions generated by the data generation app and present them onto the UI. We will do so by adding the following code:


```java
    RediSearchCommands<String, String> commands = srsc.sync();

    SearchOptions options = SearchOptions
            .builder().highlight(Highlight.builder().field("description").field("fromAccountName")
                    .field("transactionType").tag(Tag.builder().open("<mark>").close("</mark>").build()).build())
            .build();

    SearchResults<String, String> results = commands.search(SEARCH_INDEX, term, options);
```

When adding imports, choose the `com.redislabs` imports.

There are quite a few things going on here but what does this all mean? The first statement opens a RediSearch connection using the RediSearch client library (already on the classpath). The second one creates SearchOptions, based on our preferences. Notice how we're searching the description, transactionType and fromAccountName fields of the `BankTransaction`. Also notice that we highlight the search results (the UI uses these tags to display a different style, you can try this out in a minute)
The third statement executes the search and returns the results. 

* Now let's run the data generation app again (no need to rebuild it), and then build and run our banking app. Then navigate to http://localhost:8080 and try out the search. 
* Enter the letters `onli` in the search box and see what happens. Notice how it selects all transactions of Type `Online payment` and highlights the word `Online`.
* Now enter the letters `paym` in the search box and see what happens. Notice how it selects all transactions with the word `payment` in either the Description or Type.
* Now enter the letters `BMW` in the search box and see what happens. Notice how it selects all transactions with the word `BMW` in the Name field.
* Play around with the search a bit more if you want or move on to the next part.

### Adding the balance over time view

Now that we've got the search and the transaction overview working, let's do the balance over time view. The data generation app also populates a TimeSeries for the bank accounts balance, updating the TimeSeries every time there is a new transaction. We can query this TimeSeries and pass the result to the UI so it can show the balance over time in a nice visual diagram. Let's get started:

* Stop both the data generation app and banking app.
* In the In the `TransactionOverviewController` class in the `balance(Principal principal)` method, add the following:

```java
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
```

What we're doing here is asking "Give us the time series values stored under the key of BALANCE_TS between now and (now -1 week)". We then copy the results into a `Balance` array (the UI requests the data in this format for display) and return it. Notice how we're using the `Principal` name here to suffix the TimeSeries key. This means that if we log in with a different user, it will be a different key. (Of course our data generation app doesn't support more than one user at the time, but the app that we are building supports multiple users)

* Start the data generation app.
* Build and run the app and see the balance over time populateed. Also notice how it updates with each incoming transaction.

Note that in this example, we did not use an additional client library. Instead we extended the [Lettuce library](https://lettuce.io/) (this is the client library that Spring Data uses under the hood) and provided an interface that we injected. Extending Lettuce is a great way of adding Redis Module functionality into your app (Lettuce, and most other client libraries, only support core Redis OSS data structures and/or specific module additions, but not everything) while keeping the amount of dependencies down.

### Adding the 'biggest spenders' feature to the app

Now, last, but certainly not least, let's add the 'biggest spenders' functionality. This will show the biggest deductors from your bank account, in other words: 'where is my money going?' For this purpose, the data-generation app is populating a Sorted Set. For every transaction it will add the value of the transaction to a member to the sorted set of which the key is the counter-account's name and the value is the total amount of the transactions for that account. That means that the set is ordered by the total amount of transactions for that account, so we can show the 'biggest spenders' on our account, or 'where is my money going?'!

* Stop both the data generation app and banking app.
* In the `biggestSpenders()` method, add the following code:

```java
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
```

This will retrieve the members of the Sorted Set by their scores from 0 up to `Double.MAX_VALUE`. We then put the result in a `BiggestSpenders` object (which is suitable for the UI component to display).

* Run the data generation app.
* Build and run the app, and watch in awe as the biggest spenders are now shown in the app!

## What's happening in Redis?

Let's take a look at what's happening in Redis while we did this. First we'll take a look at what the data generation app is populating. If you want to see how the data generation app works internally, feel free to check out its source code (same for the front-end, feel free to check out how it works). Now let's use the Redis CLI to check out what's going on under the hood.

### Redis Stream

The data generation app generates a bank transaction every 10 seconds or so and puts in on a Stream. You can query the stream using:

```
xread count 10000 streams transactions_lars 0-0
```

This will give all of the entries on the `transactions_lars` Stream (assuming you have less than 10000 items of course). Remember that our app subscribed to this Stream via Spring Data, so every time a new element is added to the Stream it's being processed by our app and sent to the UI.

### Sorted Set

The data generation app also adds every transaction to a Sorted Set. It's adding the transaction amount to the score, and the key of the member of the set is the name of the account. So it adds up all the transaction amounts per account name and stores this in a Sorted Set. So we can get the biggest spenders out by simply querying the Sorted Set like this:

```
zrangebyscore bigspenders_lars 0 10000 withscores
```

### TimeSeries

The data generation app also adds the bank accounts balance to a TimeSeries every time there is a new transaction being generated. This allows us to view the balance over time using a query such as:

```
ts.range balance_ts_lars 1623000000000 1653230682038 (use appropriate timestamps or it will return an empty array)
```

In the example we don't do any of the advanced features like compacting or aggregation, but feel free to take a look at the [documentation](https://oss.redis.com/redistimeseries/) to learn more about those topics.

### Hashes

To be able to search transactions, each transaction is also stored as a Hash in Redis, so it can be indexed by RediSearch so we can search them, e.g. like:

```
ft.search transaction_description_idx Fuel
ft.search transaction_description_idx Fuel highlight
ft.search transaction_description_idx Fuel highlight tags <mytag> </mytag>

```

### Session data

Last, but not least, session data is also stored in Redis, using the same mechanisms we used in Exercise 4. Once again this required no code from our end whatsoever, adding the dependencies is enough.

## Next steps

This concludes this exercise, and the hands-on lab! We hope you had fun doing the hands-on lab and learned a thing or two!