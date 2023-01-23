logLevel := Level.Warn

//dependency resolver. parallel downloads
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.1.0-M11")

//shows available updates. dependencyUpdates || dependencyUpdatesReport
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.4.0")

//create one jar for application.
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")

// for autoplugins
addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.4")
