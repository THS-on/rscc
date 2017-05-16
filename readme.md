# RSCC - Remote Support Connection Client

RSCC is a RemoteSupport application to establish a connection between two clients and start an VNC-Connection, in multiple ways.

Key features:
 - Uses VNC-Server and viewer
 - Client-to-Client connection using ICE / STUN-Technology
 - Fallback solution using a relay-server
 - Runs on Linux only
 - OpenSource

The use cases is allways a Requester who seeks support and his Supporter. With the following scenarios:
 - Supporter has Public IP
    - Supporter starts Viewer in listening mode
    - Requester starts VNC-Server in connection mode 
 - Both Client are behind a NAT
    - A relay server (keyserver) is used to generate a Key
    - Both Clients start a connection over the relay-server
    - Both Clients run ICE to find a direct-connection using STUN/UDP-Holepunching
    - When ICE is successful the connection is tunneled over RUDP (TCP over UDP)
    - When no direct connection is possible, the realy-server is the fallback and will rout the traffic (TCP)
 
## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- Working Linux installation
- Java-IDE of your choice
- VNC-Server (x11vnc)
- VNC-Viewer (vncviewer)
- Python
- Maven
- Access to a running Server (use this [Dockerimage](https://hub.docker.com/r/jpduloch/p2p/) based on this [Repo](https://github.com/jpduloch/p2p))
```
Give examples
```

### Installing

Seting up the Development-Environment

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

The WitheBox-Test are made with JUnit. Therefor you can run them in your favorite IDE or with Maven
````
$ mvn package
````


### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
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

