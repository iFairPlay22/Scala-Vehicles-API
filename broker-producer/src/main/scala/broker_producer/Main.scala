package broker_producer

import akka.Done
import akka.actor.ActorSystem
import akka.event.Logging
import broker_producer.producers.vehicles.VehicleBrokerProducer
import broker_producer.schedulers.AppScheduler
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.{ Await, ExecutionContextExecutor, Future }
import scala.concurrent.duration.Duration
import scala.io.StdIn

object Main {

  // System constants
  final implicit val brokerProducerSystem: ActorSystem = ActorSystem("broker-producer-system")
  final implicit val brokerProducerExecutionContext: ExecutionContextExecutor =
    brokerProducerSystem.dispatcher
  final val brokerProducerLogger         = Logging(brokerProducerSystem, "broker-producer-logger")
  final val brokerProducerConfig: Config = brokerProducerSystem.settings.config

  // Scheduler
  final val brokerProducerScheduler: AppScheduler = new AppScheduler()

  def terminate(): Future[Done] =
    Future(Done)
      .andThen(_ => brokerProducerLogger.info("Calling broker_producer.Main.terminate()"))
      .andThen(_ => brokerProducerScheduler.terminate())

  def main(args: Array[String]): Unit = {
    /*
    StdIn.readLine()
    Await.ready(
      terminate(),
      Duration.Inf
    )
     */
  }
}
