<img src="../img/redis-logo-full-color-rgb.png" height=100/>

# Exercise 1 - Introduction to Redis
Hello and thank you for joining this hands-on lab! We're happy to have you join, so welcome! This first exercise aims to show you what Redis is, how it can be interacted with and gives a tour of some of the available data types, data structures and modules. This should provide you with the core knowledge that you will need to start building an application with Redis/Spring later on in this hands-on lab. So, without further delay, let's get started!

## Goals

* Learn some of the basic Redis commands by using the [Redis CLI](https://redis.io/topics/rediscli)
* Learn about some of the different [datastructures in Redis](https://redis.io/topics/data-types-intro)

## Exercise
### Starting Redis and opening the CLI
* Open a terminal Window and start a Redis server by using the following command:
```
docker run -p 6379:6379 redis/redis-stack-server:latest
```
* In a second terminal Window, open the Redis CLI by typing:
```
redis-cli
```

By default, the Redis CLI will connect to 127.0.0.1 and port 6379. If you want to use a different hostname and port, you can use the `-h` and `-p` options to specify a diferent hostname and port.

### Strings and hashes
* Let's see if we can store something in Redis and retrieve it again, so let's start simple and start with a basic String key/value pair.
* Add a String key/value pair to Redis with a key of `hello` and a value of `world`:
```
set hello world
```
* Retrieve the key/value pair from Redis
```
get hello
```
Congratulations, you are now a Redis veteran! Of course, `get` and `set` are not the only two commands available for dealing with Strings in Redis. You can find out about all of the other commands for Strings (and all the other datatypes) by visiting the [Redis documentation](https://redis.io/commands/#string)

Let's check out some of the other data structures in Redis as well. Let's start with a Hash. Hashes are typically used to store flat structures with multiple attributes, such as records or structs. A typical application would be to store an HTTP or user session in Redis so you can keep your own application stateless and add/remove instances/pods as needed while still being able to retrieve state from a logged in user. It's typically much faster and more efficient to store this in a solution that's optimised for fast memory access rather than a general purpose solution.
* Let's add a Hash with the key `myhash` and two key/value pairs within that Hash with keys `hello` and `how` and values `world` and `areyou?`:
```
hset myhash hello world how areyou?
```
* Retrieve the Hash and all its key/value pairs from Redis
```
hgetall myhash
```
* Retrieve a single key from the Hash
```
hget myhash hello
```
Did you notice that Redis has different commands for different data structures? For instance, a `get` command will not work on a hash, but it will work on all other data structures. And an `hget` will work on a hash but not on another data structure. If you try this you will get a 'WRONGTYPE' error. Don't know the type of a certain key? You can ask Redis the type of the `myhash` key by typing:
```
type myhash
```
and Redis will tell you that it's a hash. Alternatively, you can type:
```
type hello
```
To find out that this key is a String type. If you want to learn more about hashes then check out all the available commands at the [Redis Documentation](https://redis.io/commands#hash). You can use the drop down list on that [same page](https://redis.io/commands) to select a different data structure to find out what commands apply to that particular data structure.

### Sorted Sets
Now, let's take a look at a Sorted Set. A Sorted Set (as the name implies) is an ordered collection of unique values. In Redis each value will have a score associated with it, and by updating the score as we go along the Set will maintain its ordering according to the score. Think of scenarios like maintaining a high score leaderboard when playing a game, a list of 'biggest spenders' on your bank account or other scenarios where you need to update a ranking/score as more data becomes available in your application.

* We can add members to a Set directly by using the `zadd` command. There is no need to set a key first. So let's add three members to a Sorted Set using the following commands:
```
zadd mysortedset 2 "two"
zadd mysortedset 3 "three"
zadd mysortedset 1 "one"
```
* Notice how the order is off when adding the members to the set (we're adding the second one first, the third one second and the first one third). Let's get the first two members of the Sorted Set by typing:
```
zrange mysortedset 0 1
```
* Notice that this returns "one" and "two". We can also get the last two members of the Sorted Set by reversing the range:
```
zrange mysortedset 0 1 rev
```
This will produce the result "three" and "two". For more information on Sorted Sets and their assocatied commands, check the [Redis documentation](https://redis.io/commands#sorted_set).

Now that you have seen a few of the most commonly used Redis data structures in action, feel free to take a look at what other data types and command are available in the [Redis documentation](https://redis.io/topics/data-types)

### Modules
 But wait, there's more! While Redis has a very powerful set of data structures straight out of the box, it's always possible that you find yourself in a situation where these are not enough to cover your use cases. In those situations Redis Modules comes to the rescue! Modules allow you to extend Redis data structures and features in a modular way; you only add those modules to a specific database that you want and no more. This keeps your Redis setup fast and lean. There are many community modules available and Redis provides several modules itself as well. If that's not enough you can also write your own. The Docker container that we used to start Redis has a few modules bundled with it already, so let's take a look at a few examples. For more information on modules, see the [Redis Modules Hub](https://redis.com/community/redis-modules-hub/) and the [Redis Enterprise Modules](https://redis.com/redis-enterprise/modules/) pages.

### RediSearch
Let's start with RediSearch, a full-text search module for Redis. Retrieving keys by their primary value is fine for many use cases, but what if I have data in the cache that needs to be searched? E.g. a product model, or stores, or transactions by a certain vendor, etc. Typically this is where a key/value model starts to show its limitations. RediSearch to the rescue! RediSearch adds full-text search capabilities to Redis as well as a lot more. See the [RediSearch documentation](https://docs.redis.com/latest/modules/redisearch/) for more detailed information on this module, or checkout the [GitHub repo](https://github.com/RediSearch/RediSearch).

* First, we'll create a search index on Hash structures matching a certain prefix:
```
ft.create my_idx on hash prefix 1 my schema hello text phonetic dm:en
```
This command is a little bit more elaborate than the previous ones, so let's explore it in detail a bit more. We're creating an index called `my_idx` on the `hash` datastructure with one prefix `my` (remember that we created a Hash earlier that had the key `myhash`? also note that if you used a different key for the hash that you should also use a different prefix here) and we define the schema to be on the `hello` field (this field is present in the Hash we created earlier) which we define as a `TEXT` field and we also setup phonetic search using the `Double Metaphone for English` matcher. That's quite a handful, so don't worry if that doesn't mean a lot to you right now, let's just see what it actually does!
* Let's search our immense data set of 1 Hash and see if we can find what we want by typing a few different commands:
```
ft.search my_idx "world"
```

This will return our Hash that we created earlier.

* You can also do a wildcard search:
```
ft.search my_idx "wor*"
```
* Remember that we setup the index to be phonetic? Try it out by something that sounds like `world` but not quite:
```
ft.search my_idx "wurld"
```
This is great if your users need to search for something, but may not be able to recall exactly what it was they are searching for, or how it is spelled exactly. Another great feature is that search results can be highlighted on which part the match was found:
```
ft.search my_idx "world" highlight
```
Notice how the word `world` has no been surrounded by ```<b></b>``` tags. This is great in case we want to visually display the matching words differently in our UI. We can also change the tags by doing the following:
```
ft.search my_idx "world" highlight tags <hello> </hello>
```
Notice how the word `world` is now surrounded with the tags of our choosing.

And that's not all of the module's functionality; there's plenty more, so if you want to learn more about all the functionality of the RediSearch module, please check the [RediSearch documentation](https://oss.redis.com/redisearch/).

### RedisTimeSeries
Last for this exercise, but certainly not least, let's take a look at the RedisTimeSeries module. Where RediSearch is a module that adds capabilities to existing data structures, RedisTimeSeries is a module that adds a whole new data structure: Time Series. As a developer you may already be familiar with a lot of time series data already, e.g. are you monitoring memory usage over time in your production environment, or CPU usage, or maybe data from IoT sensors? 

* Let's see how this works in Redis and start by creating a Time Series data structure and put some data in it:
```
ts.create my_ts retention 0
```
This will create a Time Series called `my_ts` with its retention set to 'forever', e.g. there's not a moving window after which data values are discarded. Typically time series data would be discarded after doing aggregations, downsampling etc. in order to conserve resources and keep your data set small (==fast!). But since we don't want to be pressed by time in these exercises and we won't add a ton of data we'll store it forever (well, at least until you shut Redis off). Also notice that in the previous part we used `ft.<command>` and now we are using `ts.<command>`? That's because module commands have their own namespaces, so they do not interfere with the existing core Redis commands.

* Let's put some data in the Time Series and see what happens by typing the following commands:
```
ts.add my_ts 0 1.0
ts.add my_ts 1 2.0
ts.add my_ts 2 3.0
```
This will add three values to the Time Series at timestamps 0, 1 and 2 respectively. From your app, you'd probably add something like `System.currentTimeInMillis()` in here, but since we're typing things manually in this exercise we'll keep it short.
* Get the most recent value of the Time Series by typing:
```
ts.get my_ts
```
This will show you the most recent value of the Time Series, in this case `3`. 
* If we want to get the full range of the Time Series, we can type:
```
ts.range my_ts 0 2
```

In your own app you could try something like 'give me all the values in this Time Series between now and a week ago' or something similar. We will get to that in the other exercises when we'll be doing some actual coding! For now, we're done with the basics of Redis and the CLI. If you want to read more about time series, aggregation, compaction and downsampling then check out the [RedisTimeseries documentation](https://oss.redis.com/redistimeseries/).

## Next steps

Well done, you made it through the first exercise! Take a short break if you want, and then move on to [exercise 2](exercise-2-start.md).
