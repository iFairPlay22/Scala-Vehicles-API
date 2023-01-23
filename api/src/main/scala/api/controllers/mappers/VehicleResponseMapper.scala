package api.controllers.mappers

import api.controllers.responses.{ GetAvailableVehiclesInTileResponse, GetAvailableVehiclesResponse }
import api.controllers.responses.data.VehicleResponse
import domain.vehicles.VehicleEntity

object VehicleResponseMapper {

  def entityToResponse(vehicle: VehicleEntity): VehicleResponse =
    VehicleResponse(
      vehicle.id,
      vehicle.routeId,
      vehicle.runId,
      vehicle.heading,
      vehicle.predictable,
      vehicle.latLong.latitude,
      vehicle.latLong.longitude,
      vehicle.secondsSinceReport
    )

  def entityToResponse(vehicles: Seq[VehicleEntity]): GetAvailableVehiclesResponse =
    GetAvailableVehiclesResponse(
      vehicles
        .map(entityToResponse)
    )

  def entityToResponse2(vehicles: Seq[VehicleEntity]): GetAvailableVehiclesInTileResponse =
    GetAvailableVehiclesInTileResponse(
      vehicles
        .map(entityToResponse)
    )

}
