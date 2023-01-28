package broker_producer.schedulers

import akka.Done
import akka.actor.Cancellable
import broker_producer.Main.{
  brokerProducerConfig,
  brokerProducerExecutionContext,
  brokerProducerLogger,
  brokerProducerSystem
}
import broker_producer.producers.vehicles.VehicleBrokerProducer
import infra.providers.VehicleProvider

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class AppScheduler() {

  final val DATA_INGESTION_INITIAL_DELAY =
    brokerProducerConfig.getInt("broker_producer.scheduler.initial-delay-seconds").seconds;
  final val DATA_INGESTION_REFRESH_DELAY =
    brokerProducerConfig.getInt("broker_producer.scheduler.refresh-delay-seconds").seconds;

  // Producer
  final val brokerProducerProducer: VehicleBrokerProducer = new VehicleBrokerProducer()

  final val scheduler: Cancellable = {
    brokerProducerLogger.info(
      f"[AppScheduler]: Initializing tasks in producer with initialDelay=$DATA_INGESTION_INITIAL_DELAY s and refreshDelay=$DATA_INGESTION_REFRESH_DELAY s"
    )

    brokerProducerSystem.scheduler
      // Every 30 seconds, we call the external API to get the vehicles, and we produce dedicated kafka events
      .scheduleAtFixedRate(DATA_INGESTION_INITIAL_DELAY, DATA_INGESTION_REFRESH_DELAY) { () =>
        Future {}
          .andThen { _ =>
            brokerProducerLogger.info("[AppScheduler]: Starting data injection task in producer")
          }
          .flatMap(_ => produceApiVehiclesToBroker())
          .andThen { _ =>
            brokerProducerLogger.info("[AppScheduler]: Ending data injection task in producer")
          }
      }
  }

  def produceApiVehiclesToBroker(): Future[Done] =
    for {
      eitherErrorOrVehicles <- VehicleProvider.getVehicles()
      eitherErrorOrProduction <- eitherErrorOrVehicles match {
        case Right(vehicles) =>
          brokerProducerLogger.info(
            "[AppScheduler]: Successfully got vehicles from VehicleProvider"
          )
          brokerProducerProducer.produce(vehicles)
        case Left(error) =>
          Future {
            brokerProducerLogger.error(
              "[AppScheduler] Can not get vehicles from infra in producer!",
              error
            )
            Left(error)
          }
      }
    } yield {
      eitherErrorOrProduction match {
        case Right(_) =>
          brokerProducerLogger.info("[AppScheduler]: Successfully produced in topic")
          Done
        case Left(error) =>
          brokerProducerLogger.error("[AppScheduler] Can not produce vehicles in producer!", error)
          Done
      }
    }

  def terminate(): Unit = {
    scheduler.cancel()
    brokerProducerProducer.terminate()
  }

}
