// Projects
lazy val global = (project in file("."))
  .settings(
    name := "vehicles-project-root",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= globalLibraryDependencies,
    Test / parallelExecution := false,
    publish / skip := true)
  .aggregate(commons, domain, brokerConsumer, brokerProducer, database, api)
  .dependsOn(commons, domain, brokerConsumer, brokerProducer, database, api)

lazy val commons = (project in file("commons"))
  .settings(
    name := "vehicles-project-commons",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= commonsLibraryDependencies)

lazy val api = (project in file("api"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(dockerSettings: _*)
  .settings(
    name := "vehicles-api",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= apiLibraryDependencies,
    Compile / run / mainClass := Some("api.Main"),
    Docker / packageName := "vehicles-api")
  .dependsOn(commons, domain, database)

lazy val brokerConsumer = (project in file("broker-consumer"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(dockerSettings: _*)
  .settings(
    name := "vehicles-broker-consumer",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= brokerConsumerLibraryDependencies,
    Compile / run / mainClass := Some("broker_consumer.Main"),
    Docker / packageName := "vehicles-broker-consumer")
  .dependsOn(commons, domain, database)

lazy val brokerProducer = (project in file("broker-producer"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(dockerSettings: _*)
  .settings(
    name := "vehicles-broker-producer",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= brokerProducerLibraryDependencies,
    Compile / run / mainClass := Some("broker_producer.Main"),
    Docker / packageName := "vehicles-broker-producer")
  .dependsOn(commons, domain)

lazy val database = (project in file("database"))
  .settings(
    name := "vehicles-database",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= databaseLibraryDependencies,
    Compile / run / mainClass := Some("database.Main"))
  .dependsOn(commons, domain)

lazy val domain = (project in file("domain"))
  .settings(
    name := "vehicles-domain",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= domainLibraryDependencies)
  .dependsOn(commons)

// Docker plugin settings
lazy val dockerSettings = Seq(dockerBaseImage := "openjdk:11", dockerUsername := Some("ebouquet"))

// Library dependencies
lazy val projectLibraryDependencies =
  new {
    val scala = new {
      val scalaVersion = "2.13.10"
      val scalaLoggingVersion = "3.9.5"
      val scalaTestVersion = "3.2.15"

      val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
      val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion

      val all = Seq(scalaLogging, scalaTest)
    }

    val java = new {
      val javaVersion = "11.0.15"
    }

    val logback = new {
      val logbackClassicVersion = "1.4.5"

      val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackClassicVersion % Runtime

      val all = Seq(logbackClassic)
    }

    val circe = new {
      val circeVersion = "0.14.3"

      val circeCore = "io.circe" %% "circe-core" % circeVersion
      val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
      val circeParser = "io.circe" %% "circe-parser" % circeVersion

      val all = Seq(circeCore, circeGeneric, circeParser)
    }

    val akkaHttp = new {

      val akkaHttpVersion = "10.4.0"
      val akkaHttpCirceVersion = "1.39.2"
      val akkaTestKitVersion = "2.7.0"

      val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
      val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaTestKitVersion
      val akkaHttpTestkit = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion
      val akkaCirceHttp = "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion

      val all = Seq(akkaHttp, akkaTestKit, akkaHttpTestkit, akkaCirceHttp)
    }

    val akkaStream = new {

      val akkaStreamVersion = "2.7.0"
      val akkaStreamKafkaVersion = "4.0.0"

      val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaStreamVersion
      val akkaStreamKafka = "com.typesafe.akka" %% "akka-stream-kafka" % akkaStreamKafkaVersion

      val all = Seq(akkaStream, akkaStreamKafka)
    }

    val akkaCassandra = new {
      val akkaStreamAlpakkaCassandraVersion = "5.0.0"
      val esriGeometryApiVersion = "2.2.4"
      val tinkerpopTinkerGraphGremlinVersion = "3.6.2"

      val akkaStreamAlpakkaCassandra =
        "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % akkaStreamAlpakkaCassandraVersion
      val esriGeometryApi = "com.esri.geometry" % "esri-geometry-api" % esriGeometryApiVersion
      val tinkerpopTinkerGraphGremlin =
        "org.apache.tinkerpop" % "tinkergraph-gremlin" % tinkerpopTinkerGraphGremlinVersion

      val all = Seq(akkaStreamAlpakkaCassandra, esriGeometryApi, tinkerpopTinkerGraphGremlin)
    }

    val kafka = new {
      val kafkaVersion = "3.3.2"

      val kafkaClients = "org.apache.kafka" % "kafka-clients" % kafkaVersion
      val kafkaStreams = "org.apache.kafka" % "kafka-streams" % kafkaVersion
      val kafkaStreamsScala = "org.apache.kafka" %% "kafka-streams-scala" % kafkaVersion

      val all = Seq(kafkaClients, kafkaStreams, kafkaStreamsScala)
    }
  }

lazy val commonsLibraryDependencies =
  projectLibraryDependencies.scala.all ++
    projectLibraryDependencies.logback.all ++
    projectLibraryDependencies.akkaHttp.all ++
    projectLibraryDependencies.akkaStream.all ++
    projectLibraryDependencies.akkaCassandra.all ++
    projectLibraryDependencies.circe.all ++
    projectLibraryDependencies.kafka.all

lazy val globalLibraryDependencies =
  commonsLibraryDependencies

lazy val apiLibraryDependencies =
  commonsLibraryDependencies

lazy val brokerConsumerLibraryDependencies =
  commonsLibraryDependencies

lazy val brokerProducerLibraryDependencies =
  commonsLibraryDependencies

lazy val databaseLibraryDependencies =
  commonsLibraryDependencies

lazy val domainLibraryDependencies =
  commonsLibraryDependencies
