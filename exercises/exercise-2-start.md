<img src="../img/redis-logo-full-color-rgb.png" height=100/>

# Exercise 2 - Hello Redis Spring Boot world!
Enough CLI, it's time to start writing an actual application! In this exercise we'll build a basic (Spring Boot)[https://spring.io/projects/spring-boot] app that interacts with Redis. For this exercise, the difficulty will be ramped up a little, so we're not telling you exactly what to type. Instead we expect you to do some searching yourself. If you're stuck, don't dwell too long on it. Instead, check out the hints, the (solution)[exercise-2-solution.md] or reach out to one of the instructors, who will be quite happy to help!

## Goals

* Build a basic Spring Boot Redis app
* Learn what dependencies are used/needed

## Exercise
### Creating a new project
Create a new Maven Spring Boot project by either going to [start.spring.io](https://start.spring.io) or by using the wizard in your IDE of choice.
* Select the latest Spring Boot version (we used 2.5.5 in our solution), pick a group and artifact id and add the `Web` starter, `Spring Data Redis Reactive`, as well as the `Actuator` dependencies (we won't be using the last one actively but it will come in handy in case you need to troubleshoot).
* Create the zip file and download it, unzip it and open the project folder in your favorite IDE (this step can be skipped if you used the wizard in your IDE of choice in which case you already have the project in your IDE).
* Verify that the application builds by executing:
```
./mvnw package
```
This should all be working successfully. Now that we have bootstrapped a basic Spring Boot App, let's add some Redis in there.
* We'll start by creating a data structure in Java, so let's make a simple `Message` POJO and give it two `String` attributes: `welcomeText` and `messageText`.
* Add an id attribute of type `String` and add the `@Id` annotation to it so Spring Data knows that this is the id column.
* We need to tell Spring Data that this is a structure that we want to store as a Redis hash, so add the `@RedisHash` annotation to the class.
* Next, we'll add a `MessageCrudRepository` interface that extends `CrudRepository<Message, String>`. Be sure to add the `@Repository` annotation so that Spring Data can autowire an implementation of this interface.
* Add a `MessageController` class that injects the `CrudRepository` in its constructor. In the constructor, create a new instance of our `Message` POJO, populate the attributes and store it in the Repository by its id. Add the `@RestController` annotation to the class.
* Add a `hello()` method to the RestController with a `@GetMapping` annotation and the return type being our `Message` POJO and retrieve and return the value from the Repository using the `findById(..)` method.
* Build the project and run it:
```
./mvnw package spring-boot:run
```
* Navigate to http://localhost:8080 and there should be your first Java/Redis/Spring Data app up and running!
Wooohooo! You are now a veteran Spring Boot/Spring Data/Redis developer!
* Now let's take a look at what actually happened on the Redis side of things. Open up the Redis CLI again and check out which keys are present in Redis after running the application:
```
keys *
```
You will notice there are two new keys present. Both start with the package and class name of your `Message` class. Since we don't know what Spring Data created for us, let's inspect these using the `type <key>` command. You'll notice that one is a Set and one is a Hash.
* Check the content of the Set by using:
```
smembers <key>
```
You'll see that it contains one member, the id of your stored `Message`. For every `Message` you add to the repository, this set will be updated. Likewise for removals and updates.
* We can also check the contents of the Hash by typing:
```
hgetall <key>
```
You should see all your key/value pairs, the id key/value pair and a `_class` key that Spring Data uses to determine the Class of the Hash. For each new `Message` that you add, you should see new keys appear in Redis, where Spring Data generates the key by taking the fully qualified class name, followed by a `:` and the id of the `Message`.

For more info on the core concepts of Spring Data Redis, feel free to check out the excellent [Spring Data Redis documentation]([)https://spring.io/projects/spring-data-redis)


* Take a short break if you want, or play around a bit more with the basics, and then move on to [exercise 3](exercise-3-start.md) when you're ready.