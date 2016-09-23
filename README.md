# Dragons of Mugloar Battle Client

This is a [Spring Boot](https://spring.io/) Rest Client application, and it uses RestTemplate to communicate with [Mugloar Battle API](http://www.dragonsofmugloar.com/api) . For solving battles client requests also the [Mugloar Weather API](http://www.dragonsofmugloar.com/weather).

To start battles you need to clone the project and run it with Maven:

     mvn spring-boot:run
Spring Boot will download all the dependencies and load the JVM. 
In the beginning the client will ask for desired number of battles:

    [INFO] --- spring-boot-maven-plugin:1.4.0.RELEASE:run (default-cli) @ mugloar ---
    Please, enter the number of desired battles (default 5):

The average win-ratio for dragons is about 90%

Verbose battle log will be printed to console, and less verbose log will be saved to a rolling file in the project root:

    battle.log
