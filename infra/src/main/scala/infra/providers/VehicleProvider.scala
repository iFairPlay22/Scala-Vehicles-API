package infra.providers

import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import io.circe._
import akka.http.scaladsl.common.{ EntityStreamingSupport, JsonEntityStreamingSupport }
import akka.http.scaladsl.model.{ HttpRequest, HttpResponse }
import akka.http.scaladsl.unmarshalling.Unmarshal
import domain.vehicles.VehicleEntity
import infra.requests.vehicles.VehicleDictRequest
import infra.Main.{ infraConfig, infraExecutionContext, infraLogger, infraServer, infraSystem }
import infra.mappers.VehicleRequestMapper
import infra.throwables.{
  InfraException,
  UnableToGetVehiclesDataFromInfraException,
  UnableToLaunchApiRequestFromInfraException,
  UnableToParseVehiclesDataFromIntraException
}

import scala.concurrent.Future

object VehicleProvider extends Providers {

  final implicit val jsonStreamingSupport: JsonEntityStreamingSupport = EntityStreamingSupport.json()

  final val VEHICLES_API_HOST: String  = infraConfig.getString("infra.vehicles-api.host")
  final val VEHICLES_API_PORT: String  = infraConfig.getString("infra.vehicles-api.port")
  final val VEHICLES_API_ROUTE: String = infraConfig.getString("infra.vehicles-api.route")
  final val VEHICLE_API_URL: String    = f"$VEHICLES_API_HOST:$VEHICLES_API_PORT$VEHICLES_API_ROUTE"

  def getVehicles(): Future[Either[InfraException, List[VehicleEntity]]] = {

    infraLogger.info(f"[VehicleProvider] Calling API $VEHICLE_API_URL")

    for {
      eitherExceptionOrReq: Either[InfraException, HttpResponse] <- {
        infraServer
          .singleRequest(HttpRequest(uri = VEHICLE_API_URL))
          .map(req => Right(req))
          .recover(_ => Left(new UnableToLaunchApiRequestFromInfraException()))
      }
      eitherExceptionOrJson: Either[InfraException, String] <- {
        eitherExceptionOrReq match {
          case Right(req) => {
            if (req.status.isSuccess())
              Unmarshal(req.entity)
                .to[String]
                .map(json => Right(json))
            else
              Future { Left(new UnableToGetVehiclesDataFromInfraException()) }
          }
          case Left(error) => Future { Left(error) }
        }
      }
    } yield {

      eitherExceptionOrJson match {
        case Right(json) => {
          val eitherErrorOrVehicles = decode[VehicleDictRequest](json)
          eitherErrorOrVehicles match {
            case Right(vehiclesDict) => Right(vehiclesDict.vehicles.map(VehicleRequestMapper.inputToEntity))
            case Left(error)         => Left(new UnableToParseVehiclesDataFromIntraException())
          }
        }
        case Left(error) => Left(error)
      }
    }

  }

}
