package api.vehicles.service

import akka.actor.ActorSystem
import akka.stream.alpakka.cassandra.scaladsl.CassandraSession
import api.vehicles.dto.GetVehiclesDTO
import api.vehicles.mapper.VehicleResponseMapper
import cassandra.vehicles.repositories.VehicleRepository
import http._HttpServiceSystem

import java.time.LocalDate
import scala.concurrent.Future

class VehicleService(
    implicit val system: ActorSystem,
    implicit val cassandraSession: CassandraSession)
    extends _HttpServiceSystem {

  private val vehicleRepository = new VehicleRepository()

  def getAllVehicles(): Future[GetVehiclesDTO] =
    vehicleRepository
      .selectAllByDate(LocalDate.now)
      .map(VehicleResponseMapper.map)

}
