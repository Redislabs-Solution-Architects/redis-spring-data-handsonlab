<img src="img/redis-logo-full-color-rgb.png" height=100/>

## Building lightning fast Java applications using Redis, Spring Data and Spring Session
In this hands-on lab you will learn how to build lightning fast Java applications using Redis and some of its datastructures and modules, together with Spring Data and Spring Session. You will learn how easy it is to build extremely fast applications using only a minimum of code. The entire hands-on lab is self-service and is made up of several exercises. Each exercise comes with a working solution, so feel free to take a peek in case you get stuck!

## Prerequisites

You are expected to bring:

* A laptop with Linux, MacOS, [Windows (10+) with WSL](https://docs.microsoft.com/en-us/windows/wsl/tutorials/wsl-database) or another version of Windows with Linux running inside a virtual machine.
* Alternatively, you can run the whole hands-on lab on [GitPod](https://gitpod.io/#https://github.com/Redislabs-Solution-Architects/redis-spring-data-handsonlab). This comes with all dependencies included.
* A working internet connection (use corporate proxies or bad WiFi at your own peril)

You are expected to have installed:

* [JDK 16 or higher](https://adoptopenjdk.net/?variant=openjdk16&jvmVariant=hotspot) (Note: you should be fine on 11 or up, but we tested this on 16.)
* An IDE of your choice, e.g. [vscode](https://code.visualstudio.com/), [idea](https://www.jetbrains.com/idea/), [eclipse](https://www.eclipse.org/eclipseide/) or [netbeans](https://netbeans.apache.org/)
* Docker desktop (Note: another way of running a Docker container locally is also fine)

We expect you to be somewhat familiar with Java but if you're not: don't worry, working source code is provided for all exercises in this hands-on lab.

## Installing Redis and the Redis CLI and verifying that it correctly installed

MacOS:

```bash
brew install redis
redis-cli --version
```

Linux:

```bash
sudo apt install redis-server
redis-cli --version
```

You should now be all set and ready to go!

## Getting started
This hands-on lab consists of multiple exercises, see the links below. Each exercise has a goal and a set of sub goals to achieve. A working solution is also provided as well as several hints in case you get stuck. Start with exercise 1 and work your way from there. Good luck and enjoy!


## Exercises

* Exercise 1 - Introduction to Redis: [start](exercises/exercise-1-start.md)
* Exercise 2 - Hello Redis Spring Boot world!: [start](exercises/exercise-2-start.md), [solution](exercises/exercise-2-solution.md)
* Exercise 3 - Redis data structures and Java: [start](exercises/exercise-3-start.md), [solution](exercises/exercise-3-solution.md)
* Exercise 4 - Redis and Spring Session: [start](exercises/exercise-4-start.md), [solution](exercises/exercise-4-solution.md)
* Exercise 5 - Putting it all together: [start](exercises/exercise-5-start.md), [solution](exercises/exercise-4-solution.md)


## Tips

* Stuck? The exercises have hints to help you!
* Stuck? Ask your neighbour to pair with you!
* Stuck? Every exercise has a working solution right here! (Except exercise 1, which is more of a walkthrough)
* Still Stuck? Ask one of the instructors!

## Troubleshooting

### General

If your laptop has corporate restrictions in terms of installing software, internet proxies or other restrictions, it might be tricky to get this hands-on lab up and running. Asking the instructor may help, but we can't guarantee we'll get it working and we won't help you circumvent corporate policies. Running the hands-on lab via [GitPod](https://gitpod.io/#https://github.com/Redislabs-Solution-Architects/redis-spring-data-handsonlab) might be a better alternative in that case.

### Redis

Make sure Redis is running when running the exercises. During building this is not required as the integration tests support [Testcontainers](https://www.testcontainers.org/).

# Disclaimer

Redis Labs proprietary, subject to the Redis Enterprise Software and/or Cloud Services license