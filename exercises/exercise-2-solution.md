<img src="../img/redis-logo-full-color-rgb.png" height=100/>

# Exercise 2 - Hello Redis Spring Boot world! - Solution and hints

### Solution Project
The solution project can be found [over here](exercise2) and the [pom.xml](exercise2/pom.xml) will show you all the dependencies that are required as well as the versions that were used to create the solution. Since the full solution is included, feel free to try and run it!

Most important parts of the solution:
* The [Message](exercise2/src/main/java/com/redis/lars/exercise2/Message.java) class
* The [MessageCrudRepository](exercise2/src/main/java/com/redis/lars/exercise2/MessageCrudRepository.java) class
* The [MessageController](exercise2/src/main/java/com/redis/lars/exercise2/MessageController.java) class

### Troubleshooting/hints

Q: My application can't connect to Redis
A: Is Redis running, and is it running at localhost and port 6379? Note: you can also specify a different hostname and port using the `spring.redis.host` and `spring.redis.port` properties in your `application.properties` (or `application.yml` if you prefer YAML)

Q: My Repository is not recognized
A: Did you add the `@Repository` annotation to the interface?

Q: My RestController is not recognized
A: Did you add the `@RestController` annotation to the class?