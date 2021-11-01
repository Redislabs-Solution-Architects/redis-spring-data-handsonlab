<img src="../img/redis-logo-full-color-rgb.png" height=100/>

# Exercise 3 - Redis Data Structures and Java
Now that you have made your first Spring Boot/Spring Data/Redis application, let's add some of the other Redis data structures to the application. You can use the application that you made in exercise 2, make a new one, or start with the provided exercise 2 solution, which can be found [over here](exercise-2-solution.md). Feel free to check out the [Exercise 3 solution](exercise-3-solution.md) in case you run into issues or get stuck. Don't hesitate to reach out to the instructors who are always happy to help!

## Goals

* Learn how to interact with Redis data structures via the `RedisTemplate`

## Exercise
### Adding support for other data structures
In Exercise 2 we saw [Spring Data Repositories](https://docs.spring.io/spring-data/data-commons/docs/2.5.5/reference/html/#repositories) in action when using them to store/retrieve Redis hashes. In this exercise we'll focus on some of the other data structures and for this purpose we will be using the `RedisTemplate` provided by Spring Data Redis. `RedisTemplate` provides you with an abstraction of the Redis commands API. You can add code in your existing code or you can do it in a new app, whichever your prefer.

* In your `MessageController`s constructor, add a `RedisTemplate<String, String>` parameter and store it in an instance variable called `redisTemplate`.
* In the constructor, call the `redisTemplate.opsForValue().set(...)` method to set a String key and value pair to the values `Hello2u2` and `world!` respectively.

Notice how when you use Code Assist/Autocomplete (if you're using an IDE that has that functionality) on the `redisTemplate.ops` partial, that there are different 'ops...' (operations) methods for each datastructure in Redis. Remember from the previous exercise where we learned that different data structures have different command sets? This is reflected in the `RedisTemplate` and as a result it uses the same structure. Let's add some more data structures:

* In the constructor, create a `HashMap<String, String>` that takes key/value pairs in the form of Strings and put some keys/values in it.
* Use the `redisTemplate.opsForHash().putAll(...)` method to store the contents of the HashMap into Redis under the key `myhash2`.

Notice how this differs from what we did in Exercise 2. In Exercise 2 we used a `Repository` to store a Hash based on a POJO and let Spring Data figure out how to handle this in Redis under the hood. In this exercise we are creating the data structure itself (in this case a `HashMap`) and storing it in Redis. Two different, but both valid approaches. Now let's see how we can use the `RedisTemplate` to store a Sorted Set.

* In the constructor, use the `redisTemplate.opsForZSet().add` method to add a few entries to a Sorted Set called `mysortedset2`. Add entries called "two" with a score of 2.0, "three" with a score of 3.0 and "one" with a score of 1.0. (These are the same entries we added to a Sorted Set during Exercise 1)
* In your `MessageController`, add a `getString()` method with a `@GetMapping("/string")` and, using the `RedisTemplate`, return the String value that you stored in the constructor.
* In your `MessageController`, add a `getHash()` method with a `@GetMapping("/hash")` and, using the `RedisTemplate` return the `Map<String, String>` value that you stored in the constructor.
* In your `MessageController`, add a `getZSet()` method with a `@GetMapping("/zset")` and, using the `RedisTemplate` return the `Set<String>` values that you stored in the constructor by their scores.
* Build and run your application.
* Navigate to http://localhost:8080, http://localhost:8080/hash, http://localhost:8080/string and http://localhost:8080/zset and observe the beauty of your own written Redis/Spring Data application!

Notice how we're using hashes in two different ways in this hands-on lab. Using the `Repository` we can use our own domain model and let Spring Data do the heavy lifting for us to store our object in Redis as a hash. Using the `RedisTemplate` we gain access to the Redis API itself and this will give us more fine grained control over the hash. Note that you can still add a lot of control using the `Repository`/`@RedisHash` mechanic, but we did not explore that in this exercise. Check out the Spring Data documentation if you want to learn more about that.

* Take a short break if you want, maybe take a peek at the hinst and solution and then move on to [exercise 4](exercise-4-start.md).