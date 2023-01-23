package api.controllers.responses

import api.controllers.responses.data.LatLongResponse

case class GetLastPositionOfVehicleResponse(vehicle_position: LatLongResponse) extends Response
