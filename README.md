# Project

Simple project with Scala, Akka, Cassandra, Kafka and Circe. 

## Setup the environment

Generate the docker images:

    sbt docker:publishLocal

Set the deployment version (ex: 0.1.0-SNAPSHOT):

    export VERSION=0.1.0-SNAPSHOT

Launch the services:

    docker-compose up
