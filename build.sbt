// Projects
lazy val global = (project in file("."))
  .settings(
    name := "vehicles-api-root",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= globalLibraryDependencies,
    publish / skip := true
  )
  .aggregate(domain, infra, brokerConsumer, brokerProducer, database, api)
  .dependsOn(domain, infra, brokerConsumer, brokerProducer, database, api)

lazy val api = (project in file("api"))
  .enablePlugins(DockerPlugin)
  .settings(
    name := "api",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= apiLibraryDependencies,
    Compile / run / mainClass := Some("api.Main"),
    dockerExposedPorts ++= Seq(8080)
  )
  .dependsOn(domain, database)

lazy val brokerConsumer = (project in file("broker-consumer"))
  .enablePlugins(DockerPlugin)
  .settings(
    name := "broker-consumer",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= brokerConsumerLibraryDependencies,
    Compile / run / mainClass := Some("broker_consumer.Main"),
    dockerExposedPorts ++= Seq(8081)
  )
  .dependsOn(domain, database)

lazy val brokerProducer = (project in file("broker-producer"))
  .enablePlugins(DockerPlugin)
  .settings(
    name := "broker-producer",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= brokerProducerLibraryDependencies,
    Compile / run / mainClass := Some("broker_producer.Main"),
    dockerExposedPorts ++= Seq(8082)
  )
  .dependsOn(domain, infra)

lazy val database = (project in file("database"))
  .enablePlugins(DockerPlugin)
  .settings(
    name := "database",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= databaseLibraryDependencies,
    Compile / run / mainClass := Some("database.Main"),
    dockerExposedPorts ++= Seq(8083)
  )
  .dependsOn(domain)

lazy val domain = (project in file("domain"))
  .enablePlugins(DockerPlugin)
  .settings(
    name := "domain",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= domainLibraryDependencies,
    Compile / run / mainClass := Some("domain.Main"),
    dockerExposedPorts ++= Seq(8084)
  )
  .dependsOn()

lazy val infra = (project in file("infra"))
  .enablePlugins(DockerPlugin)
  .settings(
    name := "infra",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= infraLibraryDependencies,
    Compile / run / mainClass := Some("infra.Main"),
    dockerExposedPorts ++= Seq(8085)
  )
  .dependsOn(domain)

// Library dependencies
lazy val projectLibraryDependencies =
  new {
    val scala = new {
      val scalaVersion        = "2.13.4"
      val scalaLoggingVersion = "3.9.5"
      val scalaTestVersion    = "3.2.15"

      val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
      val scalaTest    = "org.scalatest"              %% "scalatest"     % scalaTestVersion % Test

      val all = Seq(
        scalaLogging,
        scalaTest
      )
    }

    val java = new {
      val javaDriverCoreVersion = "4.15.0"

      val javaDriverCore = "com.datastax.oss" % "java-driver-core" % "4.15.0"

      val all = Seq(
        javaDriverCore
      )
    }

    val logback = new {
      val logbackClassicVersion = "1.4.5"

      val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackClassicVersion % Runtime

      val all = Seq(
        logbackClassic
      )
    }

    val circe = new {
      val circeVersion = "0.14.3"

      val circeCore    = "io.circe" %% "circe-core"    % circeVersion
      val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
      val circeParser  = "io.circe" %% "circe-parser"  % circeVersion

      val all = Seq(
        circeCore,
        circeGeneric,
        circeParser
      )
    }

    val akkaHttp = new {

      val akkaHttpVersion      = "10.4.0"
      val akkaHttpCirceVersion = "1.39.2"
      val akkaTestKitVersion   = "2.7.0"

      val akkaHttp        = "com.typesafe.akka" %% "akka-http"         % akkaHttpVersion
      val akkaTestKit     = "com.typesafe.akka" %% "akka-testkit"      % akkaTestKitVersion % Test
      val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion    % Test
      val akkaCirceHttp   = "de.heikoseeberger" %% "akka-http-circe"   % akkaHttpCirceVersion

      val all = Seq(
        akkaHttp,
        akkaTestKit,
        akkaHttpTestkit,
        akkaCirceHttp
      )
    }

    val akkaStream = new {

      val akkaStreamVersion      = "2.7.0"
      val akkaStreamKafkaVersion = "4.0.0"

      val akkaStream      = "com.typesafe.akka" %% "akka-stream"       % akkaStreamVersion
      val akkaStreamKafka = "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamKafkaVersion

      val all = Seq(
        akkaStream,
        akkaStreamKafka
      )
    }

    val akkaCassandra = new {
      val akkaCassandraVersion = "1.1.0"

      val akkaPersistenceCassandra =
        "com.typesafe.akka" %% "akka-persistence-cassandra" % akkaCassandraVersion

      val all = Seq(akkaPersistenceCassandra)
    }

    val kafka = new {
      val kafkaVersion = "3.3.2"

      val kafkaClients      = "org.apache.kafka"  % "kafka-clients"       % kafkaVersion
      val kafkaStreams      = "org.apache.kafka"  % "kafka-streams"       % kafkaVersion
      val kafkaStreamsScala = "org.apache.kafka" %% "kafka-streams-scala" % kafkaVersion

      val all = Seq(kafkaClients, kafkaStreams, kafkaStreamsScala)
    }
  }

lazy val commonLibraryDependencies =
  projectLibraryDependencies.scala.all ++
    projectLibraryDependencies.java.all ++
    projectLibraryDependencies.logback.all ++
    projectLibraryDependencies.akkaHttp.all ++
    projectLibraryDependencies.akkaStream.all

lazy val globalLibraryDependencies =
  commonLibraryDependencies

lazy val apiLibraryDependencies =
  commonLibraryDependencies ++
    databaseLibraryDependencies ++
    projectLibraryDependencies.circe.all

lazy val brokerConsumerLibraryDependencies =
  commonLibraryDependencies ++
    databaseLibraryDependencies ++
    projectLibraryDependencies.circe.all ++
    projectLibraryDependencies.kafka.all

lazy val brokerProducerLibraryDependencies =
  commonLibraryDependencies ++
    infraLibraryDependencies ++
    projectLibraryDependencies.circe.all ++
    projectLibraryDependencies.kafka.all

lazy val databaseLibraryDependencies =
  commonLibraryDependencies ++
    projectLibraryDependencies.akkaCassandra.all

lazy val domainLibraryDependencies =
  commonLibraryDependencies

lazy val infraLibraryDependencies =
  commonLibraryDependencies ++
    projectLibraryDependencies.circe.all
