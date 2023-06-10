package broker_consumer.vehicles.consumers

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.model.DateTime
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import com.typesafe.scalalogging.Logger
import commons.system.broker._BrokerConsumerSystem
import commons.system.database._CassandraSystem
import database.vehicles.repositories.VehicleRepository
import domain.vehicles.VehicleDomain

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class VehicleBrokerConsumer(implicit val system: ActorSystem)
    extends _BrokerConsumerSystem[DateTime, VehicleDomain]
    with _CassandraSystem {

  private val logger: Logger = Logger(getClass)

  private val vehicleRepository = new VehicleRepository()

  override val callbacks: Set[(DateTime, VehicleDomain) => Future[Done]] = Set((_, vehicle) => {
    logger.info("Processing a consumed vehicle")
    vehicleRepository
      .insertOrEdit(vehicle)
      .andThen(_ => logger.info("Vehicle inserted in cassandra!"))
  })

  override def startBrokerConsumer(): Future[Done] = {
    super.startBrokerConsumer()
  }

  override def stopBrokerConsumer(): Future[Done] =
    super
      .stopBrokerConsumer()
      .andThen(_ => stopCassandra())
}
