# Exercise 4 - Spring Session Redis
Now that you've seen how basic Spring Data and Redis work in combination with Spring Boot, let's take a look at a more advanced scenario. Let's assume we have a web application where users are able to log in and store things in their shopping basket, then checkout and pay. Technically, we could store the shopping cart/basket client side, but what if we want to store it serve side? Typically this would require an HTTP Session and some kind of session object to hold data. But what if the server goes down? Or what if your application needs to scale up and add more instances? Or down and remove instances? To make sure your app can handle these scenarios this data needs to be stored somewhere. And in this exercise, that somewhere is going to be Redis! And what's even better, you will hardly need any code or configuration to be able to do it! Let's get started!

## Goal

* Add session support for Redis using Spring Session Redis

## Solution
### Adding Spring Session Redis dependency to our existing app
### Configuring Spring Session to use Redis
### Adding a login page to our app
### Putting it all together