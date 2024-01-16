# spring-boot-mongo-kafka


# Microservice

> Demo (Handle Kafka & Mongo)

## About

this project uses [Spring Boot](https://spring.io/guides/gs/spring-boot/) that helps creating production-grade and
stand-alone java applications & services with absolute minimum fuss.
The aim of this service is to process overdue receipts/disbursements


## Local development

### Prerequisites
. Java environment
```
> java -version
java 17
```
. Have `Maven` Installed
```
> mvn -version
Apache Maven 3.9.4
```

### Startup using CMD
```
mvn spring-boot:run
```

## Development with docker

Make sure that you have installed the following tools on your local machine
1. [Docker](https://docs.docker.com/install/#supported-platforms),
2. [docker-compose](https://docs.docker.com/compose/install/). (you should have version 1.29 or higher)
>  Installing [Docker Desktop](https://www.docker.com/products/docker-desktop) (for Mac & Windows) is the easiest way to get your environment ready

### Set up MongoDB cluster & Kafka Broker
> Note : We need to create a Mongo Replica Set in order to be able to test some promising features like @Transactional annotation, etc

###### 1. start the mongo databases
```
cd local_dev/docker/mongo
docker-compose up -d
```
check if the containers are running
```
docker ps
```

###### 2. exec into one of the mongos
```bash
docker exec -it localmongo1 /bin/bash
```

###### 3. access mongo console
```bash
mongo
```

###### 4. configure replica set
Initiate a replicaset by pasting the following
```bash
rs.initiate(
  {
    _id : 'rs0',
    members: [
      { _id : 0, host : "<LOCAL_IP_ADDRESS>:27011" },
      { _id : 1, host : "<LOCAL_IP_ADDRESS>:27012" },
      { _id : 2, host : "<LOCAL_IP_ADDRESS>:27013" }
    ]
  }
)
```
You should get the following response `{"ok" : 1 }`

Note: If you get an `errmsg" : "already initialized"` try to remove all docker containers
```
docker ps -a
docker rm <...containers>
```
######  Access the MongoDB cluster
[Compass](https://www.mongodb.com/products/compass) is an interactive tool for querying,
optimizing, and analyzing the MongoDB data


The Connection String URI Format For our Replica Set is as the following
```
mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0
```

> To connect with the mongo cluster from our application, the **database_name** option should be included in the URI


### Run Kafka Server
```bash
cd local_dev/docker/kafka/
docker-compose up
```

Before starting the stack ensure that your docker host ip is well defined
```
export DOCKER_HOST_IP=127.0.0.1
```
(that's the default value)

###### Visualize kafka cluster

[Offset Explorer](https://www.kafkatool.com/) is one of the best kafka UI tool that allow to quickly view our kafka cluster, including the borkers, topics and consumers
1. Download & Install Offset Explorer
2. Launch the app & Add a New Connection
3. In the **Advanced tab** : `Boostrap servers : localhost:9092`

>  you can use [Conduktor](https://www.conduktor.io/) which is a beautiful and fully-featured desktop client for Apache Kafka
