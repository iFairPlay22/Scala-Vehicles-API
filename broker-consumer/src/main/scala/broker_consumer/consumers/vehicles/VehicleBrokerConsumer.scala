package broker_consumer.consumers.vehicles

import broker_consumer.consumers.BrokerConsumerImpl
import domain.data.vehicles.VehicleEntity
import akka.Done
import broker_consumer.Main.{
  brokerConsumerConfig,
  brokerConsumerExecutionContext,
  brokerConsumerLogger,
  brokerConsumerSystem
}
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import broker_consumer.throwables.BrokerConsumerException
import database.repositories.VehicleRepository

import scala.concurrent.Future

class VehicleBrokerConsumer()
    extends BrokerConsumerImpl[Long, List[VehicleEntity]](
      topic = brokerConsumerConfig.getString("broker_consumer.topic"),
      callbacks = Set {
        case Right(vehicles) =>
          for {
            eitherErrorOrInsertion <- VehicleRepository.insertOrEdit(vehicles._2)
          } yield {
            eitherErrorOrInsertion match {
              case Right(_) =>
                brokerConsumerLogger.info(
                  "[VehicleBrokerConsumer]: Vehicles table data successfully updated in consumer!"
                )
                Done
              case Left(error) =>
                brokerConsumerLogger.error(
                  "[VehicleBrokerConsumer]: Can not update vehicles table data in consumer!",
                  error
                )
                Done
            }
          }

        case Left(error) =>
          brokerConsumerLogger.error(
            "[VehicleBrokerConsumer]: Can not consume vehicles in consumer!",
            error
          )
          Future(Done)
      }
    ) {}
