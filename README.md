Algorithms for machine scheduling with malleable jobs
---

An Implementation of the algorithms from 
- [Mounié, Rapine and Trystram](https://dblp.org/rec/journals/siamcomp/MounieRT07.html)
- [Jansen & Land](https://dblp.org/rec/conf/ipps/JansenL18.html)
- [Grage, Jansen, and Ohnesorge](https://dblp.org/rec/conf/europar/GrageJO23.html)
- Anonymous (for double blind review)


# Getting Started
The easiest way to start is by using docker.
```
docker build -t moldable . && docker run moldable
```
The configuration can be done entirely via environment variables, including the selection of which algorithm to use. For a full list of environment variables please refer to the `Dockerfile`.
We tried our best to set reasonable defaults. **By default the newest (currently still anonymous) algorithm is used**.

# Tests
We ran a series of tests. The results of these test can be found in the `testresults` directory in form of csv files.
We can not include all tested instances in this repo, because of size restrictions, however the subfolder `instances-<date>` contains some of the instances that were generated during our tests. They can be mapped to the corresponding row in the `.csv` file by their `id`.


# A couple of Maven commands

Once you have configured your project in your IDE you can build it from there. However if you prefer you can use maven from the command line. In that case you could be interested in this short list of commands:
œ
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