<img src="../img/redis-logo-full-color-rgb.png" height=100/>

# Exercise 3 - Redis Data Structures and Java - Solution and hints

### Solution Project
The solution project can be found [over here](exercise3) and the [pom.xml](exercise3/pom.xml) will show you all the dependencies that are required as well as the versions that were used to create the solution. Since the full solution is included, feel free to try and run it! Note that no additional dependencies were added compared to Exercise 2.

Most important parts of the solution:
* The [MessageController](exercise3/src/main/java/com/redis/lars/exercise3/MessageController.java) class

### Troubleshooting/hints

Q: My `redisTemplate.opsForHash(..)` method is returning `Map<Object,Object>` and I need `Map<String,String>`. How to get rid of the compiler warning?

A: In the solution project, we cast HashOperations<Object, Object, Object> to HashOperations<String, String, String> before getting the entries out. There may be more efficient ways to achieve the same. ;)