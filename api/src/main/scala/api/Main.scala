package api

import akka.http.scaladsl.server.Route
import api.vehicles.controller.VehicleController
import commons.system.actor._ActorSystem
import commons.system.database._CassandraSystem
import commons.system.http._HttpServerSystem

object Main extends _ActorSystem with _CassandraSystem with _HttpServerSystem {

  override def routes: Route = Route.seal(new VehicleController().routes)

  def main(args: Array[String]): Unit = {
    startServer()
  }

}
