# Exercise 3 - Redis Data Structures and Java
Now that you have made your first Spring Boot/Spring Data/Redis application, let's add some of the other Redis data structures to the application. You can use the one that you made in exercise 2, or start with the provided exercise 2 solution.

## Goal

* Add some of the other Redis data structures into your application

## Exercise
### Adding support for other data structures
In Exercise 2 we saw Spring Data Repositories in action when using them to store/retrieve Redis hashes. In this exercise we'll focus on the other data structures and for this purpose we will be using the `RedisTemplate`. You can add code in your existing code or you can do it in a new app, whichever your prefer.
* In your controller's constructor, add a `RedisTemplate<String, String>` parameter and store it in an instance variable.
* In the constructor, call the `.opsForValue().set(...)` method to set a string key and value pair.
Notice how when you use Code Assist (if you're using an IDE that has that) on the `RedisTemplate.ops` partial, that there are different 'ops' for every datastructure in Redis. So let's add another couple of lines.
* In the constructor, create a HashMap that takes a keys/value in the form of Strings and put some values in it.
* Use the `opsForHash().putAll(...) method to store the contents of the HashMap into Redis.
* In the constructor, use the `opsForZSet().add` method to add a few items to a Sorted Set. 
* In your `RestController`, add a method with a `@GetMapping` on "/string" url and return the String value that you stored in the constructor.
* In your `RestController`, add a method with a `@GetMapping` on "/hash" url and return the `Map<String, String>` value that you stored in the constructor.
* In your `RestController`, add a method with a `@GetMapping` on "/zset" url and return the `Set<String>` value that you stored in the constructor, using the `opsForZSet().rangeByScore(..)` method.
* Navigate to `http://localhost:8080` as well as the other paths that you defined in your controller and observe the might of your own written Redis/Spring Data application!
Notice how we're using hashes in two different ways in this exercise. Using the `Repository` we can use our own domain model and let Spring Data do the heavy lifting for us to store it in Redis as a hash. Using the `RedisTemplate` we gain access to the Redis API itself and this will give us more fine grained control over the hash. Note that you can still add a lot of control using the `Repository`/`@RedisHash` mechanic, but we did not explore that in this exercise. Check out the Spring Data documentation if you want to learn more about that.
* Take a short break if you want, and then move on to [exercise 4](exercise-4-start.md).