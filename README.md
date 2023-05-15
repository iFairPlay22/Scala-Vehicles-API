# Vehicles API

Simple project with Scala, Akka, Cassandra, Kafka and Circe.

## Setup the environment

### Code Style

Format the code:

    sbt scalafmt

### Artifacts

Generate the artifacts locally:

    sbt publishLocal

Generate the artifacts images publicly:

    export JENKINS_BASE_URL=https://$JENKINS_HOST_URL
    sbt publish

### Docker images

Generate the docker images locally:

    sbt docker:publishLocal

Generate the docker images publicly:

    sbt docker:publish

### Launch the app

Set the deployment version (ex: 0.1.0-SNAPSHOT):

    export VERSION=0.1.0-SNAPSHOT

Generate the docker images locally:

    sbt docker:publishLocal

Launch the services:

    docker-compose up
