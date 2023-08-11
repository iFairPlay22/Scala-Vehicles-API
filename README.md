# Vehicles

Simple project with Scala, Akka, Cassandra, Kafka and Circe.

## Setup the environment

### Nexus environment

    export NEXUS_BASE_URL=https://$NEXUS_HOST_URL

### Code Style

Format the code:

    sbt scalafmt

### Artifacts

Generate the artifacts locally:

    sbt publishLocal

Generate the artifacts images publicly:

    sbt publish

### Docker images

Generate the docker images locally:

    sbt docker:publishLocal

Generate the docker images publicly:

    sbt docker:publish

## Launch the app

### Using Docker

Generate the docker images locally:

    sbt docker:publishLocal

Set the deployment version (ex: 0.1.0-SNAPSHOT):

    export VERSION=0.1.0-SNAPSHOT

Launch the services:

    docker-compose up --detach

### Using Minikube

Launch minikube

    minikube start

Launch the services

    cd ./minikube
    kubectl apply -f vehicles-secret.yaml
    kubectl apply -f vehicles-config.yaml
    kubectl apply -f cassandra.yaml
    # Wait to be ready and then insert the data using cqlsh
    kubectl apply -f zookeeper.yaml
    # Wait to be ready
    kubectl apply -f broker.yaml
    # Wait to be ready and then insert the topic
    kubectl apply -f vehicles-api.yaml
    kubectl apply -f vehicles-broker-producer.yaml
    kubectl apply -f vehicles-broker-consumer.yaml

NOTES: Use cqlsh -u cassandra -p cassandra to run CQL queries


## Interacting with the sample

### Postman

You can test the endpoints by using the postman collection `api.postman_collection.json`. You can directly import it into Postman.

### Endpoints

If launched with minikube, you have to run `minikube service vehicles-api-service --url` to get the url of the service that you should trigger. Otherwise, it is `localhost`.

Get all available vehicles:

    curl --location --request GET 'http://127.0.0.1:8080/api/vehicles'
