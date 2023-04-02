package api.vehicles.controller

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.alpakka.cassandra.scaladsl.CassandraSession
import api.vehicles.service.VehicleService
import commons.system.http._HttpControllerSystem
import io.circe.generic.auto._

class VehicleController(implicit val system: ActorSystem, implicit val session: CassandraSession)
    extends _HttpControllerSystem {

  private val vehicleService = new VehicleService()

  override val routes: Route =
    path("vehicles") {
      get {
        response { () =>
          vehicleService
            .getAllVehicles()
        }
      }
    }
}
