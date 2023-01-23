package api_test.config

import akka.Done
import database.repositories.VehicleRepository
import database.throwables.DatabaseException
import domain.vehicles.VehicleEntity
import org.scalatest.time.SpanSugar.convertIntToGrainOfTime
import api.Main._

import scala.concurrent.duration.Duration
import scala.concurrent.{ Await, Future }

trait SpecsConfig {

  def setupAndStartEnvironment(): Unit = {
    apiLogger.info("Starting environment")
    Await.ready(api.Main.init(), Duration.Inf)
    Thread.sleep(5.seconds.toMillis)
  }

  def resetVehicleEnvironment(): Future[Done] =
    VehicleRepository
      .truncateTable()
      .map {
        case Right(_) => apiLogger.info("Vehicle table was successfully truncated!")
        case Left(err) => apiLogger.error("Error when truncating vehicle table!", err)
      }
      .map(_ => Done)

  def addVehiclesInEnvironment(
      vehicles: VehicleEntity*
  ): Future[Done] =
    VehicleRepository
      .insertOrEdit(vehicles)
      .map {
        case Right(_) => apiLogger.info("Vehicles successfully inserted in table!")
        case Left(err) => apiLogger.error("Error when inserting vehicles in table!", err)
      }
      .map(_ => Done)

  def stopEnvironment(): Unit = {
    apiLogger.info(f"Stopping environment")
    Await.ready(api.Main.terminate(), Duration.Inf)
    Thread.sleep(5.seconds.toMillis)
  }
}
