// Projects
lazy val global = (project in file("."))
  .settings(defaultSettings)
  .settings(
    name := "vehicles-project-root",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= commonsLibraryDependencies,
    Test / parallelExecution := false,
    publish / skip := true)
  .aggregate(domain, brokerConsumer, brokerProducer, database, api)
  .dependsOn(domain, brokerConsumer, brokerProducer, database, api)

lazy val api = (project in file("api"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(defaultSettings)
  .settings(dockerSettings)
  .settings(
    name := "vehicles-api",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= commonsLibraryDependencies,
    Compile / run / mainClass := Some("api.Main"),
    Docker / packageName := "vehicles-api")
  .dependsOn(domain, database)

lazy val brokerConsumer = (project in file("broker-consumer"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(defaultSettings)
  .settings(dockerSettings)
  .settings(
    name := "vehicles-broker-consumer",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= commonsLibraryDependencies,
    Compile / run / mainClass := Some("broker_consumer.Main"),
    Docker / packageName := "vehicles-broker-consumer")
  .dependsOn(domain, database)

lazy val brokerProducer = (project in file("broker-producer"))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .settings(defaultSettings)
  .settings(dockerSettings)
  .settings(
    name := "vehicles-broker-producer",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= commonsLibraryDependencies,
    Compile / run / mainClass := Some("broker_producer.Main"),
    Docker / packageName := "vehicles-broker-producer")
  .dependsOn(domain)

lazy val database = (project in file("database"))
  .settings(defaultSettings)
  .settings(
    name := "vehicles-database",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= commonsLibraryDependencies,
    Compile / run / mainClass := Some("database.Main"))
  .dependsOn(domain)

lazy val domain = (project in file("domain"))
  .settings(defaultSettings)
  .settings(
    name := "vehicles-domain",
    scalaVersion := projectLibraryDependencies.scala.scalaVersion,
    libraryDependencies ++= commonsLibraryDependencies)
  .dependsOn()

// Default settings
lazy val defaultSettings = Seq(organization := "ebouquet")

// Docker plugin settings
lazy val dockerSettings = Seq(dockerBaseImage := "openjdk:11", dockerUsername := Some("ebouquet"))

// Library dependencies
lazy val projectLibraryDependencies =
  new {

    val scala = new {
      val scalaVersion = "2.13.10"
    }

    val ebouquet = new {
      val commonsVersion = "0.1.0-SNAPSHOT"

      val commons = "ebouquet" %% "commons-project-commons" % commonsVersion

      val all = Seq(commons)
    }

  }

lazy val commonsLibraryDependencies =
  projectLibraryDependencies.ebouquet.all
