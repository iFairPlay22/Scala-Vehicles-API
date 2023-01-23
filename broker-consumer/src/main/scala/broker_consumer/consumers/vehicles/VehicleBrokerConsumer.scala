package broker_consumer.consumers.vehicles

import broker_consumer.consumers.BrokerConsumerImpl
import domain.vehicles.VehicleEntity
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

class VehicleBrokerConsumer() extends BrokerConsumerImpl[Long, List[VehicleEntity]] {

  override val topic: String = brokerConsumerConfig.getString("broker_consumer.topic")
  override val callbacks: Set[Either[BrokerConsumerException, (Long, List[VehicleEntity])] => Future[Done]] = Set(
    {
      case Right(vehicles) =>
        VehicleRepository
          .insertOrEdit(vehicles._2)
          .map({
            case Right(_) =>
              brokerConsumerLogger.info(
                "[VehicleBrokerConsumer]: Vehicles table data successfully updated in consumer!")
              Done
            case Left(error) =>
              brokerConsumerLogger.error("[VehicleBrokerConsumer]: Can not update vehicles table data in consumer!")
              Done
          })

      case Left(error) =>
        brokerConsumerLogger.error("[VehicleBrokerConsumer]: Can not consume vehicles in consumer!", error)
        Future { Done }
    }
  )
}
