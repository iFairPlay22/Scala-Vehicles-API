package domain

import akka.actor.ActorSystem
import akka.event.Logging

import scala.concurrent.ExecutionContextExecutor

object Main {
  // System constants
  final implicit val domainSystem: ActorSystem                        = ActorSystem("domain-system")
  final implicit val domainExecutionContext: ExecutionContextExecutor = domainSystem.dispatcher
  final val domainLogger = Logging(domainSystem, "domain-logger")
  final val domainConfig = domainSystem.settings.config

  def main(args: Array[String]): Unit = {}
}
