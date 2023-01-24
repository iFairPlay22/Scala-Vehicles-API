package api.controllers.mappers

import api.controllers.responses.CountAvailableVehiclesPerTileResponse
import api.controllers.responses.data.{ TileResponse, VehicleAmountOfTileResponse }
import domain.data.vehicles.VehicleAmountPerTileEntity

object VehicleAmountPerTileMapper {

  def entityToResponse(
      vehicleAmountPerTile: VehicleAmountPerTileEntity
  ): CountAvailableVehiclesPerTileResponse =
    CountAvailableVehiclesPerTileResponse(
      vehicleAmountPerTile.data
        .map(data =>
          VehicleAmountOfTileResponse(
            TileResponse(data._1),
            data._2
          )
        )
        .toList
    )

}
