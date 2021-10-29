<img src="../img/redis-logo-full-color-rgb.png" height=100/>

# Exercise 4 - Spring Session Redis
Now that you've seen how basic Spring Data and Redis work in combination with Spring Boot, let's take a look at a more advanced scenario.

 Let's assume we have a web application where users are able to log in and store things in their shopping basket, then checkout and pay. Technically, we could store the shopping cart/basket client side, but what if we want to store it server side? Typically this would require an HTTP Session and some kind of session object to hold data, typically stored in-memory. But what if the server goes down? Or what if your application needs to scale up and add more instances? Or down and remove instances? What if the users next request ends up on a different server/instance of your app? 
 
 To make sure your app can handle these scenarios this data needs to be stored somewhere. And in this exercise, that somewhere is going to be Redis! And what's even better, you will hardly need any code or configuration to be able to do it! In case you run into issues or get stuck, feel free to check the [Exercise 4 solution](exercise-4-solution.md) or reach out to one of the instructors who are always happy to help!

## Goals

* Add a login page to your application
* Add session support using Spring Session Redis

## Exercise

### Adding a login page to our app
* In order to have a session that needs to be stored, we need a user to login to our application. For this we will add the Spring Security Spring Boot starter to the `pom.xml` file of our app. You can use the app from Exercise 3 for this, or create a new one. Don't forget to include the other necessary dependencies.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
* Add a `SecurityConfiguration` class to configure the login page and authentication mechanism. Since there are many options available to configure Spring Security and getting the exact correct configuration right can be tricky, we've added the full code for you below. If you really want to, you may write it yourself of course!
```java
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .authorizeRequests()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginProcessingUrl("/perform_login")
        .defaultSuccessUrl("/")
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception   {
        auth.inMemoryAuthentication()
        .withUser("lars")
        .password("{noop}larsje")
        .roles("USER");
    }

}
```

The security configuration does two things. The first part (`configure(HttpSecurity) http`) sets up the login process and ensures that all the requests to our application require authentication. The second part (`configure(AuthenticationManagerBuilder auth)`) sets up an in-memory user database with a single user called `lars` and password set to `larsje`. In your production applications this would hopefully be delegated to something more secure and extensive.

* Build and run the app, and navigate to http://localhost:8080

Notice that you are now required to log in. Use the user/password that we setup and you will be greeted by your landing page!

* Stop your app
* Run the app again, and navigate to http://localhost:8080

Notice how you now have to login again. This is because logged-in user information was stored into memory and that information was lost when we stopped the app. Not a great experience if your users are navigating your app and collecting information and a server/process goes down or they end up on a different instance. But fear not, Spring Session and Redis are coming to the rescue!

### Configuring Spring Session to use Redis

* Add the Spring Session Redis dependency to the `pom.xml` file by adding the following elements (if not already there) to your `<dependencies>` block:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
```
* Build and run the app, and navigate to http://localhost:8080 You are required to login again.
* Stop the app
* Run the app again, and navigate to http://localhost:8080

Notice how we now do not have to login again. This is because the users session was stored in Redis using Spring Session. Two session cookies are stored client side in order to be able to identify the user session on the server side. So even when your app goes down or the user ends up on a different instance, the session can still be identified and the user can continue where they left off. See the [Spring Session documentation](https://spring.io/projects/spring-session) to find out how to configure all the available mechanisms of Spring Session.

Also notice how little configuration was needed to set this up. Adding the dependency to `spring-session-data-redis` was sufficient and `Spring Boot Autoconfiguration` does the rest. There are of course many options for configuration available should you so desire, and they can be found in the [Spring Session Data Redis documentation](https://spring.io/projects/spring-session-data-redis).

So what did Spring Sesion store in Redis? Open up your Redis CLI and check the keys that are available:
```
keys *
```

You will notice that there are several keys prefixed with `spring:session:`. One of them starts with the prefix `"spring:session:sessions:` and then some UUID. This is the session object. Using
```
type "spring:session:sessions:6b6b08a5-8d84-4bdb-97bc-256743009d1b" (example UUID, yours will be different)
```
We can find out it's a Hash and by using

```
hgetall "spring:session:sessions:6b6b08a5-8d84-4bdb-97bc-256743009d1b" (example UUID, yours will be different)
```
we can see all the key/value pairs of the stored Session object.

So how are these sessions invalidated? Let's find out and type the following:
```
ttl "spring:session:sessions:6b6b08a5-8d84-4bdb-97bc-256743009d1b" (example UUID, yours will be different)
```
and the response will be a number, like 1234. The `ttl` command returns the remaining time-to-live for the specified key. Notice how the number goes down when you repeat the command. When the number reaches zero, the key is removed and the session will be gone and the user is required to log in again. TTL is extended by Spring Session as long as the user remains active in the application. You can verify this by refreshing the page in your browser and then executing the `ttl` command again; the TTL will be set to its original value again.

We can also delete the session by typing:
```
del "spring:session:sessions:6b6b08a5-8d84-4bdb-97bc-256743009d1b" (example UUID, yours will be different)
```
and then refreshing the page. You are now forced to login again.

And that's it for this exercise! Take a short break if you want, and then move to the final exercise, [Exercise 5](exercise-5-start.md).