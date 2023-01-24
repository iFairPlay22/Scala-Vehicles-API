package infra.mappers

import domain.data.positions.LatLongEntity
import domain.data.vehicles.VehicleEntity
import infra.requests.vehicles.VehicleRequest

object VehicleRequestMapper extends RequestMapper {

  def inputToEntity(input: VehicleRequest): VehicleEntity =
    VehicleEntity(
      input.id,
      input.routeId,
      input.runId,
      input.heading,
      input.predictable,
      LatLongEntity(input.latitude, input.longitude),
      input.secondsSinceReport
    )

}
