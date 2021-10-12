# Exercise 2 - Hello Redis Spring Boot world!
Enough CLI, time to start writing an actual application! In this exercise we'll build a basic Spring Boot app to interact with Redis from your Java application. For this exercise, the difficulty will be ramped up a little, so we're not telling you exactly what to type, but we expect you to do some searching yourself. If you're stuck, don't dwell too long on it. Instead, check out the hints, the solution or call out one of the instructors, who will be quite happy to help!

## Goal

* Build a basic Spring Boot Redis app
* Learn what dependencies are used/needed

## Exercise
### Creating a new project
Create a new Maven Spring Boot project by either going to [start.spring.io](https://start.spring.io) or by using the wizard in your IDE of choice.
* Select the latest Spring Boot version, pick a group and artifact Id and add the Web starter and the Spring Data Redis Reactive dependencies, as well as the Actuator dependency (we won't be using it but it will come in handy in case you need to troubleshoot).
* Create the zip file and download it, unzip it and open the project folder in your favorite IDE (this step can be skipped if you used the wizard in your IDE of choice).
* Now that we have bootstrapped a basic Spring Boot App, let's add some Redis in there.
* We'll start by creating a data structure in Java, so let's make a simple POJO and give it some attributes.
* Add an id attribute of type `String` and add the `@Id` annotation to it so Spring Data knows this is the id column.
* We need to tell Spring Data that this is a structure that we want to store as a Redis hash, so add the `@RedisHash` annotation to the class.
* Next, we'll add a `CrudRepository` that takes a `String` as the key and our POJO as the value.
* Add a `RestController` that injects the `CrudRepository` in its constructor. In the constructor, create a new instance of our POJO, populate it with a few attributes and store it in the Repository.
* Add a method to the RestController with a `@GetMapping` annotation and the return type being your POJO and retrieve and return the value from the Repository.
* Build the project and run it.
* Navigate to http://localhost:8080 and there should be your first Java/Redis/Spring Data app up and running!
* Wooohooo! You are now a veteran Spring Boot/Spring Data/Redis developer!
* Take a short break if you want, and then move on to [exercise 3](exercise-3-start.md).