package broker_producer

import akka.actor.ActorSystem
import akka.event.Logging
import broker_producer.producers.vehicles.VehicleBrokerProducer
import broker_producer.schedulers.AppScheduler
import com.typesafe.config.{ Config, ConfigFactory }

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Main {

  // System constants
  final implicit val brokerProducerSystem: ActorSystem = ActorSystem("broker-producer-system")
  final implicit val brokerProducerExecutionContext: ExecutionContextExecutor =
    brokerProducerSystem.dispatcher
  final val brokerProducerLogger         = Logging(brokerProducerSystem, "broker-producer-logger")
  final val brokerProducerConfig: Config = brokerProducerSystem.settings.config

  // Producer
  final val brokerProducerProducer: VehicleBrokerProducer = new VehicleBrokerProducer()

  // Scheduler
  final val brokerProducerScheduler: AppScheduler = new AppScheduler()

  def main(args: Array[String]): Unit = {
    StdIn.readLine()
    brokerProducerProducer.terminate()
  }
}
