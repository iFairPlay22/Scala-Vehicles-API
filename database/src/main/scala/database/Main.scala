package database

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.alpakka.cassandra.CassandraSessionSettings
import akka.stream.alpakka.cassandra.scaladsl.{ CassandraSession, CassandraSessionRegistry }
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main {

  // System constants
  final implicit val databaseSystem: ActorSystem = ActorSystem("database-system")
  final implicit val databaseExecutionContext: ExecutionContextExecutor = databaseSystem.dispatcher
  final val databaseLogger         = Logging(databaseSystem, "database-logger")
  final val databaseConfig: Config = databaseSystem.settings.config

  // Database
  final implicit val databaseCassandraSession: CassandraSession =
    CassandraSessionRegistry.get(databaseSystem).sessionFor(CassandraSessionSettings())

  def init(): Unit = {}

  def terminate(): Unit =
    databaseCassandraSession.close(databaseExecutionContext)

  def main(args: Array[String]): Unit = {}
}
