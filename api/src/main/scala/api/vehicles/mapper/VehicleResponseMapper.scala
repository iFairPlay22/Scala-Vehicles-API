package api.vehicles.mapper

import api.vehicles.dto.{GetVehiclesDTO, VehicleDTO}
import domain.vehicles.VehicleDomain

object VehicleResponseMapper {

  def map(vehicle: VehicleDomain): VehicleDTO =
    VehicleDTO(vehicle.id, vehicle.name)

  def map(vehicles: Seq[VehicleDomain]): GetVehiclesDTO =
    GetVehiclesDTO(
      vehicles
        .map(map))
}
