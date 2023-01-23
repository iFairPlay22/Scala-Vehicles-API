package broker_consumer

import akka.actor.ActorSystem
import akka.event.Logging
import broker_consumer.consumers.vehicles.VehicleBrokerConsumer
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main {

  // System constants
  final implicit val brokerConsumerSystem: ActorSystem = ActorSystem("broker-consumer-system")
  final implicit val brokerConsumerExecutionContext: ExecutionContextExecutor =
    brokerConsumerSystem.dispatcher
  final val brokerConsumerLogger         = Logging(brokerConsumerSystem, "broker-consumer-logger")
  final val brokerConsumerConfig: Config = brokerConsumerSystem.settings.config

  // Consumer
  final val consumer: VehicleBrokerConsumer = new VehicleBrokerConsumer()

  // Database
  database.Main.initDatabase()

  def terminate(): Unit = {
    consumer.terminate()
    database.Main.terminate()
  }

  def main(args: Array[String]): Unit = {}
}
