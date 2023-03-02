Algorithms for machine scheduling with malleable jobs
---

An Implementation of the algorithms from Jansen & Land and Grage & Jansen, building upon an algorithm from Mounié, Rapine and Trystram.


# A couple of Maven commands

Once you have configured your project in your IDE you can build it from there. However if you prefer you can use maven from the command line. In that case you could be interested in this short list of commands:

* `mvn compile`: it will just compile the code of your application and tell you if there are errors
* `mvn test`: it will compile the code of your application and your tests. It will then run your tests (if you wrote any) and let you know if some fails
* `mvn install`: it will do everything `mvn test` does and then if everything looks file it will install the library or the application into your local maven repository (typically under <USER FOLDER>/.m2). In this way you could use this library from other projects you want to build on the same machine

If you need more information please take a look at this [quick tutorial](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html).



# Execution with docker-compose
Hint: before execution docker-compose the images need to be build using docker. For this see section Docker.

Docker-compose can be started with the following command:
```
docker-compose up -d
```

The logs of each container should look something like this:
```
19:33:00.888 [main] INFO  de.ohnes.App - Starting Algorithm!
19:33:01.337 [main] INFO  de.ohnes.DualApproximationFramework - Starting dual approximation Framework with shelvesAlgo: KilianApproach
19:33:04.141 [main] INFO  de.ohnes.App - Ran instance with 90 machines and 81 jobs in 2 seconds.
19:33:04.142 [main] INFO  de.ohnes.DualApproximationFramework - Starting dual approximation Framework with shelvesAlgo: KilianApproach
```

# Building docker images
The docker image can be build with the following command.
```
docker build -t malleable .
```
With the current configuration of the `Dockerfile` the `target/bachelorarbeit-1.0-SNAPSHOT-jar-with-dependencies.jar` will be used, so make sure to call `mvn package` before.
The other configuration in this file are only defaults and can be changed in the `docker-compose.yml`.