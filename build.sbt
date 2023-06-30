// Projects
lazy val global = (project in file("."))
  .settings(defaultSettings)
  .settings(
    name := "vehicles-root",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= commonsLibraryDependencies,
    Test / parallelExecution := false,
    publish / skip := true)
  .aggregate(domain, brokerConsumer, brokerProducer, cassandra, api)
  .dependsOn(domain, brokerConsumer, brokerProducer, cassandra, api)

lazy val api = (project in file("api"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(defaultSettings)
  .settings(dockerSettings)
  .settings(
    name := "vehicles-api",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= apiLibraryDependencies,
    Compile / run / mainClass := Some("api.Main"),
    Docker / packageName := "vehicles-api")
  .dependsOn(domain, cassandra)

lazy val brokerConsumer = (project in file("broker-consumer"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(defaultSettings)
  .settings(dockerSettings)
  .settings(
    name := "vehicles-broker-consumer",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= brokerConsumerLibraryDependencies,
    Compile / run / mainClass := Some("broker_consumer.Main"),
    Docker / packageName := "vehicles-broker-consumer")
  .dependsOn(domain, cassandra)

lazy val brokerProducer = (project in file("broker-producer"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(defaultSettings)
  .settings(dockerSettings)
  .settings(
    name := "vehicles-broker-producer",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= brokerProducerLibraryDependencies,
    Compile / run / mainClass := Some("broker_producer.Main"),
    Docker / packageName := "vehicles-broker-producer")
  .dependsOn(domain)

lazy val cassandra = (project in file("cassandra"))
  .settings(defaultSettings)
  .settings(
    name := "vehicles-cassandra",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= cassandraLibraryDependencies,
    Compile / run / mainClass := Some("cassandra.Main"))
  .dependsOn(domain)

lazy val domain = (project in file("domain"))
  .settings(defaultSettings)
  .settings(
    name := "vehicles-domain",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= domainLibraryDependencies)
  .dependsOn()

// Default settings
lazy val defaultSettings = Seq(
  organization := "ewenbouquet",
  publishTo := {
    val nexus =
      sys.env.getOrElse("NEXUS_BASE_URL", "https://ewenbouquet-nexus-public-url.loca.lt")
    if (isSnapshot.value)
      Some("snapshots" at nexus + "/repository/maven-snapshots/")
    else
      Some("releases" at nexus + "/repository/maven-releases/")
  },
  credentials += Credentials(Path.userHome / ".sbt" / ".ewenbouquet_credentials"))

// Docker plugin settings
lazy val dockerSettings =
  Seq(dockerUsername := Some("ewenbouquet"), dockerBaseImage := "openjdk:11")

// Library dependencies
lazy val projectLibraryDependencies =
  new {

    val scala = new {
      val scalaVersion = "2.13.10"
    }

    val ewenbouquet = new {
      val commonsVersion = "0.1.0-SNAPSHOT"

      val commonsBase = "ewenbouquet" %% "commons-commons-libs" % commonsVersion
      val commonsBroker = "ewenbouquet" %% "commons-broker-libs" % commonsVersion
      val commonsCassandra = "ewenbouquet" %% "commons-cassandra-libs" % commonsVersion
      val commonsHttp = "ewenbouquet" %% "commons-http-libs" % commonsVersion
      val commonsScheduler = "ewenbouquet" %% "commons-scheduler-libs" % commonsVersion
    }

  }

lazy val commonsLibraryDependencies =
  Seq()

lazy val apiLibraryDependencies =
  commonsLibraryDependencies ++
    Seq(projectLibraryDependencies.ewenbouquet.commonsHttp)

lazy val brokerConsumerLibraryDependencies =
  commonsLibraryDependencies ++
    Seq(projectLibraryDependencies.ewenbouquet.commonsBroker)

lazy val brokerProducerLibraryDependencies =
  commonsLibraryDependencies ++
    Seq(projectLibraryDependencies.ewenbouquet.commonsBroker) ++
    Seq(projectLibraryDependencies.ewenbouquet.commonsScheduler)

lazy val cassandraLibraryDependencies =
  commonsLibraryDependencies ++
    Seq(projectLibraryDependencies.ewenbouquet.commonsCassandra)

lazy val domainLibraryDependencies =
  commonsLibraryDependencies ++
    Seq(projectLibraryDependencies.ewenbouquet.commonsBase)
