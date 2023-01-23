package api.controllers

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.http.scaladsl.server.Route
import api.services.vehicles.VehicleService

object VehicleController extends Controller {

  override def routes: Route =
    pathPrefix("api") {
      pathPrefix("vehicles") {
        path("list") {
          get {
            complete(
              toJson(
                VehicleService
                  .getAvailableVehiclesResponse()
              )
            )
          }
        } ~
        path("vehicle" / IntNumber / "lastPosition") { vehicle_id =>
          get {
            complete(
              toJson(
                VehicleService.getLastPositionOfVehicleResponse(vehicle_id)
              )
            )
          }
        }
      } ~
      pathPrefix("tiles") {
        path("filled") {
          get {
            complete(
              toJson(
                VehicleService.getFilledTilesResponse()
              )
            )
          }
        } ~
        path("tile" / IntNumber ~ "_" ~ IntNumber / "availableVehicles") { (tileX, tileY) =>
          get {
            complete(
              toJson(
                VehicleService.getAvailableVehiclesInTileResponse(tileX, tileY)
              )
            )
          }
        } ~
        path("usecase" / "vehicleCount") {
          get {
            complete(
              toJson(VehicleService.countAvailableVehiclesResponse())
            )
          }
        }
      }
    }

}
