package infra

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.ExecutionContextExecutor

object Main {

  // System constants
  final implicit val infraSystem: ActorSystem                        = ActorSystem("infra-system")
  final implicit val infraExecutionContext: ExecutionContextExecutor = infraSystem.dispatcher
  final val infraServer                                              = Http()
  final val infraLogger = Logging(infraSystem, "infra-logger")
  final val infraConfig = infraSystem.settings.config

  def main(args: Array[String]): Unit = {}
}
