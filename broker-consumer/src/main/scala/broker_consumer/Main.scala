package broker_consumer
import akka.Done
import akka.actor.ActorSystem
import akka.event.Logging
import broker_consumer.consumers.vehicles.VehicleBrokerConsumer
import com.typesafe.config.Config

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, ExecutionContextExecutor, Future }
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
  database.Main.init()

  def terminate(): Future[Done] =
    consumer.terminate().andThen(_ => database.Main.terminate())

  def main(args: Array[String]): Unit = {
    StdIn.readLine()
    Await.ready(
      terminate(),
      Duration.Inf
    )
  }
}
