<img src="../img/redis-logo-full-color-rgb.png" height=100/>

# Exercise 4 - Spring Session Redis - Solution and hints

### Solution Project
The solution project can be found [over here](exercise4) and the [pom.xml](exercise4/pom.xml) will show you all the dependencies that are required as well as the versions that were used to create the solution. Since the full solution is included, feel free to try and run it! Note that no additional dependencies were added compared to Exercise 2.

Most important parts of the solution:
* The [Exercise4Controller](exercise4/src/main/java/com/redis/lars/exercise4/Exercise4Controller.java) class

### Troubleshooting/hints

Q: Even when using Spring Session Redis dependency, I still need to login every time I stop/start the app?

A: Did you add both the Spring Session Redis and the Spring Boot Starter for Redis Reactive?