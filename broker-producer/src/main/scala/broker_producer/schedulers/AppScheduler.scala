package broker_producer.schedulers

import akka.Done
import akka.actor.Cancellable
import broker_producer.Main.{
  brokerProducerConfig,
  brokerProducerExecutionContext,
  brokerProducerLogger,
  brokerProducerProducer,
  brokerProducerSystem
}
import infra.providers.VehicleProvider

import scala.concurrent.Future
import scala.concurrent.duration.DurationInt

class AppScheduler() {

  final val DATA_INGESTION_INITIAL_DELAY =
    brokerProducerConfig.getInt("broker_producer.scheduler.initial-delay-seconds").seconds;
  final val DATA_INGESTION_REFRESH_DELAY =
    brokerProducerConfig.getInt("broker_producer.scheduler.refresh-delay-seconds").seconds;

  def run(): Cancellable = {
    brokerProducerLogger.info("[AppScheduler]: Initializing tasks in producer")

    brokerProducerSystem.scheduler
      // Every 30 seconds, we call the external API to get the vehicles, and we produce dedicated kafka events
      .scheduleAtFixedRate(DATA_INGESTION_INITIAL_DELAY, DATA_INGESTION_REFRESH_DELAY) { () =>
        Future {}
          .map { _ =>
            brokerProducerLogger.info("[AppScheduler]: ------------------------------------------")
            brokerProducerLogger.info("[AppScheduler]: Starting data injection task in producer")
            brokerProducerLogger.info("[AppScheduler]: -----------------------------------------")
          }
          .flatMap(_ => produceApiVehiclesToBroker())
          .map { _ =>
            brokerProducerLogger.info("[AppScheduler]: ------------------------------------------")
            brokerProducerLogger.info("[AppScheduler]: Ending data injection task in producer")
            brokerProducerLogger.info("[AppScheduler]: ------------------------------------------")
          }
      }
  }

  def produceApiVehiclesToBroker(): Future[Done] =
    for {
      eitherErrorOrVehicles <- VehicleProvider.getVehicles()
      eitherErrorOrProduction <- eitherErrorOrVehicles match {
        case Right(vehicles) => brokerProducerProducer.produce(vehicles)
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
        case Right(_) => Done
        case Left(error) =>
          brokerProducerLogger.error("[AppScheduler] Can not produce vehicles in producer!", error)
          Done
      }
    }
}
