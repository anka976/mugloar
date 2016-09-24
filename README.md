# Dragons of Mugloar Battle Client

This is a [Spring Boot](https://spring.io/) Rest Client application, and it uses RestTemplate to communicate with [Mugloar Battle API](http://www.dragonsofmugloar.com/api) . For solving battles client requests also the [Mugloar Weather API](http://www.dragonsofmugloar.com/weather).

Project requires Java 8. Main logic is stored in `mugloar.Application` class.

To start battles you need to clone the project and run it with Maven:

     mvn spring-boot:run
Spring Boot will download all the dependencies and load the JVM. 
In the beginning the client will ask for desired number of battles:
![Console view](https://raw.githubusercontent.com/anka976/mugloar/master/src/test/resources/battle-run.png "Console view")

Wait for all the battles are finished. You'll see a message like this: 

	============ Final stats for 2000 battles: 1828 won, 172 lost.
	
The average win-ratio for dragons is about 90%

Verbose log will be saved to a rolling file in the project root:

    battle.log
