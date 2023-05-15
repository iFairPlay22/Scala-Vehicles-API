package api.vehicles.service

import akka.actor.ActorSystem
import akka.http.scaladsl.model.DateTime
import akka.stream.alpakka.cassandra.scaladsl.CassandraSession
import api.vehicles.dto.GetVehiclesDTO
import api.vehicles.mapper.VehicleResponseMapper
import commons.system.http._HttpServiceSystem
import database.vehicles.repositories.VehicleRepository

import java.time.LocalDate
import scala.concurrent.Future

class VehicleService(implicit val system: ActorSystem, implicit val session: CassandraSession)
    extends _HttpServiceSystem {

  private val vehicleRepository = new VehicleRepository()

  def getAllVehicles(): Future[GetVehiclesDTO] =
    vehicleRepository
      .selectAllByDate(LocalDate.now)
      .map(VehicleResponseMapper.map)

}
