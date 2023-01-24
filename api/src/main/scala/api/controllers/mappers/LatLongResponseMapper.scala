package api.controllers.mappers

import api.controllers.responses.GetLastPositionOfVehicleResponse
import api.controllers.responses.data.LatLongResponse
import domain.data.positions.LatLongEntity

object LatLongResponseMapper {
  def entityToResponse(latLong: LatLongEntity): GetLastPositionOfVehicleResponse =
    GetLastPositionOfVehicleResponse(
      LatLongResponse(
        latLong.latitude,
        latLong.longitude
      )
    )
}
