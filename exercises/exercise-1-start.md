# Exercise 1 - Introduction to Redis
Hello and thank you for joining this hands-on lab! We're happy to have you join, so welcome! This first exercise aims to show you what Redis is, how it can be interacted with and gives a tour of some of the available data types, data structures and modules. This should provide you with the knowledge that you need to start building an application with Redis/Spring later on in this hands-on lab. So let's get started!

## Goal

* Learn a few basic Redis commands by using the Redis CLI
* Learn about some of the different datastructures in Redis

## Exercise
### Starting Redis and opening the CLI
* Open up a terminal Window and start a Redis server
```
docker run -p 6379:6379 redislabs/redismod:latest
```
* In a second terminal Window, open the Redis CLI
```
redis-cli
```
### Strings and hashes
* Let's see if we can store something in Redis and retrieve it again, so let's start simple and start with a basic String key/value pair.
* Add a String key/value pair to Redis "hello" and "world"
```
set hello world
```
* Retrieve the key/value pair from Redis
```
get hello
```
Congratulations, you're now a Redis veteran! 

Now let's do some more interesting stuff and check out some of the other data structures in Redis. Let's start with a hash. Hashes are typically used to store flat structures with multiple attributes, such as records or structs. A typical application would be to store an HTTP session in Redis so you can keep your own application stateless and add/remove instances/pods as needed while still being able to retrieve state from a logged in user. It's typically much faster and more efficient to store this in a solution that's optimised for fast memory access rather than a general purpose solution.
* Let's add a simple hash with two values to Redis
```
hset myhash hello world how areyou?
```
* Retrieve the hash from Redis
```
hgetall myhash
```
* Retrieve a single key from the hash
```
hget myhash hello
```
Did you notice that Redis has different commands for different data structures? For instance, a `get` command will not work on a hash, but it will work on other data structures. And an `hget` will work on a hash but not on any other data structure. If you try this you will get a 'WRONGTYPE' error. Don't know the type of a certain key? You can ask Redis the type of the `myhash` key by typing:
```
type myhash
```
and Redis will tell you that it's a hash. Alternatively, you can type:
```
type hello
```
To find out that this key is a string type. If you want to learn more about hashes then check out all the available commands at the [Redis Documentation](https://redis.io/commands/HGETALL)

### Sorted Sets
Now that we have done this, let's take a look at a Sorted Set. A Sorted Set (as the name implies) is an ordered collection of values. In Redis each value will have a score associated with, and by updating the score as we go along the Set will maintain its ordering according to the score. Think of scenarios like a high score leaderboard when playing a game, or a list of 'biggest spenders' on your bank account.

* We can add members to a set directly by using the `zadd` command. There is no need to set a key first. So let's add three members to a sorted set:
```
zadd mysortedset 2 "two"
zadd mysortedset 3 "three"
zadd mysortedset 1 "one"
```
* Notice how the order is off when adding the members to the set (we're adding the second one first, the third one second and the first one third). Let's get the first two members of the ordered set by typing:
```
zrange mysortedset 0 1
```
* Notice that this returns "one" and "two". We can also get the last two members of the ordered set out by reversing the range:
```
zrange mysortedset 0 1 rev
```
This will produce the result "three" and "two". For more information on Sorted Sets and their assocatied commands, check the [Redis documentation](https://redis.io/commands/zadd)

### Modules
Now that you have seen some of the Redis data types in action, feel free to take a look at what other data types and command are available in the [Redis documentation](https://redis.io/topics/data-types) But wait, there's more! While Redis has a very powerful set of data types straight out of the box, it's always possible that you find yourself in a situation where these are not enough to cover your use cases. In that case, it's Redis Modules to the rescue! Modules allow you to extend Redis data types and also features in a modular way; you only add those modules to that database that you want and no more. This keeps your Redis setup fast and lean. There are many community modules available and Redis provides several modules itself as well. If that's not enough you can also write your own. The Docker container that we used to start Redis has a few modules bundled with it already, so let's take a look at a few examples.

### RediSearch
Let's start with RediSearch, a full-text search module for Redis. Retrieving keys by their primary value is fine, but what if I have data in the cache that needs to be searched? E.g. a product model, or stores, or transactions by a certain vendor, etc. This is where RediSearch comes in; it adds full-text search capabilities to Redis and a lot more. 

* First, we'll create a secondary index on hash structures matching a certain prefix:
```
ft.create my_idx on hash prefix 1 my schema hello text phonetic dm:en
```
This command is a little bit more elaborate than the previous ones, so let's explore it in detail a bit more. We're creating an index called `my_idx` on the `hash` datastructure with one prefix `my` and we define the schema to be on the `hello` field which we define as a `TEXT` field and we also setup phonetic search using the `Double Metaphone for English` matcher. That's quite a handful, so don't worry if that doesn't mean a lot to you right now, let's just see what it actually does!
* Let's search our immense data set of 1 hash and see if we can find what we want by typing a few different commands:
```
ft.search my_idx "world"
```
* You can also do a wildcard search:
```
ft.search my_idx "wor*"
```
* Remember that we setup the index to be phonetic? Try it out by something that sounds like `world` but not quite:
```
ft.search my_idx "wurld"
```
This is great if your users need to search for something, but may not be able to recall exactly what it was they are searching for, or how it is spelled. Another great feature is that search results can be highlighted on which part the match was found:
```
ft.search my_idx "world" highlight
```
Notice how the word `world` has no been surrounded by ```<b></b>``` tags. This is great in case we want to visually display the matching words differently in our UI. We can also change the tags by doing the following:
```
ft.search my_idx "world" highlight tags <hello> </hello>
```
Notice how the word `world` is now surrounded with the tags of our choosing.

And there's plenty more, so if you want to learn more about all the functionality of the RediSearch module, please check the [RediSearch documentation](https://oss.redis.com/redisearch/)

### RedisTimeSeries
Last for this exercise, but certainly not least, let's take a look at the RedisTimeSeries module. Where RediSearch is a module that adds capabilities to existing data structures, RedisTimeSeries is a module that adds a whole new data structure: Time Series. As a developer you may already be familiar with a lot of time series data already, e.g. are you monitoring memory usage over time in your production environment, or CPU usage, or maybe data from IoT sensors? 

* Let's see how this works in Redis and start by creating a time series data structure and put some data in it:
```
ts.create my_ts retention 0
```
This will create a time series called my_ts with retention set to 'forever', e.g. there's not a moving window after which data values are discarded. Typically time series data would be discarded after doing aggregations, downsampling etc. in order to conserve resources and keep your data set small (=fast!). But since we don't want to be pressed by time in these exercises and we won't add a ton of data we'll store it forever (well, at least until you shut Redis off). Also notice that in the previous part we used `ft.<command>` and now we are using `ts.<command>`? That's because module commands have their own namespace.
* Now, let's put some data in the time series and see what happens:
```
ts.add my_ts 0 1.0
ts.add my_ts 1 2.0
ts.add my_ts 2 3.0
```
What this does is add three value at timestamps 0, 1 and 2 respectively. From your app, you'd probably add `System.currentTimeInMillis()` in here, but since we're typing things manually here we'll keep it short.
* Get the current value of the time series out by typing:
```
ts.get my_ts
```
This will show you the most recent value of the time series, in this case `3`. 
* If we want to get the full range, we can type:
```
ts.range my_ts 0 2
```
In your own app you could try something like 'give me all the values in this time series between now and a week ago' or something similar. We will get to that in the other exercises. For now, we're done with the basics of Redis and the CLI. If you want to read more about time series, aggregation, compaction and downsampling then check out the [RedisTimeseries documentation](https://oss.redis.com/redistimeseries/).

Feel free to ask any questions to the instructor(s), they're here to help!

Well done, you made it through the first exercise! Take a short break if you want, and then move on to [exercise 2](exercise-2-start.md).
