# RSCC - Remote Support Connection Client

RSCC is a remote support application to establish a connection between two clients and start a VNC-Connection, in multiple ways.

Key features:
 - Uses VNC server and viewer
 - Client-to-Client (P2P) connection using ICE and STUN
   - Fallback solution using a relay-server
 - Runs on Linux only, tested with Lernstick
 - Open Source

The use cases always include a *requester* who seeks support and his *supporter*. The scenarios are the following:
 - Supporter has public IP
    - Supporter starts Viewer in listening mode
    - Requester starts VNC server in connection mode
 - Both clients (supporter and requester) are behind a NAT
    - A relay server (keyserver) is used to generate a *key*
    - The clients establish a connection through the relay server
    - The clients run ICE to establish a direct connection using STUN/UDP hole punching
    - If ICE is successful, the connection is tunneled over RUDP (TCP over UDP), which results in a direct (P2P) connection
    - If no direct connection is possible, the realy server is the fallback and will route the traffic (TCP)
 
## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- Working Linux installation
  - preferably latest [Lernstick build](http://lernstick.ch/releases/lernstick_debian8_latest.iso)
- Java IDE of your choice (e.g. IntelliJ Idea, Eclipse, NetBeans, ...)
- Most recent []Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- VNC server (x11vnc)
- VNC viewer (vncviewer)
- Python
- Maven
- Access to a running Server (use this [Docker image](https://hub.docker.com/r/jpduloch/p2p/) based on this [repository](https://github.com/jpduloch/p2p))
```
Give examples
```

### Installing

Seting up the Development-Environment
1. Make sure you have all the prerequisites from above
2. Clone repository
3. Open project in Java IDE
4. Import pom.xml as Maven project and download all dependencies
5. Run mvn package
6. Run the application

## Install application only
You can either create a debian and/or jar package with maven. Alternatively you can download an upcoming version from the imedias servers or the [this github repository](https://github.com/Kennox/rscc/releases).
The ".jar"-file can be run with the following code:
```
java -jar rsccfx.jar
```

To install the debian packge (recommend!) you can run the following command in the respective directory:

```
sudo dpkg -i rsccfx.deb
```
This will create all the necessary files on your local machine (e.g. desktop entry and default imedias supporters).

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

The whitebox tests are being carried out using JUnit 4 and Mockito. Therefore you can run them in your favorite IDE or by using Maven.
````
$ mvn package
````

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

To ensure proper coding style, checkstyle enforces the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Any checkstyle error in the rsccfx package will result into the build failing.
The checkstyle configuration file is located in the path ```config/checkstyle.xml``` and is based on the latest commit of the [official checkstyle implementation](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml) of the Google Java Style Guide.

To check the code for checkstyle violations, run:
```
mvn checkstyle:check
```

## Deployment

To use the RSCC you need a server running the Docker Image.

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

 - 

## Authors

See also the list of [contributors](https://github.com/Kennox/rscc/contributors) who participated in this project.

## License

GPL

## Acknowledgments

* Hat tip to anyone who's code was used
* Inspiration
* etc

